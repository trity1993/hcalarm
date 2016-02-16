package cn.hclab.alarm.ui.activity.base;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import cn.hclab.alarm.R;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**实现滑动返回，toolbar初始化，后退按钮的实现
 * Created by TryIT on 2015/5/17.
 */
public abstract class AppBaseActivity extends SwipeBackActivity {

    /**
     * 初始化toolbar
     * @param mToolbar
     */
    protected void trySetupToolbar(Toolbar mToolbar) {
        try {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e(getClass().getSimpleName(), "toolbar is null!");
        }
    }

    /**
     * 实现后退按钮
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home://相应 actionbar的后退按钮
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {//后退
        super.onBackPressed();
    }


    @Override
    public void finish() {
        super.finish();
        // 仍然是这句，但不得不再写一遍，否则退出的时候，会调用成 scale_in 的反向动画，读者可以做实验试试
        overridePendingTransition(R.anim.translate_right_on, R.anim.translate_right_out);
    }

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
