package cn.hclab.alarm.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.sina.weibo.sdk.AccessTokenKeeper;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.LogoutAPI;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cc.trity.common.Common;
import cn.hclab.alarm.R;
import cn.hclab.alarm.ui.HcAlarmApp;
import cn.hclab.alarm.ui.activity.ExploitsActivity;
import cn.hclab.alarm.ui.activity.SignActivity;
import cn.hclab.alarm.ui.dialog.PromptDialog;
import cn.hclab.alarm.ui.dialog.RankingDialog;
import cn.hclab.alarm.ui.view.SlideShowView;

public class InfoFragment extends Fragment implements OnClickListener {
    @InjectView(R.id.info_slideshowview)
    SlideShowView slideShowView;

    private View rootView;
    private Activity mActivity;
    private Button btnPk, btnSign, btnRank, btnInfoMsg;
    private ImageView ivSex;
    private final int USERINFO_LOGINED = 1;
    private final int USERINFO_LOGOUT = -1;
    private int user_sex;
    private boolean isLogined = false;
    private HcAlarmApp hcAlarmApp;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case USERINFO_LOGINED:
                    if (rootView != null) {
                        Logined();
                    }
                    break;
                case USERINFO_LOGOUT:
                    if (rootView != null) {
                        Logout();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        hcAlarmApp = (HcAlarmApp) mActivity.getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_info, null);
        ButterKnife.inject(this, rootView);
        initView();

        // 判断是否为登录状态
        if (hcAlarmApp.getUserInfo() != null && hcAlarmApp.getUserInfo().getAuthentication() != null) {
            Logined();

        }
        return rootView;
    }

    /**
     * 初始化view
     */
    public void initView() {
        btnPk = (Button) rootView.findViewById(R.id.info_btn_pk);
        btnSign = (Button) rootView.findViewById(R.id.info_btn_sign);
        btnRank = (Button) rootView.findViewById(R.id.info_btn_ranking_list);
        btnInfoMsg = (Button) rootView.findViewById(R.id.info_btn_msg);
        btnSign.setOnClickListener(this);
        btnPk.setOnClickListener(this);
        btnRank.setOnClickListener(this);
        btnInfoMsg.setOnClickListener(this);
    }

    /*
     * 处理登录的状态
     */
    public void Logined() {
        // 登录和注册的按钮消失
        btnSign.setText("注销");
        // 隐藏的imageview
        ivSex = (ImageView) rootView.findViewById(R.id.info_iv_sex);
//        ivSex.setImageDrawable(getResources().obtainTypedArray(
//                R.array.image_portrait_sex).getDrawable(user_sex));
        // 显示出预设的照片
        ivSex.setVisibility(View.VISIBLE);
        isLogined=true;
    }

    /*
     * 处理注销操作
     */
    public void Logout() {
        // 登录和注册的按钮显示
        btnSign.setText("登录/注册");
        // 显示出预设的照片
        ivSex.setVisibility(View.GONE);
        // 隐藏出注销按钮
        // 并把共享数据清空,和清除sharepreference
        if (hcAlarmApp != null) {
            if (hcAlarmApp.getUserInfo().getAuthentication().getFreshAccessToken() != null) {//则说明为新浪方式登录
                new LogoutAPI(mActivity, Common.WEIBO_APP_ID,
                        AccessTokenKeeper.readAccessToken(mActivity)).logout(new LogOutRequestListener());
            } else {//否则为腾讯方式登录
            }
            hcAlarmApp.saveUserInfo(null);
            hcAlarmApp.setUserInfo(null);
            isLogined=false;
        }
    }

    /**
     * 登出按钮的监听器，接收登出处理结果。（API 请求结果的监听器）
     */
    private class LogOutRequestListener implements RequestListener {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String value = obj.getString("result");

                    if ("true".equalsIgnoreCase(value)) {
                        AccessTokenKeeper.clear(mActivity);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Log.e("WeiboException", "注销失败");
        }
    }

    /*
     * 弹出排行榜
     */
    public void showRankDialog() {
        RankingDialog rankingDialog = new RankingDialog();
        rankingDialog.show(mActivity.getFragmentManager(), "RankingDialog");
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.info_btn_pk://PK 跳转，进行选择好友
                if(hcAlarmApp.getUserInfo()!=null){

                }else{
                    PromptDialog promptDialog = new PromptDialog();
                    promptDialog.initMsg("请登录！");
                    promptDialog.show(mActivity.getFragmentManager(), "error_login");
                }

                break;
            case R.id.info_btn_sign:
                if(isLogined){
                    mHandler.sendEmptyMessage(USERINFO_LOGOUT);
                }else{
                    // 打开注册页面
                    startActivity(new Intent(mActivity, SignActivity.class));
                }
                break;
            case R.id.info_btn_ranking_list:// 排行
                if(hcAlarmApp.getUserInfo()!=null){
                    showRankDialog();
                }else{
                    PromptDialog promptDialog = new PromptDialog();
                    promptDialog.initMsg("请登录！");
                    promptDialog.show(mActivity.getFragmentManager(), "error_login");
                }
                break;
            case R.id.info_btn_msg:
                // 个人信息页
			if (hcAlarmApp.getUserInfo() != null)
                startActivity(new Intent(mActivity, ExploitsActivity.class));
			else {
				PromptDialog promptDialog = new PromptDialog();
				promptDialog.initMsg("请登录！");
				promptDialog.show(mActivity.getFragmentManager(), "error_login");
			}
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        if (hcAlarmApp != null && hcAlarmApp.getUserInfo() != null && hcAlarmApp.getUserInfo().getAuthentication() != null) {
            // 判断是否登录过，以后进行存储，保持登录的状态
            mHandler.sendEmptyMessage(USERINFO_LOGINED);
        }

        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
