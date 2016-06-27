package android.baodian.com.biaodiangithub.model;

public class TaskReview {
    public static int STATUS_PARTIVIPATE = -2;
    public static int STATUS_SUBMIT = 0;
    private String task_id;
    private String shop;

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    private String item_name;
    private String taskuserphone;
    private String submitterphone;
    private String imageurl1;
    private String imageurl2;
    private String imageurl3;
    private String updatetime;

    private int status;

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getTaskuserphone() {
        return taskuserphone;
    }

    public void setTaskuserphone(String taskuserphone) {
        this.taskuserphone = taskuserphone;
    }

    public String getSubmitterphone() {
        return submitterphone;
    }

    public void setSubmitterphone(String submitterphone) {
        this.submitterphone = submitterphone;
    }

    public String getImageurl1() {
        return imageurl1;
    }

    public void setImageurl1(String imageurl1) {
        this.imageurl1 = imageurl1;
    }

    public String getImageurl2() {
        return imageurl2;
    }

    public void setImageurl2(String imageurl2) {
        this.imageurl2 = imageurl2;
    }

    public String getImageurl3() {
        return imageurl3;
    }

    public void setImageurl3(String imageurl3) {
        this.imageurl3 = imageurl3;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
