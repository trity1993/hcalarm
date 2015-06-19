package cn.hclab.alarm.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

import cn.hclab.alarm.R;
import cn.hclab.alarm.ui.dialog.base.MinimumDialogFragment;

public class PromptDialog extends MinimumDialogFragment {
	private TextView tvTag;
	private String[] strItems;
	private String msg = null;
	public void initMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View dialogView = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_tag, null);
		tvTag = (TextView) dialogView.findViewById(R.id.tv_dialog_title);
		if (msg != null) {
			tvTag.setText(msg);
		} else {
			// 得到资源
			strItems = getResources().getStringArray(R.array.morning_motto);
			// 设置
			tvTag.setText(strItems[ new Random().nextInt(strItems.length)]);
		}
		builder.setView(dialogView)
				.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
					}
				}).setNegativeButton("取消", null);
		return builder.create();
	}
}
