package android.baodian.com.biaodiangithub.tab4;

import android.baodian.com.biaodiangithub.MainApp;
import android.baodian.com.biaodiangithub.baesclass.BaseFragment;
import android.baodian.com.biaodiangithub.login.LoginActivity;
import android.baodian.com.biaodiangithub.tab1.TaskInfoListActivity;
import android.baodian.com.biaodiangithub.util.AppConstant;
import android.baodian.com.biaodiangithub.util.DL;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.baodian.com.biaodiangithub.R;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.github.yoojia.anyversion.AnyVersion;
import com.github.yoojia.anyversion.NotifyStyle;
import com.github.yoojia.anyversion.Version;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Fragment4 extends BaseFragment {
    private String TAG = "Fragment4";

    private TextView tv_phone;
    private BootstrapButton btn_logout;
    private AnyVersion version;
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

        version = AnyVersion.getInstance();
        version.setURL(AppConstant.HOST_URL_UPDATE);
        version.setCallback(new com.github.yoojia.anyversion.Callback() {
            @Override
            public void onVersion(Version version) {
                final Version final_version = version;
                DL.log("New Version");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(mContext, "发现新版本", Toast.LENGTH_SHORT).show();
                        showAlertDialog(final_version.note, true, new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                Uri downloadUri = Uri.parse(final_version.URL);
                                DL.log("toString = "+Environment.getExternalStorageDirectory().getPath() + "/"+final_version.name);
                                DL.log("final_version.name = "+final_version.name);
                                Uri destinationUri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/"+final_version.name);
                                DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
//                                .addCustomHeader("Auth-Token", "YourTokenApiKey")
                                        .setRetryPolicy(new DefaultRetryPolicy())
                                        .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
//                                .setDownloadContext(downloadContextObject)//Optional
                                        .setStatusListener(new DownloadStatusListenerV1() {
                                            @Override
                                            public void onDownloadComplete(DownloadRequest downloadRequest) {
                                                dismissProgessDialog();
                                                DL.log("setStatusListener", "onDownloadComplete");
                                                File app = new File(Environment.getExternalStorageDirectory().getPath(), final_version.name);
                                                DL.log("setStatusListener", "File = "+app.getAbsolutePath());
                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                intent.setDataAndType(Uri.parse("file://"+app), "application/vnd.android.package-archive");
                                                mContext.startActivity(intent);
                                            }

                                            @Override
                                            public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage) {
                                                DL.log("setStatusListener", "onDownloadFailed");
                                            }

                                            @Override
                                            public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {
                                                if(progress % 10 == 0)
                                                    showNumberProgessDialog("正在下载",false,progress);
                                                DL.log("setStatusListener", "progress = " + progress);

                                            }
                                        });
                                int downloadId = downloadManager.add(downloadRequest);
                            }
                        });
                    }
                });
            }
        });

        downloadManager = new ThinDownloadManager();
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
                version.check(NotifyStyle.Dialog);
            }
        });
        v.findViewById(R.id.tv_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                version.check(NotifyStyle.Callback);
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
