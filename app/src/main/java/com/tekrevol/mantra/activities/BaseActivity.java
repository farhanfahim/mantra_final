package com.tekrevol.mantra.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.tekrevol.mantra.BaseApplication;
import com.tekrevol.mantra.R;
import com.tekrevol.mantra.broadcast.AlarmReceiver;
import com.tekrevol.mantra.callbacks.GenericClickableInterface;
import com.tekrevol.mantra.constatnts.AppConstants;
import com.tekrevol.mantra.fragments.LeftSideMenuFragment;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.fragments.abstracts.GenericDialogFragment;
import com.tekrevol.mantra.managers.SharedPreferenceManager;
import com.tekrevol.mantra.managers.retrofit.GsonFactory;
import com.tekrevol.mantra.models.receiving_model.UserModel;
import com.tekrevol.mantra.widget.TitleBar;


public abstract class BaseActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;
    protected TitleBar titleBar;
    public LeftSideMenuFragment leftSideMenuFragment;
    public BaseFragment baseFragment;
    private Gson gson;
    public SharedPreferenceManager sharedPreferenceManager;
    public GenericClickableInterface genericClickableInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getViewId());
        setAndBindTitleBar();
        drawerLayout = findViewById(getDrawerLayoutId());
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        addDrawerFragment();
        gson = GsonFactory.getSimpleGson();
        sharedPreferenceManager = SharedPreferenceManager.getInstance(this);

//        Window window = getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        window.setStatusBarColor(Color.BLACK);


    }

    /**
     * Give Resource id of the view you want to inflate
     *
     * @return
     */
    protected abstract int getViewId();


    protected abstract int getTitlebarLayoutId();

    protected abstract int getDrawerLayoutId();

    protected abstract int getDockableFragmentId();

    protected abstract int getDrawerFragmentId();

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }


    public Gson getGson() {
        return gson;
    }


    public void addDrawerFragment() {
        leftSideMenuFragment = LeftSideMenuFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(getDrawerFragmentId(), leftSideMenuFragment).commit();
    }

    private void setAndBindTitleBar() {
        titleBar = findViewById(getTitlebarLayoutId());
        titleBar.setVisibility(View.GONE);
        titleBar.resetViews();
    }

    public void onSuccessfullLogin(UserModel userModel) {


        sharedPreferenceManager.putObject(AppConstants.KEY_CURRENT_USER_MODEL, userModel);
        sharedPreferenceManager.putValue(AppConstants.KEY_CURRENT_USER_ID, userModel.getId());
        sharedPreferenceManager.putValue(AppConstants.KEY_TOKEN, userModel.getAccessToken());
        sharedPreferenceManager.putValue(AppConstants.IS_LOGIN, true);

        finish();
        openActivity(HomeActivity.class);
    }

    public void closeApp() {
        final GenericDialogFragment genericDialogFragment = GenericDialogFragment.newInstance();

        genericDialogFragment.setTitle("Quit");
        genericDialogFragment.setMessage("Do you want to quit your app?");
        genericDialogFragment.setButton1("Yes", new GenericClickableInterface() {
            @Override
            public void click() {
                //
                genericDialogFragment.dismiss();
                // SharedPreferenceManager.getInstance(BaseActivity.this).clearDB();
                finish();
                //clearAllActivitiesExceptThis(MainActivity.class);
            }
        });

        genericDialogFragment.setButton2("No", new GenericClickableInterface() {
            @Override
            public void click() {
                genericDialogFragment.getDialog().dismiss();
            }
        });
        genericDialogFragment.show(getSupportFragmentManager(), null);
    }

    public void addDockableFragment(Fragment fragment, boolean isTransition) {
        baseFragment = (BaseFragment) fragment;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (isTransition) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        fragmentTransaction.replace(getDockableFragmentId(), fragment).addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    public void addFragment(Fragment fragment, boolean isTransition) {
        baseFragment = (BaseFragment) fragment;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(getDockableFragmentId(), fragment).addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    public TitleBar getTitleBar() {
        return titleBar;
    }

    public void openActivity(Class<?> tClass) {
        Intent i = new Intent(this, tClass);
        startActivity(i);
    }

    public void openImagePreviewActivity(String url, String title) {
        Intent i = new Intent(this, ImagePreviewActivity.class);
        i.putExtra(AppConstants.IMAGE_PREVIEW_TITLE, title);
        i.putExtra(AppConstants.IMAGE_PREVIEW_URL, url);
        startActivity(i);
    }

    public void openActivity(Class<?> tClass, String object) {
        Intent i = new Intent(this, tClass);
        i.putExtra(AppConstants.JSON_STRING_KEY, object);
        startActivity(i);
    }

    public LeftSideMenuFragment getLeftSideMenuFragment() {
        return leftSideMenuFragment;
    }

    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    public void clearAllActivitiesExceptThis(Class<?> cls) {
        Intent intents = new Intent(this, cls);
        intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intents);
        finish();
    }


    public void emptyBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm == null) return;
        for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
            fm.popBackStack();
        }
    }

    public void popBackStack() {
        if (getSupportFragmentManager() == null) {
            return;
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    public void popStackTill(int stackNumber) {
        FragmentManager fm = getSupportFragmentManager();
        if (fm == null) return;
        for (int i = stackNumber; i < fm.getBackStackEntryCount(); i++) {
            fm.popBackStack();
        }
    }

    public void popStackTill(String tag) {
        FragmentManager fm = getSupportFragmentManager();
        if (fm == null) {
            return;
        }

        int backStackEntryCount = fm.getBackStackEntryCount();
        for (int i = backStackEntryCount - 1; i > 0; i--) {
            if (fm.getBackStackEntryAt(i).getName().equalsIgnoreCase(tag)) {
                return;
            } else {
                fm.popBackStack();
            }
        }
    }

    public void notifyToAll(int event, Object data) {
        BaseApplication.getPublishSubject().onNext(new Pair<>(event, data));
    }

    public void refreshFragment(BaseFragment fragment) {
        popBackStack();
        addDockableFragment(fragment, false);

    }

    public void setGenericClickableInterface(GenericClickableInterface genericClickableInterface) {
        this.genericClickableInterface = genericClickableInterface;
    }


    private void dismissAlarm(long alarmId, Context context) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent newIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, (int) alarmId, newIntent, 0);

        alarmMgr.cancel(alarmIntent);
    }

}