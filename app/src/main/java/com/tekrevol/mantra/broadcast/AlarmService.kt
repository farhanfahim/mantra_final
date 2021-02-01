package com.tekrevol.mantra.broadcast

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tekrevol.mantra.BaseApplication
import com.tekrevol.mantra.R
import com.tekrevol.mantra.activities.MainActivity
import com.tekrevol.mantra.constatnts.AppConstants
import com.tekrevol.mantra.managers.SharedPreferenceManager
import com.tekrevol.mantra.models.database.AlarmModel
import com.tekrevol.mantra.models.receiving_model.MediaModel
import com.tekrevol.mantra.roomdatabase.DatabaseClient
import java.util.*

class AlarmService : Service() {

    private var arrMovieLines: ArrayList<MediaModel>? = null

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

        getScheduleMantra()

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
            val serviceIntent = Intent(this, AlarmService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                stopService(serviceIntent)
            }
        } else {
            val serviceIntent = Intent(this, AlarmService::class.java)
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


    private fun scheduleAlarmAfterServiceRestart(media: MediaModel, alarmModel: AlarmModel) {
        val gson = Gson()
        val type = object : TypeToken<MediaModel?>() {}.type
        val json = gson.toJson(media, type)
        val alarmIntent: PendingIntent
        val alarmMgr: AlarmManager = BaseApplication.getContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra(AppConstants.MEDIA_MODEL, json)
        intent.putExtra(AppConstants.GENERAL_DB_ID, media.id)
        intent.putExtra(AppConstants.DB_ID, media.dbId)
        intent.putExtra(AppConstants.ALARM_ID, alarmModel.alarmId)
        intent.putExtra(AppConstants.CURRENT_USER_ID, SharedPreferenceManager.getInstance(this).currentUser.id)
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

    private fun dismissAlarm(alarmId: Long, context: Context) {
        val alarmMgr = context.getSystemService(ALARM_SERVICE) as AlarmManager
        val newIntent = Intent(context, AlarmReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(context, alarmId.toInt(), newIntent, 0)
        alarmMgr.cancel(alarmIntent)
    }

    private fun getScheduleMantra() {
        val calendar = Calendar.getInstance()
        //Returns current time in millis
        val currentTime = calendar.timeInMillis

        class GetAllMantra : AsyncTask<Void?, Void?, List<MediaModel>>() {

            override fun onPostExecute(tasks: List<MediaModel>) {
                super.onPostExecute(tasks)
                arrMovieLines = tasks as ArrayList<MediaModel>
                arrMovieLines!!.reverse()
                for (arrMedia in arrMovieLines!!) {
                    val updatedArrAlarm = ArrayList<AlarmModel>()
                    for (arr in arrMedia.arrAlarm) {
                        if (arr.unixDTTM >= currentTime) {
                            updatedArrAlarm.add(arr)
                            scheduleAlarmAfterServiceRestart(arrMedia, arr)
                        } else {
                            dismissAlarm(arr.alarmId.toLong(), this@AlarmService)
                        }
                    }
                    arrMedia.arrAlarm = updatedArrAlarm
                    updateMedia(arrMedia)
                    Log.d("ROOM", "database retrieved successfully")
                    Log.d("ROOM", arrMedia.toString())
                }


            }

            override fun doInBackground(vararg params: Void?): List<MediaModel> {
                return DatabaseClient
                        .getInstance(this@AlarmService)
                        .appDatabase
                        .mediaDao()
                        .getCurrentUserMantra(SharedPreferenceManager.getInstance(this@AlarmService).currentUser.id)
            }
        }

        val getAllMantra = GetAllMantra()
        getAllMantra.execute()
    }

    private fun updateMedia(mediaModel: MediaModel) {
        class UpdateMedia : AsyncTask<Void?, Void?, Void?>() {

            override fun onPostExecute(aVoid: Void?) {
                super.onPostExecute(aVoid)

                Log.d("ROOM", "database updated successfully")
                Log.d("ROOM", mediaModel.toString())
            }

            override fun doInBackground(vararg params: Void?): Void? {
                DatabaseClient.getInstance(this@AlarmService).appDatabase
                        .mediaDao()
                        .updateMedia(mediaModel)
                return null
            }
        }

        val updateMedia = UpdateMedia()
        updateMedia.execute()
    }


}