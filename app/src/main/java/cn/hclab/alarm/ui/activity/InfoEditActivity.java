package cn.hclab.alarm.ui.activity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import cc.trity.common.Common;
import cc.trity.model.entities.AlarmUserInfo;
import cn.hclab.alarm.R;
import cn.hclab.alarm.api.OnEditTextListener;
import cn.hclab.alarm.ui.HcAlarmApp;
import cn.hclab.alarm.ui.dialog.PortraitDialog;
import cn.hclab.alarm.utils.Tools;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class InfoEditActivity extends SwipeBackActivity implements
		OnClickListener, OnEditTextListener {
	private ImageView ivBtnPortrait;
	private Button btnSave;
	private int portraitId;
	private EditText etSays, etNickName, etSex;
	private final int PORTRAIT_SELECTED = 1;
	private final int PORTRAIT_IMAGEURL = 2;
	private AlarmUserInfo userInfo;
	private SharedPreferences spInfoMsg;
	private HcAlarmApp hcAlarmApp;
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what){
				case PORTRAIT_SELECTED:
					// 更新头像
					ivBtnPortrait.setImageDrawable(getResources().obtainTypedArray(
							R.array.image_portrait_big).getDrawable(portraitId));
					break;
				case PORTRAIT_IMAGEURL:
					ivBtnPortrait.setImageBitmap((Bitmap)msg.obj);
					break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_edit);
		hcAlarmApp=(HcAlarmApp)getApplication();
		initView();
		getInfoMsg();// 获取数据
		// 设置滑动后退
	}

	private void initView() {
		ivBtnPortrait = (ImageView) findViewById(R.id.info_edit_iv_portrait);
		btnSave = (Button) findViewById(R.id.info_edit_save);
		etSays = (EditText) findViewById(R.id.info_edit_says);
		etNickName = (EditText) findViewById(R.id.info_edit_nickName);
		etSex = (EditText) findViewById(R.id.info_edit_sex);
		// 监听事件
		ivBtnPortrait.setOnClickListener(this);
		btnSave.setOnClickListener(this);
	}

	/*
	 * 获取数据，从sharePreference中获取,并进行设置
	 */
	public void getInfoMsg() {
		userInfo=hcAlarmApp.getUserInfo();
		if (userInfo != null) {
			// 设置显示的view内容
//			ivBtnPortrait.setImageDrawable(userInfo.getHeadId());
			if(userInfo.getHead()!=null){
				new Thread(new Runnable() {
					@Override
					public void run() {
						Bitmap bitmap=Tools.getbitmap(userInfo.getHead());
						bitmap=Tools.getCroppedBitmap(bitmap, Common.BITMAP_RADIUS,getResources().getColor(R.color.blue_bottom));
						Message msg=new Message();
						msg.what=PORTRAIT_IMAGEURL;
						msg.obj=bitmap;
						mHandler.sendMessage(msg);
					}
				}).start();
			}
			if(userInfo.getSays()!=null)
				etSays.setHint(userInfo.getSays());
			else
				etSays.setHint(R.string.info_signature);
			etNickName.setHint(userInfo.getNickName());
			etSex.setHint(userInfo.getSex());
		}
	}

	/*
	 * 保存用户的资料
	 */
	public void saveInfoMsg() {
		if (etSays.getText() != null || etNickName.getText() != null
				|| etSex.getText() != null) {
			if (userInfo != null) {
				// 如果其中一个改变，就进行更新数据，否则不进行跟新
				if (etSays.getText() != null)
					userInfo.setSays(etSays.getText().toString());
				if (etNickName.getText() != null)
					userInfo.setSays(etNickName.getText().toString());
				if (etSex.getText() != null)
					userInfo.setSays(etSex.getText().toString());
				// 更新字符串-本地-网络
				hcAlarmApp.saveUserInfo(userInfo);
				// 网络 ...
			}
		}
		finish();
	}

	public void showPortraitDialog() {
		PortraitDialog portraitDialog = new PortraitDialog();
		portraitDialog.show(getFragmentManager(), "RankingDialog");
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.info_edit_iv_portrait:
			showPortraitDialog();
			break;
		case R.id.info_edit_save:
			saveInfoMsg();
			break;
		default:
			break;
		}
	}

	@Override
	public void updateContent(String response) {
		if (!response.equals("")) {
			portraitId = Integer.parseInt(response);
			mHandler.sendEmptyMessage(PORTRAIT_SELECTED);
		}
	}

}
