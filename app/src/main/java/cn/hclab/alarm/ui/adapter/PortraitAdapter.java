package cn.hclab.alarm.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import cc.trity.model.entities.AlarmUserInfo;
import cn.hclab.alarm.R;

public class PortraitAdapter extends BaseAdapter {
	private LayoutInflater mInflater = null;
	private List<AlarmUserInfo> data;
	private Context context;

	public PortraitAdapter(Context context, List data) {
		// 根据context上下文加载布局，这里的是Demo17Activity本身，即this
		this.mInflater = LayoutInflater.from(context);
		this.data = data;
		this.context = context;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.gridview_list_item, null);
			holder.ivHead = (ImageView) convertView.findViewById(R.id.gd_iv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
//		holder.ivHead.setImageDrawable(data.get(position).getHeadId());
		return convertView;
	}

	public Drawable getTextDrawable(int id) {
		Drawable img = context.getResources().getDrawable(id);
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
		return img;
	}

	static class ViewHolder {
		public ImageView ivHead;
	}
}
