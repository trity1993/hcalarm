package cc.trity.library.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cc.trity.library.net.RequestManager;

/**
 * Created by TryIT on 2016/2/4.
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 请求列表管理器
     */
    protected RequestManager requestManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestManager=new RequestManager();
        super.onCreate(savedInstanceState);
        initVariables();
    }

    protected void init(Bundle savedInstanceState){
        initView(savedInstanceState);
        loadData();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 初始化变量，包括Intent带的数据和Activity内的变量
     * 注意这个在得到view布局之前执行
     */
    public abstract void initVariables();

    /**
     * 加载Layout布局文件，初始化控件，以及相应的事件方法
     */
    public abstract void initView(Bundle savedInstanceState);

    /**
     * 进行调用获取数据
     */
    public abstract void loadData();

    public RequestManager getRequestManager() {
        return requestManager;
    }
}
