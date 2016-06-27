package android.baodian.com.biaodiangithub.baesclass;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BaseFragment extends Fragment {

    protected Context mContext;
    protected SweetAlertDialog pDialog = null;
    protected SweetAlertDialog alertDialog = null;
    protected Handler mHandler = new Handler(Looper.myLooper());

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            pDialog =  new SweetAlertDialog(mContext,SweetAlertDialog.PROGRESS_TYPE);
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

    protected void showAlertDialog(String title) {
        showAlertDialog(title, "", true, null);
    }

    protected void showAlertDialog(String title, boolean cancelable) {
        showAlertDialog(title, "", cancelable, null);
    }

    protected void showAlertDialog(String title, boolean cancelable, SweetAlertDialog.OnSweetClickListener confirmListener) {
        showAlertDialog(title, "", cancelable, confirmListener);
    }

    protected void showAlertDialog(String title, String content, boolean cancelable, SweetAlertDialog.OnSweetClickListener confirmListener) {
        alertDialog = null;
        if (alertDialog == null) {
            alertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
            alertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        }
        alertDialog.setCancelable(cancelable);
        alertDialog.setConfirmClickListener(confirmListener);
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setTitle(title);
        alertDialog.setContentText(content);
        alertDialog.setConfirmText("确定");
        alertDialog.setCancelText("取消");
        if (!alertDialog.isShowing())
            alertDialog.show();
    }


    protected void dismissAlertDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }
}
