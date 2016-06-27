package android.baodian.com.biaodiangithub;

import android.app.Application;
import android.baodian.com.biaodiangithub.model.AppUser;
import android.baodian.com.biaodiangithub.util.DL;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.orhanobut.logger.Logger;

import net.grandcentrix.tray.AppPreferences;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.List;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MainApp extends Application {

    private OkHttpClient okHttpClient;
    public MainActivity mMainAC;
    //从第三方应用获取的参数
    public String Channel_Code;
    public String User_Id;
    public String Channel_Key;
    public AppPreferences appPreferences;
    private static MainApp sInstance;
    public List<Fragment> mFragmentList;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        okHttpClient = new OkHttpClient();
        Logger.init();
//		MobclickAgent.setDebugMode(true);
//		AppConstant.AddShortCut(this);
//		DL.log("MainApp","umeng getDeviceInfo = "+getDeviceInfo(getApplicationContext()));
        appPreferences = new AppPreferences(getApplicationContext());

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
                showImageForEmptyUri(R.mipmap.ic_default_adimage)
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .memoryCache(new WeakMemoryCache())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);

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

    //缓存用户状态
    public void setPhone(String phone) {
        appPreferences.put("PHONE", phone);
    }

    public String getPhone() {
        return appPreferences.getString("PHONE", "");
    }

    public void setQQ(String QQ) {
        appPreferences.put(getPhone() + "QQ", QQ);
    }

    public String getQQ() {
        return appPreferences.getString(getPhone() + "QQ", "");
    }

    public void setTB1(String TB1) {
        appPreferences.put(getPhone() + "TB1", TB1);
    }

    public String getTB1() {
        return appPreferences.getString(getPhone() + "TB1", "");
    }

    public void setTB2(String TB2) {
        appPreferences.put(getPhone() + "TB2", TB2);
    }

    public String getTB2() {
        return appPreferences.getString(getPhone() + "TB2", "");
    }

    public void setTB3(String TB3) {
        appPreferences.put(getPhone() + "TB3", TB3);
    }

    public String getTB3() {
        return appPreferences.getString(getPhone() + "TB3", "");
    }

    public void setCoins(int coins) {
        appPreferences.put(getPhone() + "TB3", coins);
    }

    public int getCoins() {
        return appPreferences.getInt(getPhone() + "TB3", 0);
    }

    public void logout() {
        setPhone("");
        setQQ("");
        setTB1("");
        setTB2("");
        setTB3("");
        setCoins(0);
    }

    public void login(AppUser appUser) {
        setPhone(appUser.getPhone());
        setQQ(appUser.getQq());
        setTB1(appUser.getTb1());
        setTB2(appUser.getTb2());
        setTB3(appUser.getTb3());
        setCoins(appUser.getCoins());
    }

    //版本名
    public static String getVersionName() {
        return getPackageInfo().versionName;
    }

    //版本号
    public static int getVersionCode() {
        return getPackageInfo().versionCode;
    }

    public static PackageInfo getPackageInfo() {
        PackageInfo pi = null;

        try {
            PackageManager pm = getInstance().getApplicationContext().getPackageManager();
            pi = pm.getPackageInfo(getInstance().getApplicationContext().getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    public void exit() {
        System.exit(0);
    }
}
