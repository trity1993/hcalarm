package cn.hclab.alarm.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import cn.hclab.alarm.R;
import cn.hclab.alarm.ui.adapter.RankListAdapter;
import cn.hclab.alarm.ui.dialog.base.MinimumDialogFragment;
import cn.hclab.alarm.utils.DataTools;
/*
 * 好友排名的DialogFragment
 */
public class RankingDialog extends MinimumDialogFragment {
	private ListView listView;
	private RankListAdapter rankListAdapter;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View dialogView = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_ranking, null);
		listView = (ListView) dialogView.findViewById(R.id.lv_dialog_rank);
		//实现listview
		rankListAdapter=new RankListAdapter(getActivity(), DataTools.getUserInfoList());
		listView.setAdapter(rankListAdapter);
		builder.setView(dialogView);
		return builder.create();
	}
}