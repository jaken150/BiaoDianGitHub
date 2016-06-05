package android.baodian.com.biaodiangithub.tab4;

import android.baodian.com.biaodiangithub.MainApp;
import android.baodian.com.biaodiangithub.login.LoginActivity;
import android.baodian.com.biaodiangithub.tab1.TaskInfoListActivity;
import android.baodian.com.biaodiangithub.util.DL;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.baodian.com.biaodiangithub.R;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Fragment4 extends Fragment {
    private String TAG = "Fragment4";
    private Context mContext;
    private SweetAlertDialog pDialog;
    private TextView tv_phone;
    private BootstrapButton btn_logout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DL.log(TAG, "onCreate ");
        pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("请稍候");
        pDialog.setCancelable(true);

    }

    public void updateUI(){
        DL.log(TAG,"updateUI");
        if (MainApp.getInstance().getPhone().length() > 0) {
            //已登录情况下
            DL.log(TAG,"已登录");
            tv_phone.setText(MainApp.getInstance().getPhone());
            btn_logout.setVisibility(View.VISIBLE);
        }else {
            DL.log(TAG,"未登录");
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
        }else {
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
        return v;
    }
}
