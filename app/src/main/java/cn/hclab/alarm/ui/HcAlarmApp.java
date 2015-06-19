package cn.hclab.alarm.ui;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cc.trity.common.Common;
import cc.trity.model.entities.AlarmMsg;
import cc.trity.model.entities.AlarmUserInfo;
import cn.hclab.alarm.utils.GsonTools;

/*
 * 进行共享数据，已经相应广告的sdk的初始化
 * 使用Application来存储数据很危险，必须对取到的数据进行判null
 */
public class HcAlarmApp extends Application {
	private List<AlarmMsg> alarmList;
	private SharedPreferences mSharedPreferences;
	private PendingIntent mPendingIntent;
	private boolean isBell = false;
	private boolean isAdv = false;
	private Calendar mCalendar;
	private AlarmUserInfo userInfo;
	public boolean isFirst=true;
	boolean isTestModel = false;// 是否进行试调

	@Override
	public void onCreate() {
		super.onCreate();

		// 从存储sharepreference里面得到alarmlist列表
		mSharedPreferences = getSharedPreferences(Common.CONFIG,
				Context.MODE_PRIVATE);
		String alarmListContent = mSharedPreferences.getString(
				Common.KEY_FLARM_LIST, null);
		if (alarmListContent != null) {
			alarmList = GsonTools.getAlarmInfoList(alarmListContent);
			for (int i = 0; i < alarmList.size(); i++) {
				AlarmMsg mAlarmInfo = alarmList.get(i);
				Log.i("item", mAlarmInfo.getWeek() + mAlarmInfo.getLabel() + mAlarmInfo.getTime()
						+ mAlarmInfo.isOpen());
			}
		} else {
			alarmList = new ArrayList<AlarmMsg>();
		}
		//获取个人信息
		String userInfoContent=mSharedPreferences.getString(
				Common.KEY_USERINFO, null);
		if(userInfoContent!=null){
			userInfo=GsonTools.getClass(userInfoContent,AlarmUserInfo.class);
		}
		// 获取广告，看是否进行显示
		/*String strAdv = mSharedPreferences.getString(Common.KEY_ADV, null);
		if (strAdv != null){// 说明已经登录过，已经记过时间了
			dayAgoAppear(strAdv);
			Log.e("secondLogined",strAdv);
		}
		else {
			// 获取当前的时间
			Calendar calendar = Calendar.getInstance();
			// 加上一天
			calendar.setTimeInMillis(calendar.getTimeInMillis() + 24 * 60 * 60
					* 1000);
			saveDayAgoTime(calendar.getTimeInMillis()+"");
			Log.e("firstLogined",calendar.getTimeInMillis()+"");
		}
		//获取是否初次打开引用
		isFirst=mSharedPreferences.getBoolean(Common.LAUNCHER_COUNT, true);*/
	}

	/*
	 * 此方法用于将广告在一天后，才进行展示
	 */
	public void dayAgoAppear(String strAdv) {
		Calendar calendar = Calendar.getInstance();// 获取当前的时间
		//大于之前的天数就进行展示
		Log.e("currentCalendar", calendar.getTimeInMillis()+"");
//		if (calendar.getTimeInMillis() > Long.parseLong(strAdv)){
			// 初始化广告
//			AdManager.getInstance(getApplicationContext()).init(Common.YOU_MI_APP_ID,
//					Common.YOU_MI_APP_SECRET, isTestModel);
//			SpotManager.getInstance(this).loadSplashSpotAds();
//			setAdv(true);
			setmCalendar(calendar);
//			Log.e("init_adv", "init_adv");
//		}
			

	}
	/*
	 * 第一次登陆的时候，保存时间
	 */
	public void saveDayAgoTime(String strTime) {
		Editor editor = getSharedPreferences(Common.CONFIG,
				Context.MODE_PRIVATE).edit();
		editor.putString(Common.KEY_ADV, strTime);
		editor.commit();
	}

	public List<AlarmMsg> getAlarmList() {
		return alarmList;
	}

	public void setAlarmList(List<AlarmMsg> alarmList) {
		this.alarmList = alarmList;
	}

	public PendingIntent getmPendingIntent() {
		return mPendingIntent;
	}

	public void setmPendingIntent(PendingIntent mPendingIntent) {
		this.mPendingIntent = mPendingIntent;
	}

	public boolean isBell() {
		return isBell;
	}

	public void setBell(boolean isBell) {
		this.isBell = isBell;
	}


	public void addAlarmListItem(AlarmMsg alarmInfo) {
		alarmList.add(alarmInfo);
	}

	public void removeAlarmListItem(int position) {
		alarmList.remove(position);
	}

	public AlarmMsg getAlarmListItem(int position) {
		return alarmList.get(position);
	}

	/*
	 * 保存闹钟列表到sharepreference
	 */
	public void saveAlarmList(List<AlarmMsg> dataAlarmList) {
		String result = GsonTools.createGsonString(dataAlarmList);
		Editor editor = getSharedPreferences(Common.CONFIG,
				Context.MODE_PRIVATE).edit();
		editor.putString(Common.KEY_FLARM_LIST, result);
		editor.commit();// 记得进行提交，否则没有存储的效果
	}

	/*
	 * 保存个人的所有数据
	 */
	public void saveUserInfo(AlarmUserInfo userInfo) {
		if(userInfo!=null){
			String result = GsonTools.createGsonString(userInfo);
			Editor editor = getSharedPreferences(Common.CONFIG,
					Context.MODE_PRIVATE).edit();
			editor.putString(Common.KEY_USERINFO, result);
			editor.commit();// 记得进行提交，否则没有存储的效果
		}else {
			Editor editor = getSharedPreferences(Common.CONFIG,
					Context.MODE_PRIVATE).edit();
			editor.putString(Common.KEY_USERINFO, null);
			editor.commit();
		}
	}

	public AlarmUserInfo getUserInfo() {//已经保证总是有实例化的类
		return userInfo;
	}

	public void setUserInfo(AlarmUserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public boolean isAdv() {
		return isAdv;
	}

	public void setAdv(boolean isAdv) {
		this.isAdv = isAdv;
	}

	public Calendar getmCalendar() {
		return mCalendar;
	}

	public void setmCalendar(Calendar mCalendar) {
		this.mCalendar = mCalendar;
	}

}
