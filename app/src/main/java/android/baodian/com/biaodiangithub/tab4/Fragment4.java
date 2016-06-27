package android.baodian.com.biaodiangithub.tab4;

import android.baodian.com.biaodiangithub.BuildConfig;
import android.baodian.com.biaodiangithub.MainApp;
import android.baodian.com.biaodiangithub.baesclass.BaseFragment;
import android.baodian.com.biaodiangithub.login.LoginActivity;
import android.baodian.com.biaodiangithub.model.Update;
import android.baodian.com.biaodiangithub.util.AppConstant;
import android.baodian.com.biaodiangithub.util.DL;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.baodian.com.biaodiangithub.R;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.orhanobut.logger.Logger;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;
import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Fragment4 extends BaseFragment {
    private String TAG = "Fragment4";

    private TextView tv_phone;
    private BootstrapButton btn_logout;
    private Handler mHandler = new Handler(Looper.myLooper());
    private ThinDownloadManager downloadManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DL.log(TAG, "onCreate ");
    }

    public void updateUI() {
        DL.log(TAG, "updateUI");
        if (MainApp.getInstance().getPhone().length() > 0) {
            //已登录情况下
            DL.log(TAG, "已登录");
            tv_phone.setText(MainApp.getInstance().getPhone());
            btn_logout.setVisibility(View.VISIBLE);
        } else {
            DL.log(TAG, "未登录");
            tv_phone.setText("点击登录");
            tv_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
            });
            btn_logout.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment4, container, false);
        tv_phone = (TextView) v.findViewById(R.id.tv_phone);
        btn_logout = (BootstrapButton) v.findViewById(R.id.btn_logout);

        if (MainApp.getInstance().getPhone().length() > 0) {
            //已登录情况下
            tv_phone.setText(MainApp.getInstance().getPhone());
            btn_logout.setVisibility(View.VISIBLE);
        } else {
            tv_phone.setText("点击登录");
            tv_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
            });
            btn_logout.setVisibility(View.GONE);
        }

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainApp.getInstance().setPhone("");
                updateUI();
            }
        });

        v.findViewById(R.id.tv_invate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        if(BuildConfig.DEBUG_VERSION)
            ((TextView)v.findViewById(R.id.tv_update)).setText("版本更新 V_0."+MainApp.getInstance().getVersionCode());
        else
            ((TextView)v.findViewById(R.id.tv_update)).setText("版本更新 V_1."+MainApp.getInstance().getVersionCode());
        v.findViewById(R.id.tv_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MainApp.getInstance().okHttpGet(AppConstant.URL_UPDATE, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    DL.toast(mContext, "网络异常");
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String resp = response.body().string();
                            DL.log("f4 resp = "+resp);
                            Logger.json(resp);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    final Update update = JSON.parseObject(resp, Update.class);
                                    DL.log("update.getCode() = "+update.getCode());
                                    if (update.getCode() > MainApp.getInstance().getVersionCode()) {
                                        downloadManager = new ThinDownloadManager();
                                        //有可用更新
                                        showAlertDialog(update.note, true, new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismiss();
                                                Uri downloadUri = Uri.parse(update.url);
                                                DL.log("toString = " + Environment.getExternalStorageDirectory().getPath() + "/" + update.name);
                                                DL.log("final_version.name = " + update.name);
                                                Uri destinationUri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/" + update.name);
                                                DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                                                        .setRetryPolicy(new DefaultRetryPolicy())
                                                        .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                                                        .setStatusListener(new DownloadStatusListenerV1() {
                                                            @Override
                                                            public void onDownloadComplete(DownloadRequest downloadRequest) {
                                                                dismissLoading();
                                                                DL.log("setStatusListener", "onDownloadComplete");
                                                                File app = new File(Environment.getExternalStorageDirectory().getPath(), update.name);
                                                                DL.log("setStatusListener", "File = " + app.getAbsolutePath());
                                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                intent.setDataAndType(Uri.parse("file://" + app), "application/vnd.android.package-archive");
                                                                mContext.startActivity(intent);
                                                            }

                                                            @Override
                                                            public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage) {
                                                                DL.log("setStatusListener", "onDownloadFailed");
                                                                DL.toast(mContext,"网络异常，下载失败");
                                                            }

                                                            @Override
                                                            public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {
                                                                if (progress % 10 == 0)
                                                                    showLoading("已下载"+progress+"%", false);
                                                                DL.log("setStatusListener", "progress = " + progress);
                                                            }
                                                        });
                                                int downloadId = downloadManager.add(downloadRequest);
                                            }
                                        });
                                    } else {
                                        //已是最新
                                        DL.toast(mContext, "已是最新");
                                    }
                                }
                            });
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    DL.toast(mContext, "接口出错");
                }
            }
        });


        v.findViewById(R.id.tv_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ModifyAppUserInfoActivity.class);
                i.putExtra("type", "qq");
                startActivity(i);
            }
        });
        v.findViewById(R.id.tv_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ModifyAppUserInfoActivity.class);
                i.putExtra("type", "email");
                startActivity(i);
            }
        });
        v.findViewById(R.id.tv_wx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ModifyAppUserInfoActivity.class);
                i.putExtra("type", "wx");
                startActivity(i);
            }
        });
        v.findViewById(R.id.tv_tb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ModifyAppUserInfoActivity.class);
                i.putExtra("type", "tb");
                startActivity(i);
            }
        });
        v.findViewById(R.id.tv_tb2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ModifyAppUserInfoActivity.class);
                i.putExtra("type", "tb2");
                startActivity(i);
            }
        });
        v.findViewById(R.id.tv_tb3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ModifyAppUserInfoActivity.class);
                i.putExtra("type", "tb3");
                startActivity(i);
            }
        });
        return v;
    }
}
