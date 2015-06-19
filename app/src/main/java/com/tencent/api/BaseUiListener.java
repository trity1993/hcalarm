package com.tencent.api;

import android.content.Context;
import android.util.Log;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import cn.hclab.alarm.mvp.model.SignModel;
import cn.hclab.alarm.utils.Tools;

/** IUiListener：调用SDK已经封装好的接口时，例如：登录、快速支付登录、应用分享、应用邀请等接口。
 * Created by TryIT on 2015/6/10.
 */
public class BaseUiListener implements IUiListener {
    private Context context;
    private SignModel signModel;
    private JSONObject JSONResponse;
    private String errorStr;
    public BaseUiListener(Context context){
        this.context=context;
    }
    public BaseUiListener(Context context,SignModel signModel){
        this(context);
        this.signModel=signModel;
    }
    @Override
    public void onComplete(Object response) {
        if (null == response) {
            Tools.showResultDialog(context, "返回为空", "登录失败");
            return;
        }
        JSONObject jsonResponse = (JSONObject) response;
        if (null != jsonResponse && jsonResponse.length() == 0) {
            Tools.showResultDialog(context, "返回为空", "登录失败");
            return;
        }
        this.JSONResponse=jsonResponse;
        Log.e("loginmsg", response.toString());
        doComplete(jsonResponse);//提供空方法，进行复写实现相关的操作。
        if(signModel!=null){
            signModel.onSuccess();
        }
    }

    @Override
    public void onError(UiError e) {
        Log.e("onError:", "code:" + e.errorCode + ", msg:"
                + e.errorMessage + ", detail:" + e.errorDetail);
        errorStr="code:" + e.errorCode + ", msg:"
                + e.errorMessage + ", detail:" + e.errorDetail;
    }

    @Override
    public void onCancel() {

    }
    //相应的方法，为回调进行处理
    protected void doComplete(JSONObject values) {
    }

    public JSONObject getResponseMsg(){
        return JSONResponse;
    }
    public String getErrorMsg(){
        return errorStr;
    }
}
