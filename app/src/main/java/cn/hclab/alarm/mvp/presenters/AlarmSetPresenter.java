package cn.hclab.alarm.mvp.presenters;

import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

import cc.trity.common.Common;
import cc.trity.model.entities.AlarmMsg;
import cn.hclab.alarm.mvp.views.AlarmSetView;
import cn.hclab.alarm.ui.HcAlarmApp;

/** 处理创建闹钟的逻辑，到时候增加数据库的时候，再相应的增加相关的model
 * Created by TryIT on 2015/6/16.
 */
public class AlarmSetPresenter {
    private AlarmSetView alarmSetView;
    private HcAlarmApp hcAlarmApp;
    private AlarmMsg alarmInfo;

    public AlarmSetPresenter(AlarmSetView alarmSetView, HcAlarmApp hcAlarmApp){
        this.alarmSetView=alarmSetView;
        this.hcAlarmApp=hcAlarmApp;
        alarmInfo = new AlarmMsg();
    }

    /**
     * 用于存储闹钟的信息
     */
    public void saveAlarmMsgOption(){
        // 标签
        alarmInfo.setLabel(alarmSetView.getTag());
        // 显示设置的时间
        alarmInfo.setTime(alarmSetView.getTime());

        // 设置选着的星期，并存储星期weeks
        alarmSetView.getWeek(alarmInfo);
    }

    /**
     * 保存响铃闹钟的时间，并把相关的信息，保存到xml中
     * @param currentTime
     */
    public void saveAlarmMsgTime(Calendar currentTime){
        List<AlarmMsg> dataAlarmList  = hcAlarmApp.getAlarmList();
        // 存储当前的时间
        alarmInfo.setTimes(currentTime.getTimeInMillis());
        // 默认设置闹钟开启
        alarmInfo.setOpen(true);

        // 保存完数据就进行加入list,即可显示
        dataAlarmList.add(alarmInfo);
        // 改变整个共享数据：AlarmList
        hcAlarmApp.setAlarmList(dataAlarmList);
        // 变成json的格式进行存储
        hcAlarmApp.saveAlarmList(dataAlarmList);
    }
    /**
     * 进行设置系统闹钟时间
     */
    public void addSystemAlarm(){
        //保存闹钟额外的信息，标签等
        saveAlarmMsgOption();
        //得到修正后的时间
        Calendar currentTime=getUpdateCorrectTime();
        //存储闹钟相关的信息
        saveAlarmMsgTime(currentTime);
        // 设置闹钟
        int requestCode =  alarmInfo.getId();//得到唯一的标示,里面已经进行运算
        Log.e("alarmidSet", requestCode + "");
        Intent alarmIntent=new Intent(Common.ALARM_MSG);//通过此intent来进行传递信息，来管理到底是哪一闹钟在响应

        alarmIntent.putExtra(Common.ALARM_ID, requestCode);

        alarmSetView.createAlarm(currentTime.getTimeInMillis(),requestCode,alarmIntent);
        alarmSetView.move();
    }
    /**
     * alarmInfo 用以得到星期的数组
     * @return 通过与当前时间以及星期进行匹配，修正得到正确的闹钟时间
     */
    public Calendar getUpdateCorrectTime(){
        String strRemainTime;
        // 得到当前设置的时间
        long setTimeMinute = Integer.parseInt(alarmSetView.getHour()) * 60
                + Integer.parseInt(alarmSetView.getMinute());

        //得到当前系统的时间
        Calendar currentTime = Calendar.getInstance();

        long currentTimeMinute = currentTime.get(Calendar.HOUR_OF_DAY) * 60
                + currentTime.get(Calendar.MINUTE);
        //得到当前系统的星期
        int dayOfweek=currentTime.get(Calendar.DAY_OF_WEEK)-1;

        //将要在下一个响铃的星期
        int nextDay=0;
        int[] weeks=alarmInfo.getWeeks();
        int top=dayOfweek;//标记标示
        while(top<weeks.length){//往后找
            if(weeks[top]==1){
                nextDay=top;
                break;
            }
            top++;
        }
        if(nextDay==0){//等于0，说明还没有找到，就往前面找,差top+7
            top=dayOfweek;
            while(top>=0){
                if(weeks[top]==1){
                    nextDay=top+7;
                    break;
                }
                top--;
            }
        }
        if(nextDay-dayOfweek>0){//往后面推相差的天数,若往前面找,相差（top-dayOfweek）+7，即：top+7-dayOfweek
            if (setTimeMinute <= currentTimeMinute){
                strRemainTime = (nextDay-dayOfweek)+"天" + (currentTimeMinute - setTimeMinute) / 60 + "小时"
                        + (currentTimeMinute - setTimeMinute) % 60 + "分钟";
            }
            else{
                strRemainTime = (nextDay-dayOfweek)+"天"+(setTimeMinute - currentTimeMinute) / 60 + "小时"
                        + (setTimeMinute - currentTimeMinute) % 60 + "分钟";//这里是显示相差多久进行响铃
            }
            alarmSetView.showToast(strRemainTime);

            //这里才是真正设置响铃的时候
            currentTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(alarmSetView.getHour()));
            currentTime.set(Calendar.MINUTE, Integer.parseInt(alarmSetView.getMinute()));
            currentTime.set(Calendar.SECOND, 0);// 秒清零
            currentTime.set(Calendar.MILLISECOND, 0);// 毫秒清零
            currentTime.setTimeInMillis(currentTime.getTimeInMillis()+24*(nextDay-dayOfweek) * 60
                    * 60 * 1000);
        }
        else{//标识遍历完都找不到要进行设置的时间，相当于只设置一次,不重复的闹钟
            if (setTimeMinute <= currentTimeMinute) {// 当设置的时间比当前的时间小的时候，则往后推迟一天,将当前的时间改变为闹钟的时间
                // 计算出离闹钟要响铃的时间
                strRemainTime = "1天" + (currentTimeMinute - setTimeMinute) / 60 + "小时"
                        + (currentTimeMinute - setTimeMinute) % 60 + "分钟";
                alarmSetView.showToast(strRemainTime);

                // 设置最终的闹钟时间
                currentTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(alarmSetView.getHour()));
                currentTime.set(Calendar.MINUTE, Integer.parseInt(alarmSetView.getMinute()));
                currentTime.set(Calendar.SECOND, 0);// 秒清零
                currentTime.set(Calendar.MILLISECOND, 0);// 毫秒清零
                currentTime.setTimeInMillis(currentTime.getTimeInMillis() + 24 * 60
                        * 60 * 1000);
            } else {
                strRemainTime = (setTimeMinute - currentTimeMinute) / 60 + "小时"
                        + (setTimeMinute - currentTimeMinute) % 60 + "分钟";
                //提示剩余时间
                alarmSetView.showToast(strRemainTime);

                currentTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(alarmSetView.getHour()));
                currentTime.set(Calendar.MINUTE, Integer.parseInt(alarmSetView.getMinute()));
                currentTime.set(Calendar.SECOND, 0);// 秒清零
                currentTime.set(Calendar.MILLISECOND, 0);// 毫秒清零
                currentTime.setTimeInMillis(currentTime.getTimeInMillis());
            }
        }
        return currentTime;
    }
}
