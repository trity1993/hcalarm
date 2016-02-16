package cn.hclab.alarm.ui.fragment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.getbase.floatingactionbutton.AddFloatingActionButton;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import cc.trity.common.Common;
import cc.trity.model.entities.AlarmMsg;
import cn.hclab.alarm.R;
import cn.hclab.alarm.receiver.AlarmReceiver;
import cn.hclab.alarm.ui.HcAlarmApp;
import cn.hclab.alarm.ui.activity.AlarmSettingActivity;
import cn.hclab.alarm.ui.adapter.AlarmListAdapter;
import cn.hclab.alarm.utils.Tools;

public class MainFragment extends Fragment {
	private final int UPDATE_VIEW = 0;

	@InjectView(R.id.btn_alarm_create) Button btnAlarmCreate;
	@InjectViews({R.id.layout_listview,R.id.layout_floatbtn_create}) List<ViewStub> viewStubs;
	@InjectView(R.id.rlayout_bottom)RelativeLayout rlayout;

	private ListView lvAlarmListView;
	private AddFloatingActionButton afaAlarmCreate;

	private HcAlarmApp hcAlarmApp;
	private AlarmListAdapter ala;

	private Activity mActivity;
	private boolean isFirstAppear;
	private View rootView;
	private View[] viewLayout=new View[2];
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e(MainFragment.this.getTag(),"onCreateView");
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_main, null);
		ButterKnife.inject(this, rootView);

		// 通过Application来获得闹钟列个表的信息
		hcAlarmApp = (HcAlarmApp) mActivity.getApplication();
		mHandler.sendEmptyMessage(UPDATE_VIEW);
		return rootView;
	}

	/**
	 * 加载layout里面的布局，无法使用ButterKnife进行注解
	 */
	public void initView(){
		lvAlarmListView=(ListView)rootView.findViewById(R.id.lvAlarmList);
		lvAlarmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//单按的时候

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Tools.ToastUtils(mActivity.getApplicationContext(), "单击" + position);
				//跳转到设置页面
				AlarmMsg alarmMsg=(AlarmMsg)ala.getItem(position);
				Intent intent=new Intent(mActivity,AlarmSettingActivity.class);
				intent.putExtra(Common.ALARM_MSG,alarmMsg);
				intent.putExtra(Common.ALARM_POSITION,position);
				startActivityForResult(intent, UPDATE_VIEW);
			}
		});
		lvAlarmListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {//长按的删除

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				dialog(position);
				return true;
			}
		});
		afaAlarmCreate=(AddFloatingActionButton)rootView.findViewById(R.id.afab_alarm_create);
		afaAlarmCreate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(mActivity.getApplicationContext(),
						AlarmSettingActivity.class),UPDATE_VIEW);//待会改成回调
			}
		});
	}
	@OnClick({R.id.btn_alarm_create})
	public void onClick(View view){
		switch (view.getId()){
			case R.id.btn_alarm_create:
				startActivityForResult(new Intent(mActivity.getApplicationContext(),
						AlarmSettingActivity.class),UPDATE_VIEW);//待会改成回调
				break;
		}
	}

	/**
	 * 显示listView和floatingactionbutton
	 */
	public void appearViewLayout(){
		if(isFirstAppear){
			for(View v:viewLayout){
				v.setVisibility(View.VISIBLE);
			}

		}else{
			int top=0;
			for(ViewStub v:viewStubs){
				viewLayout[top++]=v.inflate();
			}
			isFirstAppear=true;
			initView();
		}
		rlayout.setVisibility(View.GONE);
		btnAlarmCreate.setVisibility(View.GONE);
	}

	/**
	 * 隐藏listView和floatingactionbutton
	 */
	public void disappearViewLayout(){
		for(View v:viewLayout){
			if(v==null)
				return;
			v.setVisibility(View.GONE);
		}
		rlayout.setVisibility(View.VISIBLE);
		btnAlarmCreate.setVisibility(View.VISIBLE);
	}

	/*
	 * 长按进行删除
	 */
	private void dialog(final int position) {
		new AlertDialog.Builder(mActivity)
				.setTitle("操作选项")
				.setItems(new CharSequence[] { "删除" },
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:

									deleteAlarmListItem(position);
									break;

								default:
									break;
								}
							}
						}).setNegativeButton("取消", null).show();
	}

	/**
	 * 删除闹钟
	 * @param position
	 */
	public void deleteAlarmListItem(int position) {
		// 得到AlarmManager用于闹钟的删除和取消。
		AlarmManager mAlarmManager = (AlarmManager) mActivity
				.getSystemService(Context.ALARM_SERVICE);
		// 取消此闹钟
		Intent intent=new Intent(Common.ALARM_MSG);//实质是setAction的操作
		intent.setClass(mActivity.getApplicationContext(),
				AlarmReceiver.class);
		intent.setData(Uri.parse("content://calendar/calendar_alerts/1"));
		mAlarmManager.cancel(PendingIntent.getBroadcast(mActivity
				.getApplicationContext(), hcAlarmApp.getAlarmListItem(position)
				.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT));

		hcAlarmApp.removeAlarmListItem(position);

		mHandler.sendEmptyMessage(UPDATE_VIEW);
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATE_VIEW:
				if(hcAlarmApp.getAlarmList().size()!=0){
					appearViewLayout();
				}else{
					disappearViewLayout();
				}
				if(ala==null)//解决第一次创建的时候，没有子项问题，原来是子项还有加进去，适配器为null
					ala=new AlarmListAdapter(mActivity, hcAlarmApp.getAlarmList());
				if(lvAlarmListView!=null){
					lvAlarmListView.setAdapter(ala);
				}

				break;
			default:
				break;
			}

		}
	};
	@Override
	public void onPause() {
		super.onPause();
		Log.e(MainFragment.this.getTag(),"onPause");
		hcAlarmApp.saveAlarmList(hcAlarmApp.getAlarmList());
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.e(MainFragment.this.getTag(), "onStop");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.e(MainFragment.this.getTag(), "onDestroyView");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e(MainFragment.this.getTag(), "onDestroy");
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		hcAlarmApp.saveAlarmList(hcAlarmApp.getAlarmList());
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode!=mActivity.RESULT_OK){
			return;
		}
		if(requestCode==UPDATE_VIEW){
			mHandler.sendEmptyMessage(UPDATE_VIEW);
		}
	}
}
