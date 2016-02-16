package cn.hclab.alarm.ui.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.Html;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import cc.trity.common.Common;
import cn.hclab.alarm.R;
import cn.hclab.alarm.api.AlarmSensorEventListener;
import cn.hclab.alarm.api.MyOnGestureListener;
import cn.hclab.alarm.mvp.presenters.AlarmShakePresenter;
import cn.hclab.alarm.mvp.views.AlarmAppearView;
import cn.hclab.alarm.service.AlarmService;
import cn.hclab.alarm.ui.HcAlarmApp;
import cn.hclab.alarm.ui.activity.base.AppBaseActivity;
import cn.hclab.alarm.utils.Tools;

public class AlarmShakePhoneActivity extends AppBaseActivity implements AlarmAppearView {

	// 感应管理器
	private SensorManager mSensorManager;

	private SensorEventListener sensorListener; // 申明传感器监听事件
	private TextView tvShakeNum, tvCurrentTime;
	private int alertValue = 0;
	private boolean wakeUp = false; // 标志是否醒来
	private Chronometer chronometer;
	private final int START_ALARM_SHAKE = 0;
	private AlarmManager mAlarmManager;
	private ImageView ivBatteryBg, ivLightleftBig, ivLightleftBiger,
			ivLightleftSmall, ivLightleftSmallLast, ivLightRightBig,
			ivLightRightBiger, ivLightRightSmall, ivLightRightSmallLast;
	private Animation animation;
	private GestureDetector mGestureDetector;
	private int requestCodeAlarm;
	private HcAlarmApp mAlarmApp;
	AlarmShakePresenter alarmShakePresenter;

	/** 点击两次退出*/
	private boolean mIsBackKeyClicked;
	// 创建启动service的intent
	Intent intentService = null;
	private Handler mHandle = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case START_ALARM_SHAKE://开始闹钟界面
				startShake();
				break;
			}
		};
	};
	// 保持所启动的service的Ibinder对象
	private AlarmService.MyBinder binder;
	// 定义一个ServiceConnection 对象
	private ServiceConnection conn = new ServiceConnection() {
		// 当Activity和Service断开时进行回调
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			// TODO Auto-generated method stub

		}

		// 当Activity和Service链接成功时进行回调
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			binder = (AlarmService.MyBinder) service;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 当手机休眠时，使Activity进行呈现的准备
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		// 布局
		setContentView(R.layout.shake_phone_prepare);
		//得到hcAPP
		mAlarmApp =(HcAlarmApp)getApplication();
		//得到intent
		Intent intent=getIntent();
		requestCodeAlarm=intent.getIntExtra(Common.ALARM_ID, 0);

		initAlarmAppearView();

		//设置下一个闹钟
		alarmShakePresenter=new AlarmShakePresenter(this,mAlarmApp);
		Calendar currentTime=Calendar.getInstance();
		alarmShakePresenter.operation(currentTime, requestCodeAlarm);

		// 创建启动service的intent
		intentService = new Intent();
		intentService.setAction("cn.hclab.alarm.service.BIND_AUTO_CREATE");
		// 绑定指定的Service
		bindService(intentService, conn, Service.BIND_AUTO_CREATE);

		// 得到闹钟管理
		mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

	}

	private void getWidget() {
		// 判断是否显示广告：
		tvShakeNum = (TextView) findViewById(R.id.shake_sence_value);
		chronometer = (Chronometer) findViewById(R.id.cm_count);
		chronometer.setFormat("%s");
		ivLightleftBig = (ImageView) findViewById(R.id.iv_lighting_left_big);
		ivLightleftSmall = (ImageView) findViewById(R.id.iv_lighting_left_small);
		ivLightRightBig = (ImageView) findViewById(R.id.iv_lighting_right_big);
		ivLightRightSmall = (ImageView) findViewById(R.id.iv_lighting_right_small);
		ivLightRightSmallLast = (ImageView) findViewById(R.id.iv_lighting_right_small_last);
		ivLightleftSmallLast = (ImageView) findViewById(R.id.iv_lighting_left_small_last);
		ivLightRightBiger = (ImageView) findViewById(R.id.iv_lighting_right_biger);
		ivLightleftBiger = (ImageView) findViewById(R.id.iv_lighting_left_biger);
		ivBatteryBg = (ImageView) findViewById(R.id.iv_battery_bg);
		LinearLayout mLayout = (LinearLayout) findViewById(R.id.shake_ll);

		initGesture(mLayout);
		initAnimation();
	}
	/**
	 * 初始化手势
	 */
	public void initGesture(LinearLayout mLayout){
		mGestureDetector = new GestureDetector(this, new MyOnGestureListener());
		mGestureDetector.setOnDoubleTapListener(new OnDoubleTapListener() {

			@Override
			public boolean onSingleTapConfirmed(MotionEvent arg0) {
				return false;
			}

			@Override
			public boolean onDoubleTapEvent(MotionEvent arg0) {
				return false;
			}

			@Override
			public boolean onDoubleTap(MotionEvent arg0) {
				System.exit(0);// 测试时，隐藏键，进行退出
				return false;
			}
		});
		mLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent ev) {
				mGestureDetector.onTouchEvent(ev);//加入手势
				return true;
			}
		});
	}

	/**
	 * 初始化闹钟动画
	 */
	public void initAnimation(){
		// 电池充电效果
		ivBatteryBg.setBackgroundResource(R.drawable.baterry_charge);
		// 获取AnimationDrawable对象
		AnimationDrawable animationDrawable = (AnimationDrawable) ivBatteryBg
				.getBackground();
		// 开始或者继续动画播放
		animationDrawable.start();
		// 闪电效果
		animation = AnimationUtils.loadAnimation(this, R.anim.lighting);
		ivLightleftBiger.startAnimation(animation);
		ivLightRightBiger.startAnimation(animation);
		ivLightleftSmallLast.startAnimation(animation);
		ivLightleftSmallLast.startAnimation(animation);
		ivLightleftBig.startAnimation(animation);
		ivLightleftSmall.startAnimation(animation);
		ivLightRightBig.startAnimation(animation);
		ivLightRightSmall.startAnimation(animation);
		// 电池摇晃的效果
		animation = AnimationUtils.loadAnimation(this, R.anim.shake);
		ivBatteryBg.setAnimation(animation);
	}



	public void initAlarmAppearView(){
		tvCurrentTime = (TextView) findViewById(R.id.tv_current_alram_time);
		ImageButton ibStartShake = (ImageButton) findViewById(R.id.ib_start_shake);
		ibStartShake.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mHandle.sendEmptyMessage(0);
			}
		});
	}

	/*
	 * 开始摇晃的界面，以及相关事件
	 */
	public void startShake() {
		setContentView(R.layout.shake_phone);
		getWidget();

		// 设置开始计时的时间
		chronometer.setBase(SystemClock.elapsedRealtime());
		chronometer.start();
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		// 2 判断当前手机是否带加速度感应器，如果不带，直接结束，不启动服务
		List<Sensor> sensors = mSensorManager
				.getSensorList(Sensor.TYPE_ACCELEROMETER);
		if (sensors != null)
			if (sensors.size() == 0)
				return;

		// 3生成感应侦听事件
		sensorListener = new AlarmSensorEventListener(this,Common.SHAKE_SENSE_VALUE_THREE);

		// 4注册侦听事件
		mSensorManager.registerListener(sensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	/**
	 * 停止服务，并释放设备
	 */
	@Override
	public void stopDevice(){
		// 把service关闭并解绑service
		stopService(intentService);
		unbindService(conn);
		mSensorManager.unregisterListener(sensorListener);// 释放设备
	}

	/**
	 * 清醒度足够时，进行更新
	 */
	@Override
	public void updateSuccessView(int alertValue){
		tvShakeNum.setText(String.format("次数:%d",alertValue));
//		new PromptDialog().show(getFragmentManager(),
//				"morningTag");
		chronometer.stop();
		chronometer.setVisibility(View.VISIBLE);// 显示耗时
		wakeUp = true;
	}
	/**
	 * 清醒度不足够时，进行更新
	 */
	@Override
	public void updateFailureView(int alertValue){
		CharSequence senceValue = Html.fromHtml("<big>次数为:"
				+ alertValue + "</big>");
		tvShakeNum.setText(senceValue);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (!wakeUp) {// 此判断，在没有完成摇晃的情况下，被按home，或者休眠，使其重新启动进行摇晃
			Calendar calendar = Calendar.getInstance();
			int requestCode = (int) calendar.getTimeInMillis() / 1000 / 60;
			createAlarm(calendar.getTimeInMillis(), requestCode, new Intent(Common.ALARM_MSG));
			// 释放设备当前设备
			finish();
		}

	}

	@Override
	public void initVariables() {

	}

	@Override
	public void initView(Bundle savedInstanceState) {

	}

	@Override
	public void loadData() {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (alertValue > Common.SHAKE_NUMBER)
				pressAgainExit();
			else
				Tools.ToastUtils(this, R.string.unable_pressBack);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void pressAgainExit() {
		if (mIsBackKeyClicked) {
			finish();
			System.exit(0);
		} else {
			Toast.makeText(this, R.string.press_back_again, Toast.LENGTH_SHORT)
					.show();
			mIsBackKeyClicked = true;
			runOnUiThreadDelay(new Runnable() {

				@Override
				public void run() {
					mIsBackKeyClicked = false;
				}
			}, Common.DOUBLE_CLICK_INTERVAL);
		}
	}

	@Override
	public void onBackPressed() {
	}

	@Override
	public void setCurrentTime(String currentTime) {
		tvCurrentTime.setText(currentTime);
	}

	@Override
	public void createAlarm(long timesMillis, int requestCode, Intent alarmIntent) {
		mAlarmManager.set(AlarmManager.RTC_WAKEUP, timesMillis, PendingIntent.getBroadcast(
				getApplicationContext(), requestCode,alarmIntent,
				PendingIntent.FLAG_CANCEL_CURRENT));
	}

	@Override
	public void move() {
		finish();
	}

	@Override
	public void showToast(String msgStr) {
		Tools.ToastUtils(AlarmShakePhoneActivity.this,msgStr);
	}
}
