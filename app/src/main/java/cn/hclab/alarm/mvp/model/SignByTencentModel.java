package cn.hclab.alarm.mvp.model;

import android.app.Activity;
import android.os.SystemClock;
import android.util.Log;

import com.tencent.api.BaseUiListener;
import com.tencent.tauth.Tencent;

import cc.trity.common.Common;
import cc.trity.model.entities.AlarmUserInfo;
import cn.hclab.alarm.mvp.views.SignView;
import cn.hclab.alarm.ui.HcAlarmApp;
import cn.hclab.alarm.utils.Tools;

/**
 * Created by TryIT on 2015/6/14.
 */
public class SignByTencentModel implements SignModel<Tencent> {
    //腾讯接口
    private Tencent mTencent;
    private Activity mActivity;
    private BaseUiListener loginListener;
    private HcAlarmApp hcAlarmApp;
    private SignView signView;
    public SignByTencentModel(Activity mActivity,HcAlarmApp hcAlarmApp,SignView signView){
        this.mActivity=mActivity;
        this.hcAlarmApp=hcAlarmApp;
        this.signView=signView;
        mTencent=Tencent.createInstance(Common.TENCENT_APP_ID,mActivity);
    }

    /**
     *
     * @param tag 此处tag，用来标记所要获得的权限。
     * @return
     */
    @Override
    public Tencent signOperation(String tag) {
        if (!mTencent.isSessionValid()) {
            loginListener=new BaseUiListener(mActivity,this);
            mTencent.login(mActivity, tag, loginListener);
            Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
        }else{//说明已经登录过了,先注销，再进行一次登录
            mTencent.logout(mActivity);
            signOperation(tag);
        }
        return mTencent;
    }

    @Override
    public void onSuccess() {
        AlarmUserInfo alarmUserInfo=new AlarmUserInfo();
        alarmUserInfo.setAuthentication(Tools.initOpenidAndToken(mTencent, loginListener.getResponseMsg()));
        hcAlarmApp.setUserInfo(alarmUserInfo);
        hcAlarmApp.saveUserInfo(alarmUserInfo);
        signView.move();
    }

    @Override
    public void onFailure() {
        signView.showToast(loginListener.getErrorMsg());
    }
}
