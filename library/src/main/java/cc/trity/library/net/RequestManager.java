package cc.trity.library.net;

import java.util.ArrayList;
import java.util.List;

/**
 * 维护请求队列，添加和删除请求
 * Created by TryIT on 2016/2/5.
 */
public class RequestManager {
    private List<HttpRequest> requestList = null;
    public RequestManager(){
        // 异步请求列表
        requestList=new ArrayList<>();
    }

    /**
     * 无参数调用
     */
    public HttpRequest createRequest(final URLData urlData,
                                     final RequestCallback requestCallback) {
        return createRequest(urlData, null, requestCallback);
    }

    /**
     * 有参数调用
     */
    public HttpRequest createRequest(final URLData urlData,
                                     final List<RequestParameter> params,
                                     final RequestCallback requestCallback) {
        final HttpRequest request = new HttpRequest(urlData, params,
                requestCallback);

        addRequest(request);
        return request;
    }

    /**
     * 添加Request到列表
     */
    public void addRequest(final HttpRequest request) {
        requestList.add(request);
    }

    /**
     * 取消网络请求
     */
    public void cancelRequest() {
        if ((requestList != null) && (requestList.size() > 0)) {
            for (final HttpRequest request : requestList) {
//                if (request.getRequest() != null) {
//                    try {
//                        request.getRequest().abort();
//                        requestList.remove(request.getRequest());
//                    } catch (final UnsupportedOperationException e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        }
    }
}
