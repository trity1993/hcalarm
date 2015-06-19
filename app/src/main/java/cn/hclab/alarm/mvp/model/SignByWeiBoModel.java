package cn.hclab.alarm.mvp.model;

import android.app.Activity;

import com.sina.weibo.sdk.ConcreteAuthListener;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import cc.trity.common.Common;
import cc.trity.model.entities.AlarmUserInfo;
import cn.hclab.alarm.mvp.views.SignView;
import cn.hclab.alarm.ui.HcAlarmApp;

/** 处理微博登录
 * Created by TryIT on 2015/6/14.
 */
public class SignByWeiBoModel implements SignModel<SsoHandler> {
    private Activity mActivity;
    //微博认证接口
    private AuthInfo mWeiboAuth;
    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken mAccessToken;

    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;
    //微博授权的回调接口
    private ConcreteAuthListener concreteAuthListener;
    private HcAlarmApp hcAlarmApp;
    private SignView signView;
    public SignByWeiBoModel(Activity mActivity,HcAlarmApp hcAlarmApp,SignView signView){
        this.mActivity=mActivity;
        this.hcAlarmApp=hcAlarmApp;
        this.signView=signView;
        //微博初始化
        mWeiboAuth = new AuthInfo(mActivity, Common.WEIBO_APP_ID, Common.REDIRECT_URL, Common.SCOPE);
    }

    @Override
    public SsoHandler signOperation(String tag) {
        mSsoHandler = new SsoHandler(mActivity, mWeiboAuth);
        concreteAuthListener=new ConcreteAuthListener(mActivity,this);
        //先使用客户端，后使用web
        if(tag.equals(Common.WEIBO_TAG_ALL))
            mSsoHandler.authorize(concreteAuthListener);
        else if(tag.equals(Common.WEIBO_TAG_PHONE))
            mSsoHandler.registerOrLoginByMobile("",concreteAuthListener);

        return mSsoHandler;
    }

    @Override
    public void onSuccess() {
        AlarmUserInfo alarmUserInfo=new AlarmUserInfo();
        alarmUserInfo.setAuthentication(concreteAuthListener.getAuthentication());
        alarmUserInfo.setPhoneNum(concreteAuthListener.getPhoneNum());//如果使用短信登录时，则不为null
        hcAlarmApp.setUserInfo(alarmUserInfo);
        hcAlarmApp.saveUserInfo(alarmUserInfo);
        signView.move();
    }

    @Override
    public void onFailure() {
        signView.showToast(concreteAuthListener.getErrorMsg());
    }
}
