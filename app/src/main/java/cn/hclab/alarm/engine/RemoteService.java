package cn.hclab.alarm.engine;

import java.util.List;

import cc.trity.library.activity.BaseActivity;
import cc.trity.library.net.DefaultThreadPool;
import cc.trity.library.net.HttpRequest;
import cc.trity.library.net.RequestCallback;
import cc.trity.library.net.RequestParameter;
import cc.trity.library.net.URLData;
import cc.trity.library.net.UrlConfigManager;

/**
 * 网络异步请求的操作类
 * 获取请求队列，再加入到线程池中，最后进行执行
 * Created by TryIT on 2016/2/5.
 */
public class RemoteService {
    private static RemoteService service = null;

    private RemoteService() {

    }

    public static RemoteService getInstance() {
        if (RemoteService.service == null) {
            synchronized (RemoteService.class){
                RemoteService.service = new RemoteService();
            }
        }
        return RemoteService.service;
    }

    public void invoke(final BaseActivity activity,
                       final String apiKey,
                       final List<RequestParameter> params,
                       final RequestCallback callBack) {
        final URLData urlData = UrlConfigManager.findURL(activity, apiKey);

        HttpRequest request = activity.getRequestManager().createRequest(
                urlData, params, callBack);
        DefaultThreadPool.getInstance().execute(request);
    }
}
