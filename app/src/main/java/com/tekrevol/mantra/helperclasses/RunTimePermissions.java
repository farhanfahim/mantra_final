package com.tekrevol.mantra.helperclasses;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import com.tekrevol.mantra.activities.BaseActivity;
import com.tekrevol.mantra.helperclasses.ui.helper.UIHelper;

/**
 * Created by khanhamza on 23-May-17.
 */

public class RunTimePermissions {



    // Storage Permissions variables
    private static final int REQUEST_CODE = 6361;
    private static String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,

    };

    //persmission method.
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int recordAudio = ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);

        if (writePermission != PackageManager.PERMISSION_GRANTED
                || readPermission != PackageManager.PERMISSION_GRANTED
                || writePermission != PackageManager.PERMISSION_GRANTED
                || recordAudio != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS,
                    REQUEST_CODE
            );
        }
    }

    public static boolean isAllPermissionGiven(Context context, BaseActivity activity, boolean showAlert) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        ) {
            return true;
        }

        if (showAlert) {

            UIHelper.showAlertDialog("This app needs to access your record and storage permission to make mantra", "Permissions", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    verifyStoragePermissions(activity);
                }
            }, context);

        }

        return false;
    }

}
