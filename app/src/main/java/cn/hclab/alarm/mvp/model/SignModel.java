package cn.hclab.alarm.mvp.model;

/**
 * Created by TryIT on 2015/6/14.
 */
public interface SignModel<T> {
    T signOperation(String tag);//执行登录操作,tag进行标记，以及一些值的传入
    void onSuccess();
    void onFailure();
}
