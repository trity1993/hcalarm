package cn.hclab.alarm.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;

import cc.trity.common.Common;
import cn.hclab.alarm.R;
import cn.hclab.alarm.ui.activity.base.AppBaseActivity;
import cn.hclab.alarm.ui.adapter.ViewPagerFragmentAdapter;

public class ViewPageMainActivity extends AppBaseActivity {
	ViewPagerFragmentAdapter mAdapter;
	ViewPager mPager;


	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_view_page_circle);
		this.init(arg0);
	}

	@Override
	public void initVariables() {

	}

	@Override
	public void initView(Bundle savedInstanceState) {
		// 定义一个iewpager的adaper
		mAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager());
		// 定义个Pager，即布局中定义的那个pagerview
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		// 定义一个指示变量，即布局中定义的那个
		CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(mPager);
	}

	@Override
	public void loadData() {

	}

	/*
	 * 点击两次退出
	 */
	private boolean mIsBackKeyClicked;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			pressAgainExit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void pressAgainExit() {
		if (mIsBackKeyClicked) {
			finish();
		} else {
			Toast.makeText(this, R.string.press_back_again, Toast.LENGTH_SHORT)
					.show();
			mIsBackKeyClicked = true;
			runOnUiThreadDelay(new Runnable() {

				@Override
				public void run() {
					mIsBackKeyClicked = false;
				}
			}, Common.DOUBLE_CLICK_INTERVAL);
		}
	}

}
