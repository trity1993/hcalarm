
package cn.hclab.alarm.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.hclab.alarm.ui.fragment.ViewPagerFragment;

public class ViewPagerFragmentAdapter extends FragmentPagerAdapter {
    protected static final String[] CONTENT = new String[] { "Main", "Info"};

    private int mCount = CONTENT.length;

    public ViewPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return ViewPagerFragment.newInstance(CONTENT[position % CONTENT.length], position);
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return ViewPagerFragmentAdapter.CONTENT[position % CONTENT.length];
    }

   /* public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }*/
}