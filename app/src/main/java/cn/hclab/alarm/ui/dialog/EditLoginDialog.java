package cn.hclab.alarm.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import cn.hclab.alarm.R;
import cn.hclab.alarm.ui.HcAlarmApp;
import cn.hclab.alarm.ui.dialog.base.MinimumDialogFragment;
import cn.hclab.alarm.api.OnEditTextListener;
import cn.hclab.alarm.utils.Tools;

public class EditLoginDialog extends MinimumDialogFragment {
	private EditText etLoginId, etLoginPwd;
	private OnEditTextListener edTextListener = null;
	private AlertDialog mAlertDialog;
	private final int INFOLOGINING = 0;
	private HcAlarmApp mHcAlarmApp;

	public void initDialog(OnEditTextListener listener) {
		edTextListener = listener;
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case INFOLOGINING:
//				loginServer();
				break;
			default:
				break;
			}

		};
	};
	/*
	 * 进行尝试登陆到服务器
	 */
	/*private RequestQueue mQueue;

	public void loginServer() {

		mQueue = Volley.newRequestQueue(getActivity());
		JsonObjectRequest req = getPostStringRequest(
				Common.USERINFO_LOGIN, new UserInfo(etLoginId.getText()
				.toString(), etLoginPwd.getText().toString()));
		mQueue.add(req);
	}*/
	/*
	 * 使用Volly框架，并用post对网络进行请求
	 */
	/*public JsonObjectRequest getPostStringRequest(String command,
			final UserInfo mInfo) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("data", mInfo);
		params.put("command", command);
		JSONObject mJsonObject = null;
		try {
			mJsonObject = new JSONObject(GsonTools
					.createGsonString(params));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		Log.v("mJsonObject:", mJsonObject.toString());
		//加载进度条
		if (mAlertDialog != null)
			mAlertDialog.show();
		JsonObjectRequest req = new JsonObjectRequest(
				UrlConnectionToServer.urlAddress, mJsonObject,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							Log.v("Response:%n %s", response.toString(4));
							Command command = GsonTools.getClass(
									response.toString(), Command.class);
							switch (command.getState().intValue()) {
							case 0:// 表示登录失败
								break;
							case 1:// 表示登录成功，进行保存状态和存储到sharepreference
								mHcAlarmApp.setUserInfoId(etLoginId.getText()
										.toString());
								//更改相应的信息
								UserInfo mUserInfo=mHcAlarmApp.getUserInfo();
								mUserInfo.setPhoneNum(etLoginId.getText()
										.toString());
								mHcAlarmApp.saveUserInfo(mUserInfo);
								break;

							default:
								break;
							} // 使用接口，尽心接收数据
							if (mAlertDialog != null&& mAlertDialog.isShowing())
								mAlertDialog.dismiss();
							edTextListener.updateContent(command.getState().intValue()
									+ "");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("Error: ", error.getMessage());
						
					}
				});
		return req;
	}
*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHcAlarmApp = (HcAlarmApp) getActivity().getApplication();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View dialogView = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_edit_login, null);
		etLoginId = (EditText) dialogView.findViewById(R.id.et_login_id);
		etLoginPwd = (EditText) dialogView.findViewById(R.id.et_login_pwd);
		builder.setView(dialogView)
				.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if (etLoginId != null && etLoginPwd != null
								&& edTextListener != null) {
							mAlertDialog = Tools
									.CommonProgressDialog(getActivity());
							mHandler.sendEmptyMessage(INFOLOGINING);
						}
					}
				}).setNegativeButton("取消", null);
		return builder.create();
	}
}
