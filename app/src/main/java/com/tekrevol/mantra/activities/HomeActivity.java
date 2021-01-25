package com.tekrevol.mantra.activities;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tekrevol.mantra.R;
import com.tekrevol.mantra.broadcast.AlarmReceiver;
import com.tekrevol.mantra.broadcast.ExampleService;
import com.tekrevol.mantra.constatnts.AppConstants;
import com.tekrevol.mantra.constatnts.WebServiceConstants;
import com.tekrevol.mantra.enums.BaseURLTypes;
import com.tekrevol.mantra.enums.DBModelTypes;
import com.tekrevol.mantra.fragments.HomeFragment;
import com.tekrevol.mantra.fragments.RightSideMenuFragment;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.helperclasses.RunTimePermissions;
import com.tekrevol.mantra.libraries.residemenu.ResideMenu;
import com.tekrevol.mantra.managers.ObjectBoxManager;
import com.tekrevol.mantra.managers.retrofit.GsonFactory;
import com.tekrevol.mantra.managers.retrofit.WebServices;
import com.tekrevol.mantra.models.ReminderModel;
import com.tekrevol.mantra.models.database.AlarmModel;
import com.tekrevol.mantra.models.database.GeneralDBModel;
import com.tekrevol.mantra.models.receiving_model.MediaModel;
import com.tekrevol.mantra.models.wrappers.WebResponse;
import com.tekrevol.mantra.utils.utility.Blur;
import com.tekrevol.mantra.utils.utility.Utils;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.KITKAT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static com.tekrevol.mantra.BaseApplication.getContext;

public class HomeActivity extends BaseActivity {

    NavigationView navigationView;
    FrameLayout contMain;
    LinearLayout contParentActivityLayout, containerTitlebar1;
    private LinearLayout containerBottomBar;
    private RightSideMenuFragment rightSideMenuFragment;
    private ResideMenu resideMenu;
    public MediaPlayer mediaPlayer;
    //For Blurred Background
    private Bitmap mDownScaled;
    private String mBackgroundFilename;
    private Bitmap background;

    Call<WebResponse<Object>> webCall;
    private ArrayList<MediaModel> arrMovieLines;
    private ArrayList<MediaModel> arrMedia;
    private ImageView imageBlur;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mediaPlayer = new MediaPlayer();
//        setContentView(R.layout.activity_main);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);


        ViewCompat.setOnApplyWindowInsetsListener(getWindow().getDecorView(), new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
//                v.setPadding(0, 0, 0, v.getPaddingBottom() + insets.getSystemWindowInsetBottom());
                v.setPadding(0, 0, 0, insets.getSystemWindowInsetBottom());
                return insets;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isMyServiceRunning(ExampleService.class)) {
            startService();

            ArrayList<MediaModel> arrayList = ObjectBoxManager.INSTANCE.getAllScheduledMantraMediaModels(this);
            if (!arrayList.isEmpty()) {
                arrMovieLines = arrayList;
                List<Long> dbIdArray = ObjectBoxManager.INSTANCE.test (this);

                for (Long id : dbIdArray){
                    ObjectBoxManager.INSTANCE.removeGeneralDBModel(id);
                }
                getScheduleMantra();


                //Log.d("mydata",genrealDb+"");

            }

        }

    }



    private void scheduleAlarmAfterServiceRestart(long mediaId, AlarmModel alarmModel) {
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra(AppConstants.GENERAL_DB_ID, mediaId);
        intent.putExtra(AppConstants.ALARM_ID, alarmModel.getAlarmId());
        intent.putExtra(AppConstants.CURRENT_USER_ID, sharedPreferenceManager.getCurrentUser().getId());
        alarmIntent = PendingIntent.getBroadcast(getContext(),
                alarmModel.getAlarmId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);

        alarmMgr.cancel(alarmIntent);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, alarmModel.getUnixDTTM(), alarmIntent);
        long triggerAtMillis = alarmModel.getUnixDTTM();
        if (SDK_INT > LOLLIPOP) {
            AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(triggerAtMillis, alarmIntent);
            alarmMgr.setAlarmClock(alarmClockInfo, alarmIntent);
        } else if (SDK_INT > KITKAT) {
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, alarmModel.getUnixDTTM(), alarmIntent);
        } else {
            alarmMgr.set(AlarmManager.RTC_WAKEUP, alarmModel.getUnixDTTM(), alarmIntent);
        }


    }

    private void getScheduleMantra() {

        Calendar calendar = Calendar.getInstance();
        //Returns current time in millis
        long currentTime = calendar.getTimeInMillis();


        for (MediaModel arr : arrMovieLines) {
            long id = ObjectBoxManager.INSTANCE.putGeneralDBModel(0,sharedPreferenceManager.getCurrentUser().getId(), arr.toString(), DBModelTypes.SCHEDULED_MANTRA);
                for (AlarmModel arrAlarm : arr.getAlarms()){
                    if (arrAlarm.getUnixDTTM() > currentTime){
                        scheduleAlarmAfterServiceRestart(id, arrAlarm);
                    }
                }
        }


    }


    public void startService() {
        Intent serviceIntent = new Intent(this, ExampleService.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(this, serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        RunTimePermissions.verifyStoragePermissions(this);

        String intentData = getIntent().getStringExtra(AppConstants.JSON_STRING_KEY);
        navigationView = findViewById(R.id.nav_view);
        navigationView.getBackground().setColorFilter(0x80000000, PorterDuff.Mode.MULTIPLY);
        contMain = findViewById(R.id.contMain);
        contParentActivityLayout = findViewById(R.id.contParentActivityLayout);

//        containerBottomBar = findViewById(R.id.contBottomBar);
        imageBlur = findViewById(R.id.imageBlur);
        setSideMenu(ResideMenu.DIRECTION_RIGHT);
        initFragments(intentData);
        setListeners();


    }

    private void setListeners() {

    }


    public ResideMenu getResideMenu() {
        return resideMenu;
    }

    public void setSideMenu(int direction) {

        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.measurment_background);
        resideMenu.attachToActivity(HomeActivity.this);
        resideMenu.setScaleValue(0.56f);
        resideMenu.setShadowVisible(false);
        setMenuItemDirection(direction);
    }


    public void setMenuItemDirection(int direction) {

        if (direction == ResideMenu.DIRECTION_RIGHT) {
            rightSideMenuFragment = RightSideMenuFragment.newInstance();
            resideMenu.addMenuItem(rightSideMenuFragment, "RightSideMenuFragment", direction);
        }
    }

    public RightSideMenuFragment getRightSideMenuFragment() {
        return rightSideMenuFragment;
    }


    @Override
    protected int getViewId() {
        return R.layout.activity_home;
    }

    @Override
    protected int getTitlebarLayoutId() {
        return R.id.titlebar;
    }


    @Override
    protected int getDrawerLayoutId() {
        return R.id.drawer_layout;
    }

    @Override
    protected int getDockableFragmentId() {
        return R.id.contMain;
    }

    @Override
    protected int getDrawerFragmentId() {
        return R.id.contDrawer;
    }

    private void initFragments(String intentData) {
        addDockableFragment(HomeFragment.newInstance(), false);
    }

    public FrameLayout getContMain() {
        return contMain;
    }

    public LinearLayout getContParentActivityLayout() {
        return contParentActivityLayout;
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                super.onBackPressed();
                List<Fragment> fragments = getSupportFragmentManager().getFragments();
                BaseFragment fragment = (BaseFragment) fragments.get(fragments.size() - 1);
                fragment.setTitlebar(titleBar);
            } else {
                closeApp();
            }
        }
    }

    public ImageView getBlurImage() {
        return imageBlur;
    }

    public void setBlurBackground() {

        // For Future use

////        if (mBackgroundFilename == null) {
//
//        this.mDownScaled = Utils.drawViewToBitmap(this.getMainContentFrame(), Color.parseColor("#fff5f5f5"));
//
//        mBackgroundFilename = getBlurredBackgroundFilename();
//        if (!TextUtils.isEmpty(mBackgroundFilename)) {
//            //context.getMainContentFrame().setVisibility(View.VISIBLE);
//            background = Utils.loadBitmapFromFile(mBackgroundFilename);
////                if (background != null) {
//            getBlurImage().setVisibility(View.VISIBLE);
//            getBlurImage().setImageBitmap(background);
//            getBlurImage().animate().alpha(1);
////                }
//        }
////        } else {
////            getBlurImage().setVisibility(View.VISIBLE);
////            getBlurImage().setImageBitmap(background);
////            getBlurImage().animate().alpha(1);
////        }
    }

    public String getBlurredBackgroundFilename() {
        Bitmap localBitmap = Blur.fastblur(this, this.mDownScaled, 20);
        String str = Utils.saveBitmapToFile(this, localBitmap);
        this.mDownScaled.recycle();
        localBitmap.recycle();
        return str;
    }

    public void removeBlurImage() {
        getBlurImage().setVisibility(View.GONE);
    }

    public void showBottomBar() {
        if (containerBottomBar != null)
            containerBottomBar.setVisibility(View.VISIBLE);
    }

    public void hideBottomBar() {

        if (containerBottomBar != null)
            containerBottomBar.setVisibility(View.GONE);
    }

}