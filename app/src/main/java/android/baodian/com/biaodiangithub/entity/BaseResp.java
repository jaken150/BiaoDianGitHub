package android.baodian.com.biaodiangithub.entity;

import java.security.PrivateKey;

/**
 * Created by chenjj on 6/2/16.
 */
public class BaseResp {
    public int ErrorCode;
    public String ErrorMsg;
    public String api;

    public int getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(int errorCode) {
        ErrorCode = errorCode;
    }

    public String getErrorMsg() {
        return ErrorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        ErrorMsg = errorMsg;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public boolean checkErrorCode(){
        if (ErrorCode == 0)
            return true;
        else return false;
    }
}
