package com.tekrevol.mantra.broadcast;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tekrevol.mantra.BaseApplication;
import com.tekrevol.mantra.R;
import com.tekrevol.mantra.activities.AlarmActivity;
import com.tekrevol.mantra.activities.HomeActivity;
import com.tekrevol.mantra.activities.MainActivity;
import com.tekrevol.mantra.constatnts.AppConstants;
import com.tekrevol.mantra.managers.SharedPreferenceManager;
import com.tekrevol.mantra.models.database.AlarmModel;
import com.tekrevol.mantra.models.receiving_model.MediaModel;
import com.tekrevol.mantra.roomdatabase.DatabaseClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {
    Context ctx;
    long generalDBID = -1;
    static int alarmId = -1;
    static int userId = -1;
    NotificationManager notificationManager;
    public static MediaPlayer mMediaPlayer;

    MediaModel scheduledMantraMediaModel = null;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ALARM", "onReceive: " + "alarm detected");

        Gson gson = new Gson();
        String stringLocation = intent.getStringExtra(AppConstants.MEDIA_MODEL);
        if(stringLocation != null) {
            Type type = new TypeToken<MediaModel>() {
            }.getType();
            scheduledMantraMediaModel = gson.fromJson(stringLocation, type);
            Log.d("ALARM", "onReceive: " + scheduledMantraMediaModel.toString());
        }
        else{
            Log.d("ALARM", "onReceive: failed");
        }

        ctx = context;

        if (SharedPreferenceManager.getInstance(ctx).getCurrentUser() == null) {
            return;
        }

        if (intent.getLongExtra(AppConstants.GENERAL_DB_ID, -1) == -1) {
            Log.d("ALARM", "onReceive: " + "General DB invalid ID");
            return;
        }


        generalDBID = intent.getLongExtra(AppConstants.GENERAL_DB_ID, -1);
        alarmId = intent.getIntExtra(AppConstants.ALARM_ID, -1);
        userId = intent.getIntExtra(AppConstants.CURRENT_USER_ID, -1);


        Log.d("ALARM", "onReceive: " + "General DB ID: " + generalDBID);
        Log.d("ALARM", "onReceive: " + "Alarm DB ID: " + alarmId);
        Log.d("ALARM", "onReceive: " + "CURRENT USER ID: " + userId);

        dismissAlarm(alarmId, ctx);
        Log.d("ALARM", "onReceive: " + "alarm succes");

        try {
            AlarmModel alarmModel = null;

            for (AlarmModel alarm : scheduledMantraMediaModel.getArrAlarm()) {
                if (alarm.getAlarmId() == alarmId) {
                    alarmModel = alarm;
                }
            }

            if (alarmModel != null) {
                scheduledMantraMediaModel.getArrAlarm().remove(alarmModel);
            } else {
                Intent serviceIntent = new Intent(ctx, AlarmService.class);
                ctx.stopService(serviceIntent);
            }
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (userId == SharedPreferenceManager.getInstance(context).getCurrentUser().getId()) {

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {

                showNotification(alarmId, scheduledMantraMediaModel);
                playRingtone(scheduledMantraMediaModel, context);
            } else {
                openActivity(AlarmActivity.class, generalDBID);
            }
        }



    }


    /**
     * Custom method to cancel the set alarm when the work is done for that specific alarm.
     *
     * @param alarmId long unique id of the specific alarm.
     * @param context Context object.
     */
    private void dismissAlarm(long alarmId, Context context) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent newIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, (int) alarmId, newIntent, 0);

        alarmMgr.cancel(alarmIntent);
    }


    /**
     * Create and show a simple notification containing using MediaModel model.
     */
    private void sendNotification(int alarmId, MediaModel mediaModel) {

        Intent intent;
        intent = new Intent(ctx, MainActivity.class);
//        intent.putExtra(constants.K_LOG_ID, logsRowId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, (int) alarmId /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ctx)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(mediaModel.getName())
                .setContentText(mediaModel.getDescription())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(mediaModel.getName()))
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
    }


    public void showNotification(int alarmId, MediaModel mediaModel) {
        Intent intent;
        intent = new Intent(ctx, MainActivity.class);
//        intent.putExtra(constants.K_LOG_ID, logsRowId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("action", "actionName");

        Intent intentAction = new Intent(ctx, ActionReceiver.class);
        intentAction.putExtra("action", "actionName");
        PendingIntent pIntentAction = PendingIntent.getBroadcast(ctx, 1, intentAction, PendingIntent.FLAG_UPDATE_CURRENT);


        PendingIntent pendingIntent = PendingIntent.getActivity(ctx,
                alarmId,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String CHANNEL_ID = "papp_channel";// The id of the channel.
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(mediaModel.getName())
                .setContentText(mediaModel.getReminderText())
                .setAutoCancel(false)
                .setOngoing(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)

                .addAction(0, "Cancel Alarm", pIntentAction);
        notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = ctx.getString(R.string.app_name);// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        stopRingtone();
        notificationManager.notify(0, notificationBuilder.build());

    }

    public void openActivity(Class<?> tClass, long id) {
        Intent i = new Intent(ctx, tClass);
        //i.putExtra(AppConstants.GENERAL_DB_ID, id);
        Gson gson = new Gson();
        Type type = new TypeToken<MediaModel>() {}.getType();
        String json = gson.toJson(scheduledMantraMediaModel, type);

        i.putExtra(AppConstants.MEDIA_MODEL, json);
        Log.d("ALARM INTENT", "openActivity: " + id);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(i);

    }

    public void playRingtone(MediaModel mediaModel, Context context) {
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

                    if (notificationManager != null) {
                        notificationManager.cancelAll();
                        cancelNotification(context, 0);
                        stopRingtone();

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void stopRingtone() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.reset();
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

    }

    public static void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);
    }

    public static class ActionReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getStringExtra("action");
            if (action.equals("actionName")) {
                stopRingtone();
                cancelNotification(context, 0);
            }

            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.sendBroadcast(it);
        }


    }


}