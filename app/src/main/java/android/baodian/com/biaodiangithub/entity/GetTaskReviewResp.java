package android.baodian.com.biaodiangithub.entity;

import android.baodian.com.biaodiangithub.model.TaskInfo;
import android.baodian.com.biaodiangithub.model.TaskReview;

import java.util.List;

public class GetTaskReviewResp extends BaseResp{
    public List<TaskReview> data;

    public List<TaskReview> getData() {
        return data;
    }

    public void setData(List<TaskReview> data) {
        this.data = data;
    }
}
