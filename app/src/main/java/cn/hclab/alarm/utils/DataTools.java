package cn.hclab.alarm.utils;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import cc.trity.model.entities.AlarmMsg;
import cc.trity.model.entities.AlarmUserInfo;

/**
 * Created by TryIT on 2015/6/11.
 */
public class DataTools {
    /*
	 * 模拟闹钟设置时间
	 */
    public static List<AlarmMsg> getDataInfo() {
        // 重getSharePreference里得到
        List<AlarmMsg> list = new ArrayList<AlarmMsg>();
        AlarmMsg alarmInfo;
        for (int i = 0; i < 3; i++) {
            alarmInfo = new AlarmMsg();
            alarmInfo.setLabel("闹钟");
//            alarmInfo.setRemainTime("11小时18分钟后");
            alarmInfo.setTime("07:30");
            alarmInfo.setWeek("每天");
            list.add(alarmInfo);
        }
        return list;
    }
    /*
     * 模拟排行榜
     */
    public static List<AlarmUserInfo> getUserInfoList() {
        // 重getSharePreference里得到
        List<AlarmUserInfo> list = new ArrayList<AlarmUserInfo>();
        AlarmUserInfo userInfo;
        for (int i = 0; i < 15; i++) {
            userInfo = new AlarmUserInfo();
            userInfo.setNickName("张旭辉" + i);
            userInfo.setSays("他从小就很屌丝");
            list.add(userInfo);
        }
        return list;
    }
    /*
     * 模拟头像
     */
    public static List<AlarmUserInfo> getUserInfoPortrait(Drawable[] resId) {
        // 重getSharePreference里得到
        List<AlarmUserInfo> list = new ArrayList<AlarmUserInfo>();
        AlarmUserInfo userInfo;
        for (int i = 0; i < resId.length; i++) {
            userInfo = new AlarmUserInfo();
//			userInfo.setHeadId(resId[i]);
            list.add(userInfo);
        }
        return list;
    }
    /*
     * 模拟注册信息
     */
    public static AlarmUserInfo getUserInfo(){
        AlarmUserInfo userInfo=new AlarmUserInfo();
        userInfo.setHead("http:");
        userInfo.setNickName("李俊");
        userInfo.setPassword("qq123456");
        userInfo.setPhoneNum("13415653716");
        userInfo.setSays("很好");
        userInfo.setSex("男");
        return userInfo;
    }
}
