package com.tekrevol.mantra.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.tekrevol.mantra.activities.MainActivity;

public class BootCompleteReceiver extends BroadcastReceiver {


    Context mContext;
    private final String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";


    @Override
    public void onReceive(Context context, Intent intent) {
        // All registered broadcasts are received by this
        mContext = context;
        String action = intent.getAction();
        if (action.equalsIgnoreCase(BOOT_ACTION)) {
            //check for boot complete event & start your service
           // startService();
        }

    }

    private void startService() {
        //here, you will start your service
        Intent mServiceIntent = new Intent();
        mServiceIntent.setAction(".broadcast.ExampleService");
        mContext.startService(mServiceIntent);
    }

}
