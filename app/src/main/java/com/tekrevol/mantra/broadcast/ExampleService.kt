package com.tekrevol.mantra.broadcast

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.tekrevol.mantra.BaseApplication
import com.tekrevol.mantra.R
import com.tekrevol.mantra.activities.MainActivity
import com.tekrevol.mantra.constatnts.AppConstants

class ExampleService : Service() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0)
        val notification = NotificationCompat.Builder(this, BaseApplication.CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentIntent(pendingIntent)
                .build()
        startForeground(1, notification)
        //do heavy work on a background thread
        //stopSelf();
        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                    BaseApplication.CHANNEL_ID,
                    "Example Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (AppConstants.IS_LOGOUT) {
            val serviceIntent = Intent(this, ExampleService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                stopService(serviceIntent)
            }
        } else {
            val serviceIntent = Intent(this, ExampleService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(this, serviceIntent)
            } else {
                startService(serviceIntent)
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}