package cn.hclab.alarm.ui.activity.base;

import android.support.v7.app.ActionBarActivity;
import android.view.View;

/** 延迟操作，处理两次后退返回
 * Created by TryIT on 2015/6/14.
 */
public class DelayActivity extends ActionBarActivity {
    public View getRootView() {
        return getWindow().getDecorView();
    }

    /**
     * 使用线程，进行延迟操作，延迟delayMillis，再执行action，
     * 通常用于应用的退出程序的操作
     * @param action 线程执行
     * @param delayMillis 延迟时间
     */
    public void runOnUiThreadDelay(Runnable action, long delayMillis) {
        getRootView().postDelayed(action, delayMillis);
    }
}
