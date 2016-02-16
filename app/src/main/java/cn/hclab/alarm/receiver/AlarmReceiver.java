package cn.hclab.alarm.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cc.trity.common.Common;
import cn.hclab.alarm.ui.activity.AlarmShakePhoneActivity;

public class AlarmReceiver extends BroadcastReceiver {

	/*
	 * 此处的intent，能够获取随广播而来的intent中的数据，捕获广播信息
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Common.ALARM_MSG)) {
			Log.e("AlarmIdReceive",intent.getIntExtra(Common.ALARM_ID,-1)+"");
			// 要執行的工作
			// 监听闹钟发出执行的广播
			AlarmManager am = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			Log.e("getResultCode",getResultCode()+"");
			am.cancel(PendingIntent.getBroadcast(context, getResultCode(),
					new Intent(context, AlarmReceiver.class),
					PendingIntent.FLAG_CANCEL_CURRENT));// 取消当前所执行的闹钟
			intent.setClass(context, AlarmShakePhoneActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}
}
