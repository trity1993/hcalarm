package cc.trity.library.net;

/**
 * 请求后的回调接口
 * Created by TryIT on 2016/2/4.
 */
public interface RequestCallback {
    public void onSuccess(String content);

    public void onFail(String errorMessage);
}
