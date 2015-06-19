package cn.hclab.alarm.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sina.weibo.sdk.auth.sso.SsoHandler;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import cc.trity.common.Common;
import cn.hclab.alarm.R;
import cn.hclab.alarm.mvp.presenters.SignPresenter;
import cn.hclab.alarm.mvp.presenters.SignPresenterImpl;
import cn.hclab.alarm.mvp.views.SignView;
import cn.hclab.alarm.ui.HcAlarmApp;
import cn.hclab.alarm.ui.activity.base.BaseActivity;
import cn.hclab.alarm.utils.Tools;

public class SignActivity extends BaseActivity implements SignView {

    @InjectView(R.id.include_toolbar) Toolbar toolbar;
    //微博，腾讯
    @InjectViews({R.id.sign_btn_login_weibo,R.id.sign_btn_login_tencent}) List<Button> btnThirdLogin;
    //注册，登录
    @InjectViews({R.id.sign_btn_reg,R.id.sign_btn_login}) List<Button> btnNormalSign;
    //账号，密码
    @InjectViews({R.id.sign_et_phone,R.id.sign_et_pwd}) List<EditText> editTextSign;

	// Application来共享数据
	private HcAlarmApp hcAlarmApp;

    SignPresenter signPresenter;

    //第三方的回调
    private SsoHandler mSsoHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign);// 加载布局
        ButterKnife.inject(this);
		// 得到共享数据
		hcAlarmApp = (HcAlarmApp) getApplication();

        trySetupToolbar(toolbar);

        signPresenter=new SignPresenterImpl(this,SignActivity.this,hcAlarmApp);
	}

	@OnClick({R.id.sign_btn_login_weibo,R.id.sign_btn_login_tencent,R.id.sign_btn_reg,R.id.sign_btn_login})
	public void onClick(View view) {
		switch (view.getId()) {
		    case R.id.sign_btn_reg:
                //点击注册的时候，使用微博第三方进行登录
                mSsoHandler= (SsoHandler)signPresenter.login(Common.WEIBO_MSM_LOGIN);
			    break;
		    case R.id.sign_btn_login:
               //进行登录
                break;
            case R.id.sign_btn_login_tencent:
                signPresenter.login(Common.TENCENT_LOGIN);
                break;
            case R.id.sign_btn_login_weibo:

                mSsoHandler= (SsoHandler)signPresenter.login(Common.WEIBO_LOGIN);
                break;
            default:
                break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
        //除了网页，使用客户端，短信，都需要此回调。
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

    }

    @Override
    public String getPhoneNum() {
        return editTextSign.get(0).getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return editTextSign.get(1).getText().toString().trim();
    }

    @Override
    public void move() {
        finish();
    }

    @Override
    public void showToast(String msgStr) {
        Tools.ToastUtils(SignActivity.this,msgStr);
    }

}
