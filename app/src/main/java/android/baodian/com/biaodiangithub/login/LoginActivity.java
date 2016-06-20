package android.baodian.com.biaodiangithub.login;

import android.baodian.com.biaodiangithub.MainApp;
import android.baodian.com.biaodiangithub.R;
import android.baodian.com.biaodiangithub.entity.BaseResp;
import android.baodian.com.biaodiangithub.tab4.Fragment4;
import android.baodian.com.biaodiangithub.util.AppConstant;
import android.baodian.com.biaodiangithub.util.DL;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.github.yoojia.anyversion.AnyVersion;
import com.github.yoojia.anyversion.NotifyStyle;
import com.github.yoojia.anyversion.Version;

import org.json.JSONObject;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private BootstrapEditText et_phone, et_pwd;
    private Handler mHandler = new Handler(Looper.myLooper());
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponent();

    }

    private void initComponent() {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("请稍候");
        pDialog.setCancelable(true);
        et_phone = (BootstrapEditText) findViewById(R.id.et_phone);
        et_pwd = (BootstrapEditText) findViewById(R.id.et_pwd);
        if(DL.DEBUGVERSION){
            et_phone.setText("13763319124");
            et_pwd.setText("q");
        }
        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnyVersion version = AnyVersion.getInstance();
                version.setURL(AppConstant.HOST_URL_UPDATE);
                version.setCallback(new com.github.yoojia.anyversion.Callback() {
                    @Override
                    public void onVersion(Version version) {
                        DL.log("New Version");
                        MainApp.toast("New Version: \n" + version);
                    }
                });
                version.check(NotifyStyle.Callback);
//                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
//                finish();
            }
        });
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_phone.getText().length() < 11) {
                    DL.toast(LoginActivity.this, "请输入正确手机号");
                    return;
                }
                if (et_pwd.getText().length() == 0) {
                    DL.toast(LoginActivity.this, "请输入密码");
                    return;
                }
                try{
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("phone",et_phone.getText().toString());
                    jsonObject.put("pwd",et_pwd.getText().toString());
                    pDialog.show();
                    MainApp.getInstance().okHttpPost(AppConstant.URL_APP_LOGIN, jsonObject.toString(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    pDialog.dismiss();
                                    DL.toast(LoginActivity.this,"网络异常");
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String resp = response.body().string();
                            DL.log("LoginActivity","resp = "+resp);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    pDialog.cancel();
                                    BaseResp baseResp = JSON.parseObject(resp, BaseResp.class);
                                    if (baseResp.checkErrorCode()) {
                                        pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                        pDialog.setTitleText(baseResp.getErrorMsg());
                                        pDialog.setCancelable(false);
                                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                pDialog.cancel();
                                                finish();

                                            }
                                        });
                                        pDialog.show();
                                        MainApp.getInstance().setPhone(et_phone.getText().toString());
                                        // TODO: 6/5/16 更新UI
                                        ((Fragment4)MainApp.getInstance().mFragmentList.get(3)).updateUI();
                                    } else {
                                        pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                        pDialog.setTitleText(baseResp.getErrorMsg());
                                        pDialog.setCancelable(true);
                                        pDialog.show();
                                    }
                                }
                            });
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                    DL.toast(LoginActivity.this, "app运行状态异常");
                }
            }
        });
    }
}
