package android.baodian.com.biaodiangithub.util;

import android.baodian.com.biaodiangithub.BuildConfig;

import net.grandcentrix.tray.AppPreferences;

public class AppConstant {
    public static boolean DEBUGVERSION = BuildConfig.DEBUG_VERSION;
    public static String HOST_URL = "http://192.168.1.110:5000";
//    public static String HOST_URL = "http://115.28.222.78:5000";
    public static String HOST_URL_UPDATE = HOST_URL+"/api/update";
    public static String URL_GET_SMS = HOST_URL+"/api/sms";
    public static String URL_GET_TASK_INFO = HOST_URL+"/api/get_task_info";
    public static String URL_GET_TASK_REVIEW = HOST_URL+"/api/get_task_review";
    public static String URL_SUMBIT_TASK = HOST_URL+"/api/submit_task";
    public static String URL_APP_REGISTER = HOST_URL+"/api/app_register";
    public static String URL_APP_LOGIN = HOST_URL+"/api/app_login";
    public static String URL_MODIFY_USER_INFO = HOST_URL+"/api/modify_user_info";

    public static int TASK_INFO_LIST_TYPE_ALL = 0;
    public static int TASK_INFO_LIST_TYPE_MINE = 1;

}
