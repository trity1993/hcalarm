package cn.hclab.alarm.mvp.presenters;

/**
 * Created by TryIT on 2015/6/14.
 */
public interface SignPresenter {
    Object login(int flag);//使用基类，为后面强转成相应的回调类
    void onSuccess();
    void onFault();
}
