package cn.hclab.alarm.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import cn.hclab.alarm.R;
import cn.hclab.alarm.ui.HcAlarmApp;
import cn.hclab.alarm.ui.activity.base.AppBaseActivity;

public class SplashActivity extends AppBaseActivity {
	private static final long SPEND_TIME = 2000;
	private View view;
	private AlphaAnimation start_anima;
	private HcAlarmApp hcAlarmApp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.init(savedInstanceState);
	}

	@Override
	public void initVariables() {

	}

	@Override
	public void initView(Bundle savedInstanceState) {
		view = View.inflate(this, R.layout.activity_splash, null);
		setContentView(view);//显示布局
		hcAlarmApp=(HcAlarmApp) getApplication();
	}

	@Override
	public void loadData() {
		start_anima = new AlphaAnimation(0.1f, 1.0f); // 淡入浅出的效果，过度
		start_anima.setDuration(SPEND_TIME); // 用的时间
		view.startAnimation(start_anima);
		start_anima.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				redirectTo();
			}
		});
	}

	/*
	 * 进行跳转
	 */
	private void redirectTo() {
		startActivity(new Intent(SplashActivity.this,
				ViewPageMainActivity.class));
		finish();
	}

}
