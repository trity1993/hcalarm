package com.tencent.api;

import com.tencent.open.utils.HttpUtils;
import com.tencent.tauth.IRequestListener;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

/** 使用requestAsync、request等通用方法调用sdk未封装的接口时，例如上传图片、查看相册等。
 * 应用在调用SDK提供的接口时，将实现了对应回调接口的实例传入。
 * 当SDK的接口调用完成后，具体如登录、应用邀请和应用分享调用完成后，会回调传入的接口实例。
 * Created by TryIT on 2015/6/10.
 */
public class BaseApiListener implements IRequestListener {
    @Override
    public void onComplete(JSONObject jsonObject) {

    }

    @Override
    public void onIOException(IOException e) {

    }

    @Override
    public void onMalformedURLException(MalformedURLException e) {

    }

    @Override
    public void onJSONException(JSONException e) {

    }

    @Override
    public void onConnectTimeoutException(ConnectTimeoutException e) {

    }

    @Override
    public void onSocketTimeoutException(SocketTimeoutException e) {

    }

    @Override
    public void onNetworkUnavailableException(HttpUtils.NetworkUnavailableException e) {
        // 当前网络不可用时触发此异常
    }

    @Override
    public void onHttpStatusException(HttpUtils.HttpStatusException e) {
        // http请求返回码非200时触发此异常
    }

    @Override
    public void onUnknowException(Exception e) {
        // 出现未知错误时会触发此异常
    }
}
