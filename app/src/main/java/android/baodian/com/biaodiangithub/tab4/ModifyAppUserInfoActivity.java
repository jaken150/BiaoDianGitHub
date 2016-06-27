package android.baodian.com.biaodiangithub.tab4;

import android.baodian.com.biaodiangithub.MainApp;
import android.baodian.com.biaodiangithub.R;
import android.baodian.com.biaodiangithub.baesclass.BaseActivity;
import android.baodian.com.biaodiangithub.entity.BaseResp;
import android.baodian.com.biaodiangithub.util.AppConstant;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ModifyAppUserInfoActivity extends BaseActivity {
    private String mType = "";
    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_app_user_info);
        mType = getIntent().getStringExtra("type");
        actionBar.setTitle("修改" + getType_CN(mType));
        et = (EditText) findViewById(R.id.et);
        findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et.getText().length() == 0) {
                    MainApp.toast("请输入内容");
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("phone", MainApp.getInstance().getPhone());
                    jsonObject.put("form", "app");
                    jsonObject.put("type", mType);
                    jsonObject.put("qq", et.getText());
                    jsonObject.put("email", et.getText());
                    jsonObject.put("wx", et.getText());
                    jsonObject.put("tb", et.getText());
                    jsonObject.put("tb2", et.getText());
                    jsonObject.put("tb3", et.getText());
                    showLoading("请稍候",true);
                    MainApp.getInstance().okHttpPost(AppConstant.URL_MODIFY_USER_INFO, jsonObject.toString(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    dismissLoading();
                                    showAlertDialog("网络异常", true);
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String resp = response.body().string();
                            Logger.json(resp);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    dismissLoading();
                                    BaseResp respObj = JSON.parseObject(resp, BaseResp.class);
                                    if (respObj.getErrorCode() != 0) {
                                        MainApp.getInstance().toast(respObj.getErrorMsg());
                                        return;
                                    } else MainApp.getInstance().toast(respObj.getErrorMsg());
                                    ModifyAppUserInfoActivity.this.finish();
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

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    private String getType_CN(String type) {
        if (type.equals("qq"))
            return "QQ";
        else if (type.equals("wx"))
            return "微信";
        else if (type.equals("email"))
            return "Email";
        else if (type.equals("tb"))
            return "店铺1";
        else if (type.equals("tb2"))
            return "店铺2";
        else if (type.equals("tb3"))
            return "店铺3";
        else return "";
    }
}
