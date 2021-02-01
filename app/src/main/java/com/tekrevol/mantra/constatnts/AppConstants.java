package com.tekrevol.mantra.constatnts;

import android.content.Context;
import android.os.Environment;

import com.tekrevol.mantra.BaseApplication;
import com.tekrevol.mantra.managers.SharedPreferenceManager;
import com.tekrevol.mantra.models.receiving_model.MediaModel;

import java.util.ArrayList;


/**
 * Created by khanhamza on 4/20/2017.
 */

public class AppConstants {

    // Temporary User
    public static String tempUserName = "Developer/Tester";


    /**
     * Static Booleans
     */

    public static boolean isForcedResetFragment;

    /**
     * Date Formats
     */

    public static final String INPUT_DATE_FORMAT = "yyyy-dd-MM hh:mm:ss";
    public static final String INPUT_DATE_FORMAT_AM_PM = "yyyy-dd-MM hh:mm:ss a";
    public static final String OUTPUT_DATE_FORMAT = "EEEE dd,yyyy";
    public static final String INPUT_TIME_FORMAT = "yyyy-dd-MM hh:mm:ss a";
    public static final String TIME = "mm:ss";
    public static final String OUTPUT_TIME_FORMAT = "hh:mm a";
    public static final String OUTPUT_UTC = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String OUTPUT_DATE_TIME_FORMAT = "EEEE dd,yyyy hh:mm a";
    public static final String INPUT_LAB_DATE_FORMAT_AM_PM = "mm/dd/yyyy hh:mm:ss a";

    // Custom For AKUH
    public static final String INPUT_DATE_FORMAT_IMMUNIZATION = "dd/MM/yyyy";
    public static final String GENERAL_DATE_FORMAT = "dd-MM-yy";
    public static final String FORMAT_PEOPLESOFT = "yyyy-MM-dd";

    public static final String FORMAT_DATE_SHOW = "dd MMMM yyyy";
  //  public static final String FORMAT_DATE_SEND = "dd/MM/yyyy HH:mm:ss";
    public static final String FORMAT_DATE_SEND = "MM/dd/yyyy hh:mm:ss a";


    /**
     * Path to save MediaModel
     */
    public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getPath()
            + "/" + BaseApplication.getApplicationName();

    public static final String DOC_PATH = ROOT_PATH + "/Docs";

    public static String getUserFolderPath(Context context) {
        return DOC_PATH + "/" + SharedPreferenceManager.getInstance(context).getCurrentUser().getUserDetails().getFullName();
    }


    /**
     * MASKING FORMATs
     */

    public static final String CNIC_MASK = "99999-9999999-9";
    public static final String CARD_MASK = "9999-9999-9999";
    //    public static final String CARD_MASK = "wwww-wwww-wwww";
    public static final String MR_NUMBER_MASK = "999-99-99";


    /*************** INTENT DATA KEYS **************/
    public static final String LABORATORY_MODEL = "laboratoryModel";
    public static final String JSON_STRING_KEY = "JSON_STRING_KEY";
    public static final String IMAGE_PREVIEW_URL = "url";
    public static final String IMAGE_PREVIEW_TITLE = "title";
    public static final String GCM_DATA_OBJECT = "gcmDataObject";
    public static final String GENERAL_DB_ID = "general_db_id";
    public static final String DB_ID = "db_id";
    public static final String ALARM_ID = "alarm_id";
    public static final String MEDIA_MODEL = "media_model";
    public static final String CURRENT_USER_ID = "current_user_id";


    /*******************Preferences KEYS******************/
    public static final String KEY_CURRENT_USER_MODEL = "userModel";
    public static final String KEY_CURRENT_USER_ID = "user_model_id";
    public static final String KEY_CURRENT_USER_EMAIL = "userEmail";
    public static final String KEY_CARD_MEMBER_DETAIL = "cardMemberDetail";
    public static final String KEY_CARD_NUMBER = "card_number";
    public static final String KEY_CODE = "code";
    public static final String KEY_ABOUT = "2";
    public static final String KEY_LIBRARY_OF_POSITIVITY = "4";
    public static final String KEY_PRIVACY = "1";
    public static final String KEY_TERMS = "3";
    public static final String USER_NOTIFICATION_DATA = "USER_NOTIFICATION_DATA";
    public static String FORCED_RESTART = "forced_restart";
    public static final String KEY_REGISTER_VM = "register_vm";
    public static final String KEY_TOKEN = "getToken";
    public static final String KEY_ONE_TIME_TOKEN = "one_time_token";
    public static final String KEY_CROSS_TAB_DATA = "cross_tab";
    public static final String KEY_REGISTERED_DEVICE = "registered_device";
    public static final String KEY_INSERT_REGISTERED_DEVICE = "registered_device";
    public static final String KEY_FIREBASE_TOKEN_UPDATED = "FIREBASE_TOKEN_UPDATED";
    public static final String KEY_PIN_CODE = "pin_code";
    public static final String KEY_IS_PIN_ENABLE = "is_pin_enable";
    public static final String KEY_CURRENT_LOCATION = "current_location";
    public static final String KEY_CURRENT_ALARM_ID = "current_ALARM_ID";
    public static final String IS_LOGIN = "login";


    /**
     * File Name initials if user download the pdf
     */
    public static String FILE_NAME = "Demo-App";


    /**
     * Data Static Strings
     */

    public static final String NO_RECORD_FOUND = "No Record Found";
    public static Boolean IS_LOGOUT = false;

    public static String DEVICE_OS_ANDROID = "android";
    public static String SOCIAL_MEDIA_PLATFORM_FACEBOOK = "android";
    public static final int FILE_TYPE_PUBLIC = 20;
    public static final int FILE_TYPE_PRIVATE = 10;
    public static final int IS_MINE = 1;
    public static final int ACTION_LIKE = 40;
    public static final int ACTION_UNLIKE = 10;
    public static final int ACTION_FAV = 50;
    public static final int ACTION_UNFAV = 30;
    public static final int ACTION_SHARE = 20;
    public static final String ORDER_BY_ID = "id";
    public static final String SORTED_BY = "desc";


    public static final int EXPECTATION_LEVEL_EXCEEDED = 10;
    public static final int EXPECTATION_LEVEL_MET  = 20;
    public static final int EXPECTATION_LEVEL_BELOW  = 30;
    public static final int EXPECTATION_LEVEL_NONE  = 40;

    //private category
    public static final int PRIVATE_CATEGORY = 5;



}
