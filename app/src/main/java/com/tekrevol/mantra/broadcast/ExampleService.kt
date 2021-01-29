package com.tekrevol.mantra.broadcast

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.tekrevol.mantra.BaseApplication
import com.tekrevol.mantra.R
import com.tekrevol.mantra.activities.MainActivity
import com.tekrevol.mantra.constatnts.AppConstants
import com.tekrevol.mantra.enums.DBModelTypes
import com.tekrevol.mantra.managers.ObjectBoxManager
import com.tekrevol.mantra.managers.SharedPreferenceManager
import com.tekrevol.mantra.models.database.AlarmModel
import com.tekrevol.mantra.models.receiving_model.MediaModel
import java.util.*

class ExampleService : Service() {

    private var arrMovieLines: ArrayList<MediaModel>? = null
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

        val arrayList = ObjectBoxManager.INSTANCE.getAllScheduledMantraMediaModels(this)
        if (!arrayList.isEmpty()) {
            arrMovieLines = arrayList
            val dbIdArray: List<Long> = ObjectBoxManager.INSTANCE.test(this)
            for (id in dbIdArray) {
                ObjectBoxManager.INSTANCE.removeGeneralDBModel(id!!)
            }
            getScheduleMantra()
        }
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


    private fun scheduleAlarmAfterServiceRestart(mediaId: Long, alarmModel: AlarmModel) {
        val alarmMgr: AlarmManager
        val alarmIntent: PendingIntent
        alarmMgr = BaseApplication.getContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra(AppConstants.GENERAL_DB_ID, mediaId)
        intent.putExtra(AppConstants.ALARM_ID, alarmModel.alarmId)
        intent.putExtra(AppConstants.CURRENT_USER_ID, SharedPreferenceManager.getInstance(this).getCurrentUser().getId())
        alarmIntent = PendingIntent.getBroadcast(BaseApplication.getContext(),
                alarmModel.alarmId, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        alarmMgr.cancel(alarmIntent)
        alarmMgr[AlarmManager.RTC_WAKEUP, alarmModel.unixDTTM] = alarmIntent
        val triggerAtMillis = alarmModel.unixDTTM
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            val alarmClockInfo = AlarmManager.AlarmClockInfo(triggerAtMillis, alarmIntent)
            alarmMgr.setAlarmClock(alarmClockInfo, alarmIntent)
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, alarmModel.unixDTTM, alarmIntent)
        } else {
            alarmMgr[AlarmManager.RTC_WAKEUP, alarmModel.unixDTTM] = alarmIntent
        }
    }

    private fun getScheduleMantra() {
        val calendar = Calendar.getInstance()
        //Returns current time in millis
        val currentTime = calendar.timeInMillis
        Collections.reverse(arrMovieLines)
        for (arr in arrMovieLines!!) {
            val id = ObjectBoxManager.INSTANCE.putGeneralDBModel(0, SharedPreferenceManager.getInstance(this).getCurrentUser().getId(), arr.toString(), DBModelTypes.SCHEDULED_MANTRA)
            for (arrAlarm in arr.alarms) {
                if (arrAlarm.unixDTTM > currentTime) {
                    scheduleAlarmAfterServiceRestart(id, arrAlarm)
                }
            }
        }
    }


}