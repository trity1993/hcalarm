package cc.trity.library.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间的出列类
 * Created by TryIT on 2016/1/19.
 */
public class TimeUtils {
    /**
     *
     * 注意 SimpleDateFormat 是线程不安全的
     * @param match 匹配的格式输出
     * @return 输出当前时间格式化的数据
     */
    public static String getCurentTime(String match){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(match);
        Date date=new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    /**
     * 传入timeMillis通过指定的规则match
     * 进行匹配输出对应的格式的字符串
     * @param timMillis
     * @param math
     * @return
     */
    public static String getAssignFormatTime(long timMillis,String math){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(math);
        Date date=new Date(timMillis);
        return simpleDateFormat.format(date);
    }
    public static String getAssignFormatTime(Date date,String math){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(math);
        return simpleDateFormat.format(date);
    }

    /**
     * 截取小时和分钟
     * @param date
     * @return
     */
    public static String getHM(String date){
        if(date==null){
            return "";
        }
        else if(date.length()<8){
            return "";
        }
        return date.substring(8, 12);
    }

    /**
     * 将日期+小时+分钟 进行拼接
     * @param strHourMinuite 格式为 0600
     * @return
     */
    public static String getCurAppointHour(String strHourMinuite){
        StringBuilder stringBuilder=new StringBuilder(getCurentTime("yyyyMMdd"));
        stringBuilder.append(strHourMinuite);
        return stringBuilder.toString();
    }

    /**
     * 得到第二天
     * @return
     */
    public static Date getTomorrow() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }

    /**
     * 依靠match将dateStr转换成对应的Date后
     * 再输出对应的mills毫秒为long型
     * @param dateStr
     * @param match
     * @return
     */
    public static long getTimemills(String dateStr,String match){
        SimpleDateFormat dateFormat=new SimpleDateFormat(match);
        Date date=null;
        try {
            date=dateFormat.parse(dateStr);
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date);
            return calendar.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
