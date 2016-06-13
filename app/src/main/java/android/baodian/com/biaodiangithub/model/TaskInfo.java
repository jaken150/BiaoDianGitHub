package android.baodian.com.biaodiangithub.model;

/**
 * Created by chenjj on 6/2/16.
 */
public class TaskInfo {
    private static String key_word_search = "key_word_search";//关键字搜索
    private static String shop_search = "shop_search";//店铺搜索
    private static String direct_bus = "direct_bus";//直通车
    private static String tao_kou_ling = "tao_kou_ling";//淘口令
    private String shop_name;
    private String shop;
    private String platform;
    private String key_word_phone;
    private String item_link;
    private String item_price;
    private String item_place;

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_link() {
        return item_link;
    }

    public void setItem_link(String item_link) {
        this.item_link = item_link;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getItem_place() {
        return item_place;
    }

    public void setItem_place(String item_place) {
        this.item_place = item_place;
    }

    private String item_name;

    public String getTask_type_cn() {
        if(task_type.equals(key_word_search))
            return "关键字搜索";
        else  if(task_type.equals(shop_search))
            return "店铺搜索";
        else  if(task_type.equals(direct_bus))
            return "直通车";
        else  if(task_type.equals(tao_kou_ling))
            return "淘口令";
        else return "关键字搜索";
    }


    public String getTask_type() {
        return task_type;
    }

    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }

    private String task_type;

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getKey_word_phone() {
        return key_word_phone;
    }

    public void setKey_word_phone(String key_word_phone) {
        this.key_word_phone = key_word_phone;
    }
}
