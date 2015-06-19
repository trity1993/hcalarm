package cn.hclab.alarm.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import cc.trity.model.entities.AlarmUserInfo;
import cn.hclab.alarm.R;

import cn.hclab.alarm.utils.GsonTools;

public class VsAnimActivity extends Activity {
	private ImageView ivMan,ivWoman,ivLighting,ivVs;
	private TextView tvSure;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//得到对手的信息
		Intent intent=getIntent();
		String vsInfo=intent.getStringExtra("vsInfo");
		AlarmUserInfo userInfo=GsonTools.getClass(vsInfo, AlarmUserInfo.class);
		setContentView(R.layout.activity_vs);
		initView();
	}
	public void initView(){
		//man的照片view
		ivMan=(ImageView)findViewById(R.id.vs_iv_man);
		ivWoman=(ImageView)findViewById(R.id.vs_iv_woman);
		ivLighting=(ImageView)findViewById(R.id.vs_iv_lighting);
		ivVs=(ImageView)findViewById(R.id.vs_iv_vs_text);
		tvSure=(TextView)findViewById(R.id.vs_tv_sure);
		tvSure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				//弹回主界面，并关联两个好友，改变模式，进行30秒内，比胜负。
				Intent intent=new Intent(VsAnimActivity.this,ViewPageMainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//变成栈顶，就可以退出了
				startActivity(intent);
				finish();
			}
		});
		Animation anim=AnimationUtils.loadAnimation(this, R.anim.up_to_down);
		ivMan.startAnimation(anim);
		
		
		anim=AnimationUtils.loadAnimation(this, R.anim.down_to_up);
		ivWoman.startAnimation(anim);
		
		anim=AnimationUtils.loadAnimation(this, R.anim.lighting);
		ivLighting.startAnimation(anim);
		
		anim=AnimationUtils.loadAnimation(this, R.anim.lighting);
		ivVs.startAnimation(anim);
		
	}

}
