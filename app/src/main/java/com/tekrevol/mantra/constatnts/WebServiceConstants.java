package com.tekrevol.mantra.constatnts;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by khanhamza on 09-Mar-17.
 */


public class WebServiceConstants {


    private static Map<String, String> headers;

    public static Map<String, String> getHeaders(String token) {
        if (headers == null) {
            headers = new HashMap<>();
            headers.put("_token", token);
        }
        return headers;
    }

    /**
     * URLs
     */


    /**
     * BEFORE LIVE DEPLOYMENT
     * Change URL Live/ UAT
     * Change Image URL
     * Check Version Code
     * BaseApplication Fabric enable
     */



    // DEMO
    public static final String BASE_URL = "http://mantra.demo.servstaging.com/";
    public static final String IMAGE_BASE_URL = "http://mantra.demo.servstaging.com/api/resize/";


    // Live
//    public static final String BASE_URL = "http://app.wakememantra.com/";
//    public static final String IMAGE_BASE_URL = "http://app.wakememantra.com/api/v1/resize/";


   /* // STAGING
    public static final String BASE_URL = "http://mantra.servstaging.com";
    public static final String IMAGE_BASE_URL = "http://mantra.servstaging.com/api/resize/";
*/
    // DEV
    //public static final String BASE_URL = "http://mantra.apps.fomarkmedia.com/";
//    public static final String BASE_URL = "http://mantraapp.apps.fomarkmedia.com/";
//    public static final String IMAGE_BASE_URL = "http://mantraapp.apps.fomarkmedia.com/api/resize/";

    // LOCAL MACHINE
//    public static final String BASE_URL = "http://192.168.29.49/papp/";
//    public static final String IMAGE_BASE_URL = "http://192.168.29.49/papp/api/resize/";


    /**
     * API PATHS NAMES
     */

    public static final String PATH_REGISTER = "register";
    public static final String PATH_LOGIN = "login";
    public static final String PATH_ACTIONS = "user-actions";
    public static final String PATH_FEEDBACK = "feed-back";
    public static final String PATH_REMINDER = "my_mantra";
    public static final String PATH_GET_DEPARTMENTS = "departments";
    public static final String PATH_GET_SPECIALIZATIONS = "specializations";
    public static final String PATH_GET_USERS = "users";
    public static final String PATH_GET_USERS_SLASH = "users/";
    public static final String PATH_GET_REFRESH = "refresh";
    public static final String PATH_PROFILE = "profile";
    public static final String PATH_GIFTS = "gifts";
    public static final String PATH_REDEEM_POINTS = "redeem-points";
    public static final String PATH_TASKS = "tasks";
    public static final String PATH_ACCEPT_TASK = "task-users";
    public static final String PATH_COMPLETE_TASK = "upload-completed-task";
    public static final String PATH_REVIEWS = "reviews";
    public static final String PATH_ADD_DEPENDENT = "addDependant";
    public static final String PATH_CANCEL_TASK = "cancel-task";
    public static final String PATH_CHANGE_DEPENDENT_PASSWORD = "change-dependant-password";
    public static final String PATH_SESSIONS = "sessions";
    public static final String PATH_ACCEPT_SESSION = "accept-session-request/";
    public static final String PATH_DECLINE_SESSION = "decline-session-request/";
    public static final String PATH_COMPLETE_SESSION = "complete-session/";
    public static final String PATH_START_SESSION = "start-session/";
    public static final String PATH_FORGET_PASSWORD = "forget-password";
    public static final String PATH_CATEGORY = "categories";
    public static final String PATH_PARENTID = "parent_id";
    public static final String PATH_MEDIA = "media";
    public static final String PATH_REMINDERS = "reminders";
    public static final String PATH_CATEGORY_ID = "categories/";
    public static final String PATH_ME = "me";
    public static final String PATH_VERIFY_RESET_CODE = "verify-reset-code";
    public static final String PATH_RESET_PASSWORD = "reset-password";
    public static final String PATH_CHANGE_PASSWORD = "change-password";
    public static final String PATH_PAGES = "pages";
    public static final String PATH_SOCIAL_LOGIN = "social_login";
    public static final String PATH_VERIFY_COMPLETED_SESSION = "verify-completed-session/";


    /**
     * QUERY PARAMS
     */

    public static final String Q_PARAM_ROLE = "role";
    public static final String Q_PARAM_LIMIT = "limit";
    public static final String Q_PARAM_OFFSET = "offset";

    public static final String Q_PARAM_ORDERBY = "orderBy";
    public static final String Q_PARAM_SORTED = "sortedBy";
    public static final String Q_PARAM_TOP_MENTOR = "top_mentor";
    public static final String Q_PARAM_MY_MENTOR = "my_mentor";
    public static final String Q_PARAM_SEARCH = "search";
    public static final String Q_PARAM_DEPT_ID = "department_id";
    public static final String Q_PARAM_TYPE = "type";
    public static final String Q_PARAM_AVAILABLE = "available";
    public static final String Q_PARAM_STATUS = "status";
    public static final String Q_PARAM_MENTOR_ID = "mentor_id";
    public static final String Q_PARAM_CURRENT_MENTOR = "current_mentor";
    public static final String Q_PARAM_SESSION_HISTORY = "session_history";
    public static final String Q_PARAM_SESSION_FROM = "session_from";
    public static final String Q_PARAM_SESSION_TO = "session_to";
    public static final String Q_PARAM_TYPE_FILTER = "type_filter";
    public static final String Q_PARAM_UPCOMING_SESSION_REQUEST = "upcoming_session_request";
    public static final String Q_PARAM_EMAIL = "email";
    public static final String Q_PARAM_CATEGORIESID = "category_id";
    public static final String Q_PARAM_IS_MINE = "is_mine";
    public static final String Q_PARAM_ROOT = "root";
    public static final String DAILY_MANTRA = "is_daily_mantra";
    public static final String ADMIN_MANTRA = "is_admin_mantra";
    public static final String Q_MOVIELINE = "is_movieline";
    public static final String Q_FAVOURITE = "my_favourite";
    public static final String Q_DRAFT = "is_mine";
    public static final String Q_SHARE = "my_share";
    public static final String Q_SLUG_ABOUT = "about_us";
    public static final String Q_SHEDULED = "my_mantra";
    public static final String Q_PARAM_SLUG = "slug";


    /**
     * STATUS
     */

    public static final int PARAMS_TOKEN_EXPIRE = 401;
    public static final int PARAMS_TOKEN_BLACKLIST = 402;


    /**
     * WSC KEYS
     */

    public static final String WSC_KEY_ATTACHMENT = "attachment[]";
    public static final String WSC_KEY_ATTACHMENT_FILE = "file";


    public static final String API_KEY = "46354312";
    public static final String SECRET = "d3c4046485d6ef92672123f7a9926f2967361d09";


}
