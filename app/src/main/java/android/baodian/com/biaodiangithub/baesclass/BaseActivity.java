package android.baodian.com.biaodiangithub.baesclass;

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

    protected void showLoading(){
        showLoading("请稍候","",true);
    }

    protected void showLoading(String title){
        showLoading(title,"",true);
    }

    protected void showLoading(String title,boolean cancelable){
        showLoading(title,"",cancelable);
    }

    protected void showLoading(String title,String content,boolean cancelable){
        if(pDialog == null){
            pDialog =  new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setCancelable(cancelable);
        }
        pDialog.setTitleText(title);
        pDialog.setContentText(content);
        if(!pDialog.isShowing())
            pDialog.show();
    }

    protected void dismissLoading(){
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
