package cn.hclab.alarm.mvp.presenters;

import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

import cc.trity.common.Common;
import cc.trity.model.entities.AlarmMsg;
import cn.hclab.alarm.mvp.views.AlarmAppearView;
import cn.hclab.alarm.ui.HcAlarmApp;
import cn.hclab.alarm.utils.Tools;

/**
 * Created by TryIT on 2015/6/16.
 */
public class AlarmShakePresenter {
    private AlarmAppearView alarmAppearView;
    private HcAlarmApp mAlarmApp;
    public AlarmShakePresenter(AlarmAppearView alarmAppearView,HcAlarmApp mAlarmApp){
        this.alarmAppearView=alarmAppearView;
        this.mAlarmApp=mAlarmApp;
    }
    public void operation(Calendar currentTime,int requestCodeAlarm){
        alarmAppearView.setCurrentTime(Tools.getCurrentTime());
        createNextAlarm(getDifferNum(currentTime, requestCodeAlarm),currentTime,requestCodeAlarm);
    }

    /**
     * @param currentTime 当前时间做参数
     * @param requestCodeAlarm 通过此标记，来得到相应的闹钟信息：AlarmMsg，并得到星期几，设置下一个闹钟
     * @return 返回下一个闹钟与当前相差的天数
     */
    public int getDifferNum(Calendar currentTime,int requestCodeAlarm){
        List<AlarmMsg> alarmList=mAlarmApp.getAlarmList();
        AlarmMsg currenAlarmMsg=null;
        int len=alarmList.size();
        Log.e("len=", len + "");
        for(int i=0;i<len;i++){
            Log.e("alarmId=",alarmList.get(i).getId()+"");
            if(requestCodeAlarm==alarmList.get(i).getId()){
                Log.e("alarmIdGet=",requestCodeAlarm+"");
                currenAlarmMsg=alarmList.get(i);//得到当前的闹钟
            }
        }
        //得到当前星期
        //得到当前系统的星期
        int dayOfweek=currentTime.get(Calendar.DAY_OF_WEEK)-1;
        //将要在下一个响铃的星期
        int nextDay=-1;
        if(currenAlarmMsg==null)//防止其他未取消的进行干扰
            return -1;
        int[] weeks=currenAlarmMsg.getWeeks();
//		for(int x:weeks)
//			Log.e("week",x+"");

        int top=dayOfweek;
        while(top<weeks.length-1){//往后找
            top++;
            if(weeks[top]==1){
                nextDay=top;
                break;
            }

        }
        if(nextDay==-1){//等于-1，说明还没有找到，就往前面找
            top=dayOfweek;
            while(top>0){
                top--;
                if(weeks[top]==1){
                    nextDay=top+7;
                    break;
                }

            }
        }
        Log.e("相差的天数", nextDay - dayOfweek + "");
        return nextDay - dayOfweek;
    }

    /**
     * 创建下一个闹钟
     * @param differNum 下个闹钟相差的天数
     * @param currentTime 当前时间
     * @param requestCodeAlarm 当前闹钟的唯一标志
     */
    public void createNextAlarm(int differNum,Calendar currentTime,int requestCodeAlarm){
        if(differNum>0){//往后面推相差的天数
            currentTime.set(Calendar.SECOND, 0);// 秒清零
            currentTime.set(Calendar.MILLISECOND, 0);// 毫秒清零
            currentTime.setTimeInMillis(currentTime.getTimeInMillis()+24*(differNum) * 60
                    * 60 * 1000);
            // 设置闹钟
            Intent alarmIntent=new Intent(Common.ALARM_MSG);//通过此intent来进行传递信息，来管理到底是哪一闹钟在响应

            alarmIntent.putExtra(Common.ALARM_ID, requestCodeAlarm);


           /* mAlarmManager.set(AlarmManager.RTC_WAKEUP,
                    currentTime.getTimeInMillis(), PendingIntent.getBroadcast(
                            getApplicationContext(), requestCodeAlarm, alarmIntent,*//* 发出指定的广播 *//*
                            PendingIntent.FLAG_CANCEL_CURRENT));*/

            alarmAppearView.createAlarm(currentTime.getTimeInMillis(),requestCodeAlarm,alarmIntent);
        }else{
            //标识遍历完都找不到要进行设置的时间，相当于只设置一次,不重复的闹钟
        }
    }
}
