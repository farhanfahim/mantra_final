package com.tekrevol.mantra.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.modules.facebooklogin.FacebookHelper;
import com.modules.facebooklogin.FacebookResponse;
import com.modules.facebooklogin.FacebookUser;
import com.tekrevol.mantra.R;
import com.tekrevol.mantra.constatnts.AppConstants;
import com.tekrevol.mantra.enums.BaseURLTypes;
import com.tekrevol.mantra.fragments.LandingFragment;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.helperclasses.RunTimePermissions;
import com.tekrevol.mantra.managers.SharedPreferenceManager;
import com.tekrevol.mantra.managers.retrofit.WebServices;
import com.tekrevol.mantra.models.sending_model.SocialLoginSendingModel;
import com.tekrevol.mantra.models.wrappers.UserModelWrapper;
import com.tekrevol.mantra.models.wrappers.WebResponse;

import java.util.List;

import static com.tekrevol.mantra.constatnts.WebServiceConstants.PATH_SOCIAL_LOGIN;


public class MainActivity extends BaseActivity implements FacebookResponse {

    private FacebookHelper mFbHelper;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mFbHelper = new FacebookHelper(this,
                "id,name,email,gender,birthday,picture",
                this);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mFbHelper.onActivityResult(requestCode, resultCode, data);
    }

    public void fbSignIn() {
        mFbHelper.performSignIn(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        RunTimePermissions.verifyStoragePermissions(this);
        initFragments();
    }

    @Override
    protected int getViewId() {
        return R.layout.activity_main;
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

    private void initFragments() {
        if (SharedPreferenceManager.getInstance(getApplicationContext()).getCurrentUser() == null) {
            addDockableFragment(LandingFragment.newInstance(), false);
        } else {
            openActivity(HomeActivity.class);
            this.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (DeviceUtils.isRooted(getApplicationContext())) {
//            showAlertDialogAndExitApp("This device is rooted. You can't use this app.");
//        } BaseApplication getApp()

        setVisible(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        setVisible(false);
    }

    @Override
    public void onBackPressed() {
        /**
         * Show Close app popup if no or single fragment is in stack. otherwise check if drawer is open. Close it..
         */

        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {

            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
                List<Fragment> fragments = getSupportFragmentManager().getFragments();
                BaseFragment fragment = (BaseFragment) fragments.get(fragments.size() - 1);
                fragment.setTitlebar(titleBar);
            }
        } else {
            closeApp();
        }
    }


    public void showAlertDialogAndExitApp(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

        alertDialog.show();
    }

    @Override
    public void onFbSignInFail() {

    }

    @Override
    public void onFbSignInSuccess() {

    }


    @Override
    public void onFbProfileReceived(FacebookUser facebookUser) {

        SocialLoginSendingModel socialLoginSendingModel = new SocialLoginSendingModel();
        socialLoginSendingModel.setClientId(facebookUser.facebookID);
        socialLoginSendingModel.setDeviceType(AppConstants.DEVICE_OS_ANDROID);
        socialLoginSendingModel.setEmail(facebookUser.email);
        socialLoginSendingModel.setImage(facebookUser.profilePic);
        socialLoginSendingModel.setPlatform(AppConstants.SOCIAL_MEDIA_PLATFORM_FACEBOOK);
        socialLoginSendingModel.setUsername(facebookUser.name);
        socialLoginSendingModel.setToken("abc123");
        socialLoginSendingModel.setDeviceToken("abc123");

        new WebServices(this, "", BaseURLTypes.BASE_URL, true).postAPIAnyObject(PATH_SOCIAL_LOGIN, socialLoginSendingModel.toString(), new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {
                UserModelWrapper userModelWrapper = getGson().fromJson(getGson().toJson(webResponse.result), UserModelWrapper.class);
                onSuccessfullLogin(userModelWrapper.getUser());

                mFbHelper.performSignOut();
            }

            @Override
            public void onError(Object object) {

            }
        });


    }

    @Override
    public void onFBSignOut() {

    }
}