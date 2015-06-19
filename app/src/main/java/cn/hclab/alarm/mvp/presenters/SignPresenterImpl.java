package cn.hclab.alarm.mvp.presenters;

import android.app.Activity;
import android.content.Context;

import cc.trity.common.Common;
import cn.hclab.alarm.mvp.model.SignByTencentModel;
import cn.hclab.alarm.mvp.model.SignByWeiBoModel;
import cn.hclab.alarm.mvp.model.SignModel;
import cn.hclab.alarm.mvp.views.SignView;
import cn.hclab.alarm.ui.HcAlarmApp;

/**
 * Created by TryIT on 2015/6/14.
 */
public class SignPresenterImpl implements SignPresenter{
    private SignView signView;
    private Activity mActivity;
    private SignModel signModel;
    private HcAlarmApp hcAlarmApp;
    private Context context;
    public SignPresenterImpl(SignView signView,Activity mActivity, HcAlarmApp hcAlarmApp){
        this.signView=signView;
        this.mActivity=mActivity;
        this.hcAlarmApp=hcAlarmApp;

    }
    public SignPresenterImpl(SignView signView,Activity mActivity, HcAlarmApp hcAlarmApp,Context context){
        this(signView,mActivity,hcAlarmApp);
        this.context=context;
    }

    /**
     *
     * @param flag
     * 0 代表普通的手机登录，与自己的服务器进行交互 1 代表腾讯登录
     * 2 代表微博登录 3 代表使用微博手机注册登录
     */
    @Override
    public Object login(int flag){
        switch (flag){
            case 0:

                break;
            case 1:
                signModel=new SignByTencentModel(mActivity,hcAlarmApp,signView);
                return signModel.signOperation(Common.TENCENT_GET_PERMISSION);

            case 2:
                signModel=new SignByWeiBoModel(mActivity,hcAlarmApp,signView);
                return signModel.signOperation(Common.WEIBO_TAG_ALL);

            case 3:
                signModel=new SignByWeiBoModel(mActivity,hcAlarmApp,signView);
                return signModel.signOperation(Common.WEIBO_TAG_PHONE);

        }
        return null;
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFault() {

    }

}
