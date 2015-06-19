package cn.hclab.alarm.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import cn.hclab.alarm.R;
import cn.hclab.alarm.ui.dialog.base.MinimumDialogFragment;
import cn.hclab.alarm.api.OnEditTextListener;

public class EditTagDialog extends MinimumDialogFragment {
	private EditText etContent;
	private OnEditTextListener edTextListener=null;
	public void initDialog(OnEditTextListener listener) {
		edTextListener = listener;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View dialogView = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_edit_alarm_setting, null);
		etContent = (EditText) dialogView.findViewById(R.id.et_dialog_content);
		builder.setView(dialogView)
				.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if (etContent != null) {
							edTextListener=(OnEditTextListener)getActivity();//一定要强转为我们的自定义接口
							edTextListener.updateContent(etContent.getText().toString());
						}
					}
				}).setNegativeButton("取消", null);
		return builder.create();
	}
}
