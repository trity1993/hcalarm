package cn.hclab.alarm.ui.adapter;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import java.util.List;

import cc.trity.model.entities.AlarmMsg;
import cn.hclab.alarm.R;

public class AlarmListAdapter extends BaseAdapter {

	private LayoutInflater mInflater = null;
	private List<AlarmMsg> data;
	private Context context;

	public AlarmListAdapter(Context context,List data) {
		// 根据context上下文加载布局，这里的是Demo17Activity本身，即this
		this.mInflater = LayoutInflater.from(context);
		this.data=data;
		this.context=context;
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
	public void remove(int position){
		data.remove(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=mInflater.inflate(R.layout.listview_alram_list_item, null);
			holder.tvDay=(TextView)convertView.findViewById(R.id.tvDay);
			holder.tvLabel=(TextView)convertView.findViewById(R.id.tvLabel);
			/*holder.tvRemainTime=(TextView)convertView.findViewById(R.id.tvRemainTime);*/
			holder.tvTime=(TextView)convertView.findViewById(R.id.tvTime);
			holder.switchCompatBtn=(SwitchCompat)convertView.findViewById(R.id.switchCompat_open);
			convertView.setTag(holder);
		}
		else{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.tvDay.setText((String)data.get(position).getWeek());
		holder.tvLabel.setText((String)data.get(position).getLabel());
//		holder.tvRemainTime.setText((String)data.get(position).getRemainTime());
		holder.tvTime.setText((String)data.get(position).getTime());
		holder.switchCompatBtn.setChecked(data.get(position).isOpen());
		//toggleBtn监听开关
		holder.switchCompatBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					//将当前时间进行注册
				}
				else{
					//取消当前的时间
				}
			}
		});
		return convertView;
	}

	static class ViewHolder {
		public TextView tvDay;
		public TextView tvLabel;
		public TextView tvRemainTime;
		public TextView tvTime;
		public SwitchCompat switchCompatBtn;
	}

}
