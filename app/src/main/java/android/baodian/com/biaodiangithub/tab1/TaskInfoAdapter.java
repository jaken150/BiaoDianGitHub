package android.baodian.com.biaodiangithub.tab1;

import android.baodian.com.biaodiangithub.R;
import android.baodian.com.biaodiangithub.model.TaskInfo;
import android.baodian.com.biaodiangithub.util.DL;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.List;

/**
 * Created by chenjj on 6/2/16.
 */
public class TaskInfoAdapter extends UltimateViewAdapter {
    private List<TaskInfo> mList;
    private Context mContext;

    public class ViewHolder extends UltimateRecyclerviewViewHolder {

        private LinearLayout lly;
        private TextView shopname;
        private TextView shop;
        private ImageView platform;
        private TextView key_word_phone;

        public ViewHolder(View itemView, boolean isNormal) {
            super(itemView);
            if (isNormal) {
                lly = (LinearLayout) itemView.findViewById(R.id.lly);
                shopname = (TextView) itemView.findViewById(R.id.tv_shopname);
                shop = (TextView) itemView.findViewById(R.id.tv_shop);
                platform = (ImageView) itemView.findViewById(R.id.iv_platform);
                key_word_phone = (TextView) itemView.findViewById(R.id.tv_key_word_phone);
            }
        }
    }

    public TaskInfoAdapter(List<TaskInfo> mList) {
        this.mList = mList;
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
        mContext = parent.getContext();
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
            orderViewHolder.shopname.setText("店铺名:"+mList.get(position).getShop_name());
            orderViewHolder.shop.setText("链接:"+mList.get(position).getShop());
            if(mList.get(position).getPlatform().equals("taobao")){
                orderViewHolder.platform.setImageResource(R.mipmap.tao);
            }else {
                orderViewHolder.platform.setImageResource(R.mipmap.tmall);
            }
            orderViewHolder.key_word_phone.setText("关键字:"+mList.get(position).getKey_word_phone());
            orderViewHolder.lly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext,TaskSubmitActivity.class));
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
