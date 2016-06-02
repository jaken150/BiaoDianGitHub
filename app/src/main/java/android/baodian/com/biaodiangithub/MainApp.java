package android.baodian.com.biaodiangithub;

import android.app.Application;
import android.baodian.com.biaodiangithub.util.DL;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MainApp extends Application{

    private OkHttpClient okHttpClient;
    public MainActivity mMainAC;
    //从第三方应用获取的参数
    public String Channel_Code;
    public String User_Id;
    public String Channel_Key;

    private static MainApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        okHttpClient = new OkHttpClient();
        Logger.init();
//		MobclickAgent.setDebugMode(true);
//		AppConstant.AddShortCut(this);
//		DL.log("MainApp","umeng getDeviceInfo = "+getDeviceInfo(getApplicationContext()));
    }

    public static MainApp getInstance() {
        return sInstance;
    }

    public static void toast(String text) {
        Toast.makeText(getInstance(), text, Toast.LENGTH_SHORT).show();
    }

    public void okHttpGet(String url, Callback callback) throws IOException {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public void okHttpPost(String url, String json, Callback callback) {
        try {
            DL.log("MainApp", "okHttpPost  url = " + url);
            DL.log("MainApp", "okHttpPost json = " + json);
            final MediaType JSON
                    = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder().url(url).post(body).build();
            okHttpClient.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exit() {
        System.exit(0);
    }
}