package android.baodian.com.biaodiangithub.login;

import android.app.Activity;
import android.baodian.com.biaodiangithub.MainApp;
import android.baodian.com.biaodiangithub.R;
import android.baodian.com.biaodiangithub.entity.BaseResp;
import android.baodian.com.biaodiangithub.util.AppConstant;
import android.baodian.com.biaodiangithub.util.DL;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;

import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Logger;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private String TAG = "RegisterActivity";
    private BootstrapButton mBTNVerifyCode, mBTNRegister;
    private BootstrapEditText mETMobile, mETPassword1, mETPassword2, mETSmsCode;
    private TimeCountUtils time;
    private Handler mHandler = new Handler(Looper.myLooper());
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initComponent();
        initLister();
    }

    private void initComponent() {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("请稍候");
        pDialog.setCancelable(true);
        mBTNVerifyCode = (BootstrapButton) findViewById(R.id.btn_code);
        mBTNRegister = (BootstrapButton) findViewById(R.id.btn_register);
        mETMobile = (BootstrapEditText) findViewById(R.id.et_phone);
        mETSmsCode = (BootstrapEditText) findViewById(R.id.et_code);
        mETPassword1 = (BootstrapEditText) findViewById(R.id.et_pwd);
        mETPassword2 = (BootstrapEditText) findViewById(R.id.et_pwd2);
        if(DL.DEBUGVERSION){
//            mETMobile.setText("13763319124");
//            mETPassword1.setText("q");
//            mETPassword2.setText("q");
        }
    }

    private void initLister() {
        mBTNVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mETMobile != null && mETMobile.getText().length() != 11) {
                    DL.toast(RegisterActivity.this, "请输入手机号码");
                    return;
                } else {
                    try {
                        JSONObject json = new JSONObject();
                        json.put("phone", mETMobile.getText().toString());
                        json.put("type", 0);
                        pDialog.show();
                        MainApp.getInstance().okHttpPost(AppConstant.URL_GET_SMS, json.toString(), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        DL.toast(RegisterActivity.this,"网络异常");
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                final String resp = response.body().string();
                                DL.log(TAG,"resp = "+resp);
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        pDialog.dismiss();
                                        DL.toast(RegisterActivity.this, "短信已下发，请注意查收。");
                                    }
                                });
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    time = new TimeCountUtils(RegisterActivity.this, 60 * 1000, 1000, mBTNVerifyCode);
                    time.start();
                }
            }
        });
        mBTNRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("phone", mETMobile.getText().toString());
                    jsonObject.put("code", mETSmsCode.getText().toString());
                    jsonObject.put("pwd", mETPassword1.getText().toString());
                    pDialog = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("请稍候");
                    pDialog.setCancelable(true);
                    pDialog.show();
                    MainApp.getInstance().okHttpPost(AppConstant.URL_APP_REGISTER, jsonObject.toString(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    DL.toast(RegisterActivity.this,"网络异常");
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String resp = response.body().string();
                            DL.log(TAG,"resp = "+resp);
                            com.orhanobut.logger.Logger.json(resp);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    pDialog.dismiss();
                                    BaseResp baseResp = JSON.parseObject(resp, BaseResp.class);
                                    if (baseResp.checkErrorCode()) {
                                        pDialog = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                        pDialog.setTitleText("注册成功");
                                        pDialog.setCancelable(false);
                                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                finish();
                                            }
                                        });
                                        pDialog.show();
                                        MainApp.getInstance().setPhone(mETMobile.getText().toString());
                                        // TODO: 6/5/16 更新UI
                                    } else {
                                        pDialog = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.ERROR_TYPE);
                                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                        pDialog.setTitleText(baseResp.getErrorMsg());
                                        pDialog.setCancelable(true);
                                        pDialog.show();
                                    }
                                }
                            });
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


    //自定义60S倒时器
    public class TimeCountUtils extends CountDownTimer {
        private Activity mActivity;
        private BootstrapButton btn;//按钮

        /**
         * 在这个构造方法里需要传入三个参数，一个是Activity，一个是总的时间millisInFuture，一个是countDownInterval，
         * 然后就是你在哪个按钮上做这个是，就把这个按钮传过来就可以了
         */
        public TimeCountUtils(Activity activity, long millisInFuture, long countDownInterval, BootstrapButton btn) {
            super(millisInFuture, countDownInterval);
            this.btn = btn;
            this.mActivity = activity;
        }

        //点击的时候
        public void onTick(long millisInFuture) {
            btn.setClickable(false);//不能点击
            btn.setText(millisInFuture / 1000 + "秒后可重新获取");//设置倒计时间
            //设置按钮为灰色
            btn.setBackgroundColor(Color.parseColor("#BDBDBD"));
            Spannable spannable = new SpannableString(btn.getText().toString());//获取按钮的文字
            /**
             * new ForegroundColorSpan(Color.RED)设置所选文本颜色
             *  0, 2, 从第几个到第几个
             *  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE没用，可为0
             */
            spannable.setSpan(new ForegroundColorSpan(Color.RED), 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//将倒计时间显示为红色
            btn.setText(spannable);
        }

        @Override
        public void onFinish() {
            btn.setText("重新获取验证码");
            btn.setClickable(true);//点击重新获取
            btn.setBackgroundColor(Color.parseColor("#F54F73"));
        }
    }
}
