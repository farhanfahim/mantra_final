package com.tekrevol.mantra.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.tekrevol.mantra.activities.MainActivity;

public class BootCompleteReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String log = "Action: " + intent.getAction() + "\n" +
                "URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n";
        Log.d(TAG, log);

        // assumes WordService is a registered service
//        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
        Intent i = new Intent(context, AlarmService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(i);
        } else {
            context.startService(i);
        }
//        }
    }

}
