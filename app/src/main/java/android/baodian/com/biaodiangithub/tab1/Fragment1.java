package android.baodian.com.biaodiangithub.tab1;

import android.baodian.com.biaodiangithub.R;
import android.baodian.com.biaodiangithub.util.DL;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Fragment1 extends Fragment {
    private String TAG = "Fragment1";
    private Context mContext;
    private SweetAlertDialog pDialog;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment1, container, false);
        v.findViewById(R.id.lly_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext,TaskInfoListActivity.class));
            }
        });
        return v;
    }
}