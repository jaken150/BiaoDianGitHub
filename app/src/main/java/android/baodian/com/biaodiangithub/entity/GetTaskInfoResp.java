package android.baodian.com.biaodiangithub.entity;

import android.baodian.com.biaodiangithub.model.TaskInfo;

import java.util.List;

/**
 * Created by chenjj on 6/2/16.
 */
public class GetTaskInfoResp extends BaseResp{
    public List<TaskInfo> data;

    public List<TaskInfo> getData() {
        return data;
    }

    public void setData(List<TaskInfo> data) {
        this.data = data;
    }
}
