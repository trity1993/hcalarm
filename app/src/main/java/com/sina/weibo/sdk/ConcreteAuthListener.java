package com.sina.weibo.sdk;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import cc.trity.model.entities.Authentication;
import cn.hclab.alarm.R;
import cn.hclab.alarm.mvp.model.SignModel;

/**
 * 微博认证授权回调类。
 * 1. SSO 授权时，需要在 {@link #} 中调用 {@link SsoHandler#authorizeCallBack} 后，
 *    该回调才会被执行。
 * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
 * 授权成功后，SDK 会将 access_token、expires_in、uid 等通过 Bundle 的形式返回，在 onComplete 凼数中，
 * 请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
 * 可以获取该信息。具体如何保存和 Token 信息由开发者自行处理
 */
public class ConcreteAuthListener implements WeiboAuthListener {
    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken mAccessToken;
    private Activity mActivity;
    private SignModel signModel;
    private String errorMsg;//错误代码


    private String phoneNum;//使用手机注册将会得到
    public ConcreteAuthListener(Activity mAcitivity,SignModel signModel){
        this.mActivity=mAcitivity;
        this.signModel=signModel;
    }

    @Override
    public void onComplete(Bundle values) {
        // 从 Bundle 中解析 Token
        mAccessToken = Oauth2AccessToken.parseAccessToken(values);
        //从这里获取用户输入的 电话号码信息
        setPhoneNum( mAccessToken.getPhoneNum());

        if (mAccessToken.isSessionValid()) {
            signModel.onSuccess();

        } else {
            /* 以下几种情况，您会收到 Code：
             1. 当您未在平台上注册的应用程序的包名与签名时；
             2. 当您注册的应用程序包名与签名不正确时；
             3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
             */
            errorMsg = values.getString("code");
            signModel.onFailure();

        }
    }

    @Override
    public void onWeiboException(WeiboException e) {
        Log.e("Weibo Auth exception", e.getMessage());
    }

    @Override
    public void onCancel() {

    }

    /**
     * 得到授权信息 Authentication
     * 并保存 Token 保存到 SharedPreferences
     */
    public Authentication getAuthentication(){
        return AccessTokenKeeper.writeAccessToken(mActivity, mAccessToken);
    }
    /**
     * 得到错误信息
     */
    public String getErrorMsg(){
        String message = mActivity.getString(R.string.weibo_login_error);
        if (!TextUtils.isEmpty(errorMsg)) {
            errorMsg = message + "\nObtained the code: " + errorMsg;
        }
        return errorMsg;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
