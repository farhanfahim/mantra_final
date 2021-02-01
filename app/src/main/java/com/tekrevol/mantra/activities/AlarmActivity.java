package com.tekrevol.mantra.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.material.navigation.NavigationView;
import com.tekrevol.mantra.BaseApplication;
import com.tekrevol.mantra.R;
import com.tekrevol.mantra.constatnts.AppConstants;
import com.tekrevol.mantra.fragments.AlarmSelectionFragment;
import com.tekrevol.mantra.models.receiving_model.MediaModel;

public class AlarmActivity extends BaseActivity {

    private NavigationView navigationView;
    //    public String API_KEY = WebServiceConstants.API_KEY;
//    public String SESSION_ID = "2_MX40NjM1NDMxMn5-MTU2MTU0NTg2MjYxNn5WYlFjRkkydXNQSWwyM0wrUXVHSWJkUEp-fg";
//    public String TOKEN = "T1==cGFydG5lcl9pZD00NjM1NDMxMiZzaWc9NDMxMGIwOGI2YTRhMmQ1ZTUzZmMwYmU3MjNiOWZlODVlZTQxMzUwMDpzZXNzaW9uX2lkPTJfTVg0ME5qTTFORE14TW41LU1UVTJNVFUwTlRnMk1qWXhObjVXWWxGalJra3lkWE5RU1d3eU0wd3JVWFZIU1dKa1VFcC1mZyZjcmVhdGVfdGltZT0xNTYxNTQ1ODkxJm5vbmNlPTAuMDgwMTQ2MzMxNDA2NjgzOTImcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTU2MjE1MDY5MCZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";
    public final String LOG_TAG = MainActivity.class.getSimpleName();
    public final int RC_SETTINGS_SCREEN_PERM = 123;
    public final int RC_VIDEO_APP_PERM = 124;
    MediaModel mediaModel;
    Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        if (Build.VERSION.SDK_INT == 26) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            this.setTurnScreenOn(true);
        } else {
            final Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }


      /*  mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(this, alert);
        } catch (Exception e) {
            e.printStackTrace();
        }*/


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        RunTimePermissions.verifyStoragePermissions(this);

        Intent intent = getIntent();
        mediaModel = (MediaModel) intent.getSerializableExtra(AppConstants.MEDIA_MODEL);

        initFragments(mediaModel);

        navigationView = findViewById(R.id.nav_view);
        navigationView.getBackground().setColorFilter(0x80000000, PorterDuff.Mode.MULTIPLY);
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

    private void initFragments(MediaModel mediaModel) {
        playRingtone(mediaModel);
        addDockableFragment(AlarmSelectionFragment.newInstance(mediaModel), false);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        finish();
        openActivity(HomeActivity.class);


        /**
         * Show Close app popup if no or single fragment is in stack. otherwise check if drawer is open. Close it..
         */

//        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
//
//            if (drawerLayout.isDrawerOpen(Gravity.START)) {
//                drawerLayout.closeDrawer(Gravity.START);
//            } else {
//                super.onBackPressed();
//                List<Fragment> fragments = getSupportFragmentManager().getFragments();
//                BaseFragment fragment = (BaseFragment) fragments.get(fragments.size() - 1);
//                fragment.setTitlebar(titleBar);
//            }
//        } else {
//            finish();
//        }
    }

    @Override
    protected void onDestroy() {
        stopRingtone();
        super.onDestroy();
    }

    /**
     * Stop Ringtone if playing
     */

    public void stopRingtone() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.reset();
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

    }


    /**
     * Play default ringtone
     *
     * @param mediaModel
     */

    public void playRingtone(MediaModel mediaModel) {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(mediaModel.getFileAbsoluteUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
         //   mMediaPlayer.setLooping(true);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
//                    stopRingtone();

                    try {
                        if (!isFinishing()) {
                            finish();
//                    addDockableFragment(DetailFragment.newInstance(mediaModel), false);
                            sendNotification((int) mediaModel.getId(),mediaModel);
                            openActivity(HomeActivity.class);
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Create and show a simple notification containing using MediaModel model.
     *
     */
    private void sendNotification(int alarmId,  MediaModel mediaModel) {

        Intent intent;
        intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.putExtra(constants.K_LOG_ID, logsRowId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), (int)alarmId /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.img_mantra_status_logo)
                .setContentTitle(mediaModel.getReminderText())
             /*   .setContentText("text")*/
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("missed reminder"))
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify(alarmId /* ID of notification */, notificationBuilder.build());
        }
    }


}