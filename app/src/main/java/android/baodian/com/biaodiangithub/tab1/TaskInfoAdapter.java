package android.baodian.com.biaodiangithub.tab1;

import android.app.Activity;
import android.baodian.com.biaodiangithub.MainApp;
import android.baodian.com.biaodiangithub.R;
import android.baodian.com.biaodiangithub.entity.BaseResp;
import android.baodian.com.biaodiangithub.login.LoginActivity;
import android.baodian.com.biaodiangithub.model.TaskInfo;
import android.baodian.com.biaodiangithub.model.TaskReview;
import android.baodian.com.biaodiangithub.util.AppConstant;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by chenjj on 6/2/16.
 */
public class TaskInfoAdapter extends UltimateViewAdapter {
    private List<TaskInfo> mList;
//    private Context mContext;
    private Handler mHandler;
    private Activity mActivity;
    private int mType;//显示任务类型（0：全部；1：我的）
//    private SweetAlertDialog pDialog;

    public class ViewHolder extends UltimateRecyclerviewViewHolder {

        private LinearLayout lly;
        private TextView tv_shop_name, tv_item_name, tv_task_detail, tv_item_link,tv_task_type_key_word_phone;
        private ImageView iv_platform;
        private BootstrapButton btn_copy, btn_participate;


        public ViewHolder(View itemView, boolean isNormal) {
            super(itemView);
            if (isNormal) {
                lly = (LinearLayout) itemView.findViewById(R.id.lly);
                tv_shop_name = (TextView) itemView.findViewById(R.id.tv_shop_name);
                tv_item_name = (TextView) itemView.findViewById(R.id.tv_item_name);
                tv_item_link = (TextView) itemView.findViewById(R.id.tv_item_link);
                tv_task_detail = (TextView) itemView.findViewById(R.id.tv_task_detail);
                iv_platform = (ImageView) itemView.findViewById(R.id.iv_platform);
                tv_task_type_key_word_phone = (TextView) itemView.findViewById(R.id.tv_key_word_phone);
                btn_copy = (BootstrapButton) itemView.findViewById(R.id.btn_copy);
                btn_participate = (BootstrapButton) itemView.findViewById(R.id.btn_participate);
            }
        }
    }

    public TaskInfoAdapter(List<TaskInfo> mList, Handler handler, Activity activity, int type) {
        this.mList = mList;
        this.mHandler = handler;
        this.mActivity = activity;
        this.mType = type;
    }

    @Override
    public RecyclerView.ViewHolder newFooterHolder(View view) {
        return new ViewHolder(view, false);
    }

    @Override
    public RecyclerView.ViewHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
//        mContext = parent.getContext();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_taskinfo, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v, true);
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        return mList.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == UltimateViewAdapter.VIEW_TYPES.NORMAL) {
            ViewHolder orderViewHolder = (ViewHolder) holder;
            final int final_position = position;
            orderViewHolder.tv_shop_name.setText("店铺:" + mList.get(position).getShop());
            orderViewHolder.tv_item_link.setText( mList.get(position).getItem_link());
            orderViewHolder.tv_item_name.setText("商品:" + mList.get(position).getItem_name());
            orderViewHolder.tv_task_detail.setText("任务和奖励:");//todo
            if (mList.get(position).getPlatform().equals("taobao")) {
                orderViewHolder.iv_platform.setImageResource(R.mipmap.tao);
            } else {
                orderViewHolder.iv_platform.setImageResource(R.mipmap.tmall);
            }
            orderViewHolder.tv_task_type_key_word_phone.setText(mList.get(position).getTask_type_cn() + ":" + mList.get(position).getKey_word());
            final String final_id = mList.get(position).getId();
            if (mType == 0) {
                orderViewHolder.btn_participate.setText("抢任务");
                orderViewHolder.btn_participate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("task_id", final_id);
                            jsonObject.put("task_review_app_user_phone", MainApp.getInstance().getPhone());
                            jsonObject.put("status", TaskReview.STATUS_PARTIVIPATE);
                            MainApp.getInstance().okHttpPost(AppConstant.URL_SUMBIT_TASK, jsonObject.toString(), new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Toast.makeText(mActivity, "网络异常", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    final String resp = response.body().string();
                                    Logger.json(resp);
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                final BaseResp respObj = JSON.parseObject(resp, BaseResp.class);
                                                if (!respObj.checkErrorCode()) {
                                                    MainApp.getInstance().toast(respObj.getErrorMsg());
                                                    return;
                                                }
                                                MainApp.getInstance().toast(respObj.getErrorMsg());
                                                mActivity.finish();
                                            } catch (Exception e) {
                                                e.printStackTrace();
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
            } else {
                orderViewHolder.btn_participate.setText("提交任务");
                orderViewHolder.btn_participate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MainApp.getInstance().getPhone().length() > 0) {
                            //已登录情况下
                            Intent i = new Intent(mActivity, TaskSubmitActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("TaskInfo", mList.get(final_position));
                            i.putExtras(bundle);
                            mActivity.startActivity(i);
                        } else {
                            MainApp.toast("请先登录");
                            mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
                        }

                    }
                });
            }
            final String item_link_final = mList.get(position).getItem_link();
            orderViewHolder.btn_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cm = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setPrimaryClip(ClipData.newPlainText("MIMETYPE_TEXT_PLAIN", item_link_final));
                    MainApp.toast("复制成功");
                }
            });

        }


    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
