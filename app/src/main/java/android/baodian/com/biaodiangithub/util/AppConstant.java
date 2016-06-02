package android.baodian.com.biaodiangithub.util;

import android.baodian.com.biaodiangithub.BuildConfig;

public class AppConstant {
    public static boolean DEBUGVERSION = BuildConfig.DEBUG_VERSION;
    public static String HOST_URL = "http://192.168.1.105:5000";

    public static String URL_GET_SMS = HOST_URL+"/auth/sms";
    public static String URL_GET_TASK_INFO = HOST_URL+"/auth/get_task_info";
}
