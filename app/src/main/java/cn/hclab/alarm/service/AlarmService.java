package cn.hclab.alarm.service;

import java.io.IOException;

import cn.hclab.alarm.ui.HcAlarmApp;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.Vibrator;

public class AlarmService extends Service{
	private MediaPlayer mMediaPlayer;
	private HcAlarmApp hcAlarmApp;
	// 震动器
	private Vibrator vibrator;

	//定义onBinder方法所返回的对象
	private MyBinder binder=new MyBinder();
	//通过继承binder来实现IBinder类
	public class MyBinder extends Binder{//通过Binder实现Activity和service之间的通信
		public void startPlayMusic(){
			playAlarm();
		}
	}
	@Override
	public IBinder onBind(Intent arg0) {
	
		return binder;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		hcAlarmApp = (HcAlarmApp) getApplication();
		alarmStyle = hcAlarmApp.isBell();// 赋值变成是铃声还是不是铃声
		vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);// 获取震动
		// 开始响铃或者震动
		if (getAlarmStyle()) {
			playAlarm();
		} else {
			startVibrate();
		}
	}
	/*
	 * 响铃提示方式
	 */
	private static boolean alarmStyle = false; // 闹钟提示方式 (true:铃声;false:振动)

	public static void setAlarmStyle(boolean style) {
		alarmStyle = style;
	}

	public static boolean getAlarmStyle() {
		return alarmStyle;
	}
	/*
	 * 播放铃声
	 */
	private void playAlarm() {
		mMediaPlayer = new MediaPlayer();

		Uri alert = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

		try {
			mMediaPlayer.setDataSource(this, alert);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
		mMediaPlayer.setLooping(true); // 循环播放开
		try {
			mMediaPlayer.prepare(); // 后面的是try 和catch ，自动添加的
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mMediaPlayer.start();// 开始播放
	}
	
	/*
	 * 开启震动方式
	 */
	private void startVibrate() {
		long[] vib = { 0, 200, 3000, 500, 2000, 1000 };
		vibrator.vibrate(vib, 4);
	}
	//service 被断开连接回调方法
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}
	//service 被关闭之前回调
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if(vibrator!=null)
			vibrator.cancel();
		if(mMediaPlayer!=null)
			mMediaPlayer.stop();
		super.onDestroy();
	}
}
