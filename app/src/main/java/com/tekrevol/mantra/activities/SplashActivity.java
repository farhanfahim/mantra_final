package com.tekrevol.mantra.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;

import com.tekrevol.mantra.R;
import com.tekrevol.mantra.managers.SharedPreferenceManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SPLASH SCREEN";
    @BindView(R.id.contParentLayout)
    RelativeLayout contParentLayout;
    private final int SPLASH_TIME_OUT = 2000;
    private final int ANIMATIONS_DELAY = 2000;
    private final int ANIMATIONS_TIME_OUT = 250;
    private final int FADING_TIME = 500;
    private boolean hasAnimationStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        printHashKey(getBaseContext());
//        contParentLayout.setVisibility(View.INVISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        MobileAds.initialize(this);


        TextView txtVersionNumber = findViewById(R.id.txtVersionNumber);


        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo info = packageManager.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
            txtVersionNumber.setText("Build Version: " + info.versionName);

        } catch (PackageManager.NameNotFoundException e) {
            txtVersionNumber.setText("");
            e.printStackTrace();
        }



    }


    private void changeActivity(Class activityClass) {
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                Intent i;
                // This method will be executed once the timer is over
                // Start your app main activity
                i = new Intent(SplashActivity.this, activityClass);

                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, R.anim.fade_out);
                // close this activity
                finish();
            }
        }, ANIMATIONS_TIME_OUT);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {

            if (SharedPreferenceManager.getInstance(getApplicationContext()).getCurrentUser() == null) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        animateSplashLayout(true);
                        changeActivity(MainActivity.class);
                    }
                }, ANIMATIONS_DELAY);

            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        changeActivity(HomeActivity.class);
//                        animateSplashLayout(false);
                    }
                }, ANIMATIONS_DELAY);
            }
        }
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

    public static void printHashKey(Context context) {
        // Add code to print out the key hash
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}
