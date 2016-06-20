package android.baodian.com.biaodiangithub.tab3;

import android.baodian.com.biaodiangithub.R;
import android.baodian.com.biaodiangithub.model.TaskReview;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.List;

public class TaskReviewAdapter extends UltimateViewAdapter {
    private List<TaskReview> mList;
    private Context mContext;

    public class ViewHolder extends UltimateRecyclerviewViewHolder {

        private LinearLayout lly;
        private TextView tv_shop_name;
        private TextView tv_status;
        private TextView tv_updatetime;

        public ViewHolder(View itemView, boolean isNormal) {
            super(itemView);
            if (isNormal) {
                lly = (LinearLayout) itemView.findViewById(R.id.lly);
                tv_shop_name = (TextView) itemView.findViewById(R.id.tv_item_name);
                tv_status = (TextView) itemView.findViewById(R.id.tv_status);
                tv_updatetime = (TextView) itemView.findViewById(R.id.tv_updatetime);
            }
        }
    }

    public TaskReviewAdapter(List<TaskReview> mList) {
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
                .inflate(R.layout.list_item_taskreview, parent, false);
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
        if (getItemViewType(position) == VIEW_TYPES.NORMAL) {
            ViewHolder orderViewHolder = (ViewHolder) holder;
            orderViewHolder.tv_shop_name.setText("店铺名:"+mList.get(position).getShop());
            if(mList.get(position).getStatus() == 0)
                orderViewHolder.tv_status.setText("未审核");
            else if(mList.get(position).getStatus() == 1)
                orderViewHolder.tv_status.setText("已审核");
            else if(mList.get(position).getStatus() == -1)
                orderViewHolder.tv_status.setText("不通过");
            orderViewHolder.tv_updatetime.setText(mList.get(position).getUpdatetime());

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
