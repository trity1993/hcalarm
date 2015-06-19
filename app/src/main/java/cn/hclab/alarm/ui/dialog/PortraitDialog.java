package cn.hclab.alarm.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import cn.hclab.alarm.R;
import cn.hclab.alarm.api.OnEditTextListener;
import cn.hclab.alarm.ui.adapter.PortraitAdapter;
import cn.hclab.alarm.ui.dialog.base.MinimumDialogFragment;
import cn.hclab.alarm.utils.DataTools;


public class PortraitDialog extends MinimumDialogFragment {
	private GridView gridview;
	private PortraitAdapter portraitAdapter;
	private OnEditTextListener edTextListener = null;
	private int portraitSelectedId;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View dialogView = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_portrait, null);
		gridview = (GridView) dialogView.findViewById(R.id.portrait_gv);
		//获取资源：
		Drawable [] resId=new Drawable[8];
		for(int i=0;i<resId.length;i++){
		resId[i]=getResources().obtainTypedArray(
				R.array.image_portrait_normal).getDrawable(i);
		}
		//实现gridview
		portraitAdapter=new PortraitAdapter(getActivity(), DataTools.getUserInfoPortrait(resId));
		gridview.setAdapter(portraitAdapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				edTextListener=(OnEditTextListener)getActivity();
				portraitSelectedId=position;
				
			}
		});
		builder.setView(dialogView).setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				edTextListener.updateContent(portraitSelectedId+"");
			}
		}).setNegativeButton("取消", null);
		return builder.create();
	}	
}
