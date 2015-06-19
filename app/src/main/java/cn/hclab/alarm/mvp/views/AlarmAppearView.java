package cn.hclab.alarm.mvp.views;

import android.content.Intent;

/**
 * Created by TryIT on 2015/6/16.
 */
public interface AlarmAppearView extends BaseView{
    void setCurrentTime(String currentTime);
    void createAlarm(long timesMillis,int requestCode,Intent alarmIntent);
    void stopDevice();//关闭设备，如震动
    void updateSuccessView(int alertValue);
    void updateFailureView(int alertValue);
}
