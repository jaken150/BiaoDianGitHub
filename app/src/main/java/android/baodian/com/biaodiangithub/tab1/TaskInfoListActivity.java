package android.baodian.com.biaodiangithub.tab1;

import android.baodian.com.biaodiangithub.MainApp;
import android.baodian.com.biaodiangithub.R;
import android.baodian.com.biaodiangithub.entity.GetTaskInfoResp;
import android.baodian.com.biaodiangithub.model.TaskInfo;
import android.baodian.com.biaodiangithub.util.AppConstant;
import android.baodian.com.biaodiangithub.util.DL;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TaskInfoListActivity extends AppCompatActivity {
    private String TAG = "TaskInfoListActivity";
    private UltimateRecyclerView mListView;
    private LinearLayoutManager mLinearLayoutManager;
    private SweetAlertDialog pDialog;
    private Handler mHandler = new Handler(Looper.myLooper());
    private TaskInfoAdapter mAdapter;
    private List<TaskInfo> mList = new ArrayList<>();
    private int mPage = 1;
    private int mPageSize = 10;
    private boolean mIsLoadmoreNow = false;
    private boolean mIsRefreshNew = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DL.log(TAG, "onCreate");
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("请稍候");
        pDialog.setCancelable(true);
        if (mList == null || mList.size() == 0) {
            httpPostQuery();
        }
        initListView();

    }

    private void httpPostQuery() {
        try {
            JSONObject json = new JSONObject();

            json.put("phone", "13763319124");
//            json.put("page", mPage);
//            json.put("pagesize", mPageSize);
            MainApp.getInstance().okHttpPost(AppConstant.URL_GET_TASK_INFO, json.toString(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    DL.log(TAG, "onFailure:" + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String resp = response.body().string();
                    Logger.json(resp);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                            try {
                                GetTaskInfoResp respObj = JSON.parseObject(resp, GetTaskInfoResp.class);
                                if (respObj.getErrorCode() != 0) {
                                    pDialog.setTitleText(respObj.getErrorMsg());
                                    pDialog.show();
                                    return;
                                }
                                DL.log(TAG, "getOrder().size() = " + respObj.getData().size());
                                if (respObj.getData().size() > 0) {
//                            if (mIsRefreshNew) {//下拉刷新，先清除list里的数据
//                                mList.clear();
//                                mAdapter.notifyDataSetChanged();
//                            }
                                    if (respObj.getData().size() < mPageSize) {
                                        //当返回数据小于pagesize时，禁止自动加载
                                        mListView.disableLoadmore();
                                        mAdapter.enableLoadMore(false);
                                    } else {
                                        mListView.reenableLoadmore();
                                        mAdapter.enableLoadMore(true);
                                    }
                                    for (TaskInfo item : respObj.getData()) {
//                                    if (mIsRefreshNew && DL.DEBUGVERSION)
//                                        orderInfo.setCardnum(orderInfo.getCardnum() + "刷新标志");
                                        mAdapter.insertLastInternal(mList, item);
                                    }
                                    DL.log(TAG, "getItemCount = " + mAdapter.getItemCount());
                                    DL.log(TAG, "getItemViewType = " + mAdapter.getItemViewType(mAdapter.getItemCount() - 1));
                                    if (mIsLoadmoreNow) {
                                        mIsLoadmoreNow = false;
                                    }
                                    if (mIsRefreshNew) {
                                        mIsRefreshNew = false;
                                        mListView.setRefreshing(false);
                                        mLinearLayoutManager.scrollToPosition(0);
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                MainApp.toast("服务器返回数据格式不正确，请稍后重试");
                            }
                        }


                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListView() {
        mListView = (UltimateRecyclerView) findViewById(R.id.ultimate_recycler_view);
        mListView.setHasFixedSize(false);
        mAdapter = new TaskInfoAdapter(mList);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mListView.setLayoutManager(mLinearLayoutManager);

        enableLoadMore();
        mListView.setRecylerViewBackgroundColor(Color.parseColor("#ffffff"));
        enableRefreshGoogleMaterialStyle();
        mListView.setEmptyView(R.layout.empty_view, UltimateRecyclerView.EMPTY_CLEAR_ALL);
        mListView.setAdapter(mAdapter);

    }

    private void enableRefreshGoogleMaterialStyle() {
        mListView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mList.clear();
                mAdapter.notifyDataSetChanged();
                mListView.reenableLoadmore();
                mIsRefreshNew = true;
                mPage = 1;
                httpPostQuery();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mListView.setDefaultSwipeToRefreshColorScheme(
                    this.getColor(android.R.color.holo_orange_light),
                    this.getColor(android.R.color.holo_orange_dark),
                    this.getColor(android.R.color.holo_red_dark),
                    this.getColor(android.R.color.holo_red_light));
        } else {
            mListView.setDefaultSwipeToRefreshColorScheme(
                    getResources().getColor(android.R.color.holo_orange_light),
                    getResources().getColor(android.R.color.holo_orange_dark),
                    getResources().getColor(android.R.color.holo_red_dark),
                    getResources().getColor(android.R.color.holo_red_light));
        }
    }

    private void enableLoadMore() {
        mListView.setLoadMoreView(R.layout.custom_bottom_progressbar);
        mListView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        DL.log(TAG, "loadMor... ");
                        mIsLoadmoreNow = true;
                        mPage++;
                        httpPostQuery();
                    }
                }, 1000);
            }
        });
    }
}
