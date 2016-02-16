package cn.hclab.alarm.ui.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectViews;
import cc.trity.common.Common;
import cc.trity.model.entities.AlarmMsg;
import cn.hclab.alarm.R;
import cn.hclab.alarm.api.OnEditTextListener;
import cn.hclab.alarm.mvp.presenters.AlarmSetPresenter;
import cn.hclab.alarm.mvp.views.AlarmSetView;
import cn.hclab.alarm.receiver.AlarmReceiver;
import cn.hclab.alarm.ui.HcAlarmApp;
import cn.hclab.alarm.ui.activity.base.AppBaseActivity;
import cn.hclab.alarm.ui.dialog.EditTagDialog;
import cn.hclab.alarm.ui.view.PickerView;
import cn.hclab.alarm.ui.view.PickerView.onSelectListener;
import cn.hclab.alarm.utils.Tools;

public class AlarmSettingActivity extends AppBaseActivity implements
		OnClickListener, OnEditTextListener,AlarmSetView {
    private Toolbar toolBar;
	private Button btnPick;
	private TextView tvTag;
	private RelativeLayout rlSetTag, rlSetBell;
	private HcAlarmApp hcAlarmApp;
	private AlarmManager managerAlarm;
	private EditTagDialog mEditDialog;
	private SeekBar sbSound;
	private AudioManager audioManager;
	private int alarmListPosition=-1;
	@InjectViews({R.id.cb_mon,R.id.cb_tues,R.id.cb_wed,R.id.cb_thurs,R.id.cb_fri,R.id.cb_satur,R.id.cb_sun})
	List<CheckBox> weekList;
	private PickerView hour, min;

	List<String> hours = new ArrayList<String>(); // 小时的初始化集合
	List<String> mins = new ArrayList<String>();// 分钟的初始化集合
	AlarmSetPresenter alarmSetPresenter;
	private boolean isUpdate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_setting);
		ButterKnife.inject(this);
		this.init(savedInstanceState);
	}

	@Override
	public void initVariables() {
		// 得到Application的信息
		hcAlarmApp = (HcAlarmApp) getApplication();
		// 得到闹钟配置
		managerAlarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);//设置铃声
		//判断是否进行修改的闹钟，而不是创建闹钟
		Intent intentUpdate=getIntent();
		if(intentUpdate!=null){
			AlarmMsg alarmMsg=(AlarmMsg)intentUpdate.getSerializableExtra(Common.ALARM_MSG);
			if(alarmMsg!=null){
				alarmListPosition=intentUpdate.getIntExtra(Common.ALARM_POSITION,-1);
				viewSetting(alarmMsg);
				isUpdate=true;
				alarmSetPresenter=new AlarmSetPresenter(this,hcAlarmApp,alarmListPosition);
				return ;
			}
		}
	}

	@Override
	public void initView(Bundle savedInstanceState) {
		// 设置actionbar
		toolBar=(Toolbar)findViewById(R.id.include_toolbar);
		if (toolBar != null){
			trySetupToolbar(toolBar);
		}
		// 设置标签
		rlSetTag = (RelativeLayout) findViewById(R.id.rl_set_tag);
		// 设置铃声
		rlSetBell = (RelativeLayout) findViewById(R.id.rl_set_bell);

		hour = (PickerView) findViewById(R.id.hour);

		min = (PickerView) findViewById(R.id.mins);

		for (int i = 0; i < 24; i++) {
			hours.add(i < 10 ? "0" + i : "" + i);
		}
		for (int i = 0; i < 60; i++) {
			mins.add(i < 10 ? "0" + i : "" + i);
		}
		hour.setData(hours);
		hour.setOnSelectListener(new onSelectListener() {

			@Override
			public void onSelect(String text) {
				// Tools.ToastUtils(AlarmSettingActivity.this, text+"时");
			}
		});
		min.setData(mins);
		min.setOnSelectListener(new onSelectListener() {

			@Override
			public void onSelect(String text) {
				// Tools.ToastUtils(AlarmSettingActivity.this, text+"时");
			}
		});

		// 得到闹钟的其他选项
		tvTag = (TextView) findViewById(R.id.tv_tag_edited);
		btnPick = (Button) findViewById(R.id.btn_pick);
		sbSound = (SeekBar) findViewById(R.id.sb_sound);

		sbSound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar arg0, int progress,
										  boolean fromUser) {
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
						progress, 0);//设置闹铃的声音大小
			}
		});

		btnPick.setOnClickListener(this);
		rlSetTag.setOnClickListener(this);
		rlSetBell.setOnClickListener(this);
	}

	@Override
	public void loadData() {
		// 设置当前时间
		Calendar c = Calendar.getInstance();
		hour.setmCurrentSelected(c.get(Calendar.HOUR_OF_DAY));
		min.setmCurrentSelected(c.get(Calendar.MINUTE));

		alarmSetPresenter=new AlarmSetPresenter(this,hcAlarmApp);
	}

	/**
	 * 根据alarmMsg的信息，尽心对view的更新
	 * @param alarmMsg
	 */
	public void viewSetting(AlarmMsg alarmMsg){
		//设置标签
		tvTag.setText(alarmMsg.getLabel());
		//设置显示的时间
		long time=alarmMsg.getTimes();
		Calendar calendar=Calendar.getInstance();
		calendar.setTimeInMillis(time);
		hour.setmCurrentSelected(calendar.get(Calendar.HOUR_OF_DAY));
		min.setmCurrentSelected(calendar.get(Calendar.MINUTE));
		//设置星期几
		int [] weeks=alarmMsg.getWeeks();
		for(int i=1;i<weeks.length;i++){
			if(weeks[i]==1){
				weekList.get(i-1).setChecked(true);
			}
		}
	}

	/**
	 * 设置选择的星期,对勾选的日期进行处理
	 */
	public void alarmWeekSet(AlarmMsg alarmInfo) {
		int[] weeks=new int[8];
		String[] strDate=new String[]{"一","二","三","四","五","六","日"};
		StringBuffer sBuffer = new StringBuffer("星期");
		boolean isEveryDay=true;
		int top=1;
		for(CheckBox checkBox:weekList){
			if(checkBox.isChecked()){
				weeks[top]=1;
				sBuffer.append(strDate[top-1]);
				if(top==7){
					weeks[0]=1;
				}else
					sBuffer.append(",");
			}
			else{
				isEveryDay=false;
			}
			top++;
		}
		if(isEveryDay){
			alarmInfo.setWeek("每天");
		}else if(sBuffer.length()==2){//在初始化阶段，仅有“星期”，表示一次的状态
			alarmInfo.setWeek("一次");
		}else{
			alarmInfo.setWeek(sBuffer.toString());
		}
		alarmInfo.setWeeks(weeks);

		/*if (mon.isChecked() && tues.isChecked() && wed.isChecked()
				&& thurs.isChecked() && fri.isChecked() && satur.isChecked()
				&& sun.isChecked()){
			for(int i=0;i<=7;i++){
				weeks[i]=1;//全部赋值为1，标识已经进行设置了
			}
			alarmInfo.setWeeks(weeks);
			sBuffer.append("每天");
			alarmInfo.setWeek(sBuffer.toString());
			
			return ;
		}
		if(mon.isChecked() || tues.isChecked() || wed.isChecked()
				|| thurs.isChecked() || fri.isChecked() || satur.isChecked()
				|| sun.isChecked()){
			//依次某个星期的
			if (mon.isChecked()){
				weeks[1]=1;
				sBuffer.append("星期一,");
			}
			if (tues.isChecked()){
				weeks[2]=1;
				sBuffer.append("星期二,");
			}
			if (wed.isChecked()){
				weeks[3]=1;
				sBuffer.append("星期三,");
			}
			if (thurs.isChecked()){
				weeks[4]=1;
				sBuffer.append("星期四,");
			}
			if (fri.isChecked()){
				weeks[5]=1;
				sBuffer.append("星期五,");
			}
			if (satur.isChecked()){
				weeks[6]=1;
				sBuffer.append("星期六,");
			}
			if (sun.isChecked()){
				weeks[7]=1;
				weeks[0]=1;
				sBuffer.append("星期日");
			}
			alarmInfo.setWeeks(weeks);
			alarmInfo.setWeek(sBuffer.toString());
		}
		else{//表示从不重复
			alarmInfo.setWeeks(weeks);
			sBuffer.append("一次");
			alarmInfo.setWeek(sBuffer.toString());
		}*/
		
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_pick:
			alarmSetPresenter.addSystemAlarm();
			break;
		case R.id.rl_set_tag:
			createDialog();
			break;
		case R.id.rl_set_bell:// 添加默认铃声
			getSystemRingtone();
			break;
		}
	}

	/*
	 * 得到默认铃声
	 */
	private static final int Ringtone = 0;

	@SuppressWarnings("deprecation")
	public void getSystemRingtone() {
		// 打开系统铃声设置
		Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_INCLUDE_DRM, true);
		if (hcAlarmApp != null)// 设置为铃声选择为true;
			hcAlarmApp.setBell(true);
		startActivityForResult(intent, Ringtone);
	}

	/**
	 * 设置铃声之后的回调函数
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {//后退，进行取消的操作
			return;
		} else {
			// 得到我们选择的铃声,如果选择的是"静音"，那么将会返回null
			Uri uri = data
					.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
			Log.e("onActivityResult====", "" + uri);
			Toast.makeText(AlarmSettingActivity.this, uri + "", Toast.LENGTH_SHORT).show();
			if (uri != null) {
				switch (requestCode) {
				case Ringtone:
					RingtoneManager.setActualDefaultRingtoneUri(this,
							RingtoneManager.TYPE_RINGTONE, uri);// 为某一铃声类型设置默认铃声,为下一次响铃做准备
					break;
				}
			}
		}
	}

	/*
	 * 创建填写标签的dialog
	 */
	public void createDialog() {
		mEditDialog = new EditTagDialog();
		mEditDialog.show(getFragmentManager(), "EditTagDialog");
	}

	@Override
	public void updateContent(String content) {
		// 更改标签
		tvTag.setText(content);
	}

	@Override
	public void getWeek(AlarmMsg alarmInfo) {
		alarmWeekSet(alarmInfo);
	}

	@Override
	public String getTag() {
		return tvTag.getText().toString().trim();
	}

	@Override
	public String getTime() {
	return hours.get(hour.getmCurrentSelected()) + ":"
				+ mins.get(min.getmCurrentSelected());
	}

	@Override
	public String getHour() {
		return hours.get(hour.getmCurrentSelected());
	}

	@Override
	public String getMinute() {
		return mins.get(min.getmCurrentSelected());
	}

	@Override
	public void createAlarm(long timesMillis,int requestCode,Intent alarmIntent) {
		alarmIntent.setClass(this, AlarmReceiver.class);
		managerAlarm.set(AlarmManager.RTC_WAKEUP,
				timesMillis, PendingIntent.getBroadcast(
						getApplicationContext(), requestCode, alarmIntent,/* 发出指定的广播 */
						PendingIntent.FLAG_CANCEL_CURRENT));
	}

	@Override
	public void move(){
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public void showToast(String msgStr) {
		Tools.ToastUtils(AlarmSettingActivity.this,msgStr);
	}
}
