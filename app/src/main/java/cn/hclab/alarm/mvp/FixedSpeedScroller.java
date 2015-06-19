package cn.hclab.alarm.mvp;

import java.lang.reflect.Field;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * 用作轮播图 图片滑动动画时间控制类 ,如果用默认时间可用不这个类
 * 
 * FixedSpeedScroller scroller = new FixedSpeedScroller(context,new
 * AccelerateInterpolator());
 */

public class FixedSpeedScroller extends Scroller {
	Context mContext;
	private int mDuration = 500;

	public FixedSpeedScroller(Context context) {
		super(context);
		mContext = context;
	}

	public FixedSpeedScroller(Context context, Interpolator interpolator) {
		super(context, interpolator);
		mContext = context;
	}

	/**
	 * 设置滑动时间 ,如果用默认时间可不用这个类 ,反射技术实现
	 * 
	 * @param vp
	 *            ViewPager 对象
	 * @param time
	 *            时间
	 */
	public void setDuration(ViewPager vp, int time) {
		try {
			Field field = ViewPager.class.getDeclaredField("mScroller");
			field.setAccessible(true);
			setmDuration(time);
			field.set(vp, this);
		} catch (Exception e) {
		}
	}

	@Override
	public void startScroll(int startX, int startY, int dx, int dy, int duration) {
		super.startScroll(startX, startY, dx, dy, mDuration);
	}

	@Override
	public void startScroll(int startX, int startY, int dx, int dy) {
		super.startScroll(startX, startY, dx, dy, mDuration);
	}

	public void setmDuration(int time) {
		mDuration = time;
	}

	public int getmDuration() {
		return mDuration;
	}
}