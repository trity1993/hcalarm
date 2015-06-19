package cn.hclab.alarm.api;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import cc.trity.common.Common;
import cn.hclab.alarm.mvp.views.AlarmAppearView;

/**
 * Created by TryIT on 2015/6/16.
 */
public class AlarmSensorEventListener implements SensorEventListener {
    private int shakeSenseValue;//摇一摇敏感值 设定
    private int alertValue = 0;//摇晃的次数
    private AlarmAppearView alarmAppearView;

    /**
     *
     * @param alarmAppearView
     * @param shakeSenseValue 传进摇晃的灵敏值
     */
    public AlarmSensorEventListener(AlarmAppearView alarmAppearView,int shakeSenseValue){
        this.alarmAppearView=alarmAppearView;
        this.shakeSenseValue=shakeSenseValue;
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();

        // values[0]:X轴，values[1]：Y轴，values[2]：Z轴
        float[] values = event.values;

        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            int value = (int) Math.max(Math.abs(values[0]), // 计算与敏感值的差值
                    Math.max(Math.abs(values[1]), Math.abs(values[2])))
                    - shakeSenseValue;
            if (value > 0) {
                alertValue += value;
                if (alertValue >= Common.SHAKE_NUMBER)// 摇晃的值》100就结束
                {
                    alarmAppearView.stopDevice();
                    alarmAppearView.updateSuccessView(alertValue);

                } else {
                    alarmAppearView.updateFailureView(alertValue);
                }
            }
        }
    }
    // 感应器发生改变
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
