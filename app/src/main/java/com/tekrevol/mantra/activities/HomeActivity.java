package com.tekrevol.mantra.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
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
import com.tekrevol.mantra.BaseApplication;
import com.tekrevol.mantra.R;
import com.tekrevol.mantra.broadcast.ExampleService;
import com.tekrevol.mantra.constatnts.AppConstants;
import com.tekrevol.mantra.fragments.HomeFragment;
import com.tekrevol.mantra.fragments.RightSideMenuFragment;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.helperclasses.RunTimePermissions;
import com.tekrevol.mantra.libraries.residemenu.ResideMenu;
import com.tekrevol.mantra.managers.ObjectBoxManager;
import com.tekrevol.mantra.models.receiving_model.MediaModel;
import com.tekrevol.mantra.utils.utility.Blur;
import com.tekrevol.mantra.utils.utility.Utils;

import java.util.ArrayList;
import java.util.List;

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
                v.setPadding(0, 0, 0,  insets.getSystemWindowInsetBottom());
                return insets;
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!isMyServiceRunning(ExampleService.class)){
            startService();
        }

    }

    public void startService() {
        Intent serviceIntent = new Intent(this, ExampleService.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(this, serviceIntent);
        }else{
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