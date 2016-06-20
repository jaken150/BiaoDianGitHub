package android.baodian.com.biaodiangithub.baesclass;

import android.baodian.com.biaodiangithub.R;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BaseActivity extends AppCompatActivity {

    protected ActionBar actionBar;
    protected SweetAlertDialog pDialog = null ;
    protected SweetAlertDialog alertDialog = null;
    protected Handler mHandler = new Handler(Looper.myLooper());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
    }


    protected void showProgessDialog(String content,boolean cancelable){
        if(pDialog == null){
            pDialog =  new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setCancelable(cancelable);
        }
        pDialog.setTitleText(content);
        pDialog.show();
    }

    protected void dismissProgessDialog(){
        if(pDialog != null && pDialog.isShowing()){
            pDialog.dismiss();
        }
    }


    protected void showAlertDialog(String content,boolean cancelable){
        if(alertDialog == null ){
            alertDialog = new SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE);
            alertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            alertDialog.setCancelable(cancelable);
        }
        pDialog.setTitleText(content);
        pDialog.show();
    }

    protected void dismissAlertDialog(){
        if(alertDialog != null && alertDialog.isShowing()){
            alertDialog.dismiss();
        }
    }
}
