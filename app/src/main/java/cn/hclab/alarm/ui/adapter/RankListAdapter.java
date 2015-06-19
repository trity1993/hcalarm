package cn.hclab.alarm.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cc.trity.model.entities.AlarmUserInfo;
import cn.hclab.alarm.R;

public class RankListAdapter extends BaseAdapter {
	private LayoutInflater mInflater = null;
	private List<AlarmUserInfo> data;
	private Context context;

	public RankListAdapter(Context context, List data) {
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
			convertView = mInflater.inflate(R.layout.listview_rank_list_item,
					null);
			holder.tvTitle = (TextView) convertView
					.findViewById(R.id.rank_tv_title);
			holder.ivHead = (ImageView) convertView
					.findViewById(R.id.rank_iv_head);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.rank_tv_name);
			holder.tvSignature = (TextView) convertView
					.findViewById(R.id.rank_tv_signature);
			holder.tvNum = (TextView) convertView
					.findViewById(R.id.rank_tv_num);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 前三名头衔用图片显示
		switch (position) {
		case 0:
			// 清除操作
			holder.tvTitle.setBackground(getTextDrawable(R.drawable.crown));
			holder.tvTitle.setText("");// 防止回收的view,变成第一个了，导致字体存在。
			break;
		case 1:
			holder.tvTitle.setBackground(getTextDrawable(R.drawable.silver));
			holder.tvTitle.setText("");
			break;
		case 2:
			holder.tvTitle.setBackground(getTextDrawable(R.drawable.copper));
			holder.tvTitle.setText("");
			break;

		default:
			// 清楚默认的资源
			holder.tvTitle
					.setBackground(getTextDrawable(R.drawable.rank_tv_title_blue_bg));
			// 设置排名
			holder.tvTitle.setText(position + 1 + "");
			break;
		}
		holder.ivHead.setImageResource(R.drawable.head_small);
		holder.tvName.setText(data.get(position).getNickName());
		holder.tvSignature.setText(data.get(position).getSays());
//		holder.tvNum.setText(data.get(position).getNeedPoints() + "");
		return convertView;
	}

	public Drawable getTextDrawable(int id) {
		Drawable img = context.getResources().getDrawable(id);
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
		return img;
	}

	static class ViewHolder {
		public TextView tvTitle;
		public ImageView ivHead;
		public TextView tvName;
		public TextView tvSignature;
		public TextView tvNum;
	}
}
