package cn.hclab.alarm.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.hclab.alarm.ui.fragment.InfoFragment;
import cn.hclab.alarm.ui.fragment.MainFragment;
public final class ViewPagerFragment extends Fragment {
    private static final String KEY_CONTENT = "TestFragment:Content";
    public int currentNum;
	public static Fragment newInstance(String content,int currentNum) {
		Fragment mFragment=null;
		Log.d("currentNum=",currentNum+"");
		switch (currentNum) {
		case 0:
			mFragment=new MainFragment();
			break;
		case 1:
			mFragment=new InfoFragment();
			break;
		default:
			break;
		}

        return mFragment;
    }
	public ViewPagerFragment(){
		
	}
    private String mContent = "???";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }
}
