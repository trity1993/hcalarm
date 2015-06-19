package cn.hclab.alarm.mvp.views;

import android.content.Intent;

import cc.trity.model.entities.AlarmMsg;

/**
 * Created by TryIT on 2015/6/16.
 */
public interface AlarmSetView extends BaseView {
    void getWeek(AlarmMsg alarmInfo);
    String getTag();
    String getTime();
    String getHour();
    String getMinute();
    void createAlarm(long timesMillis,int requestCode,Intent alarmIntent);
}
