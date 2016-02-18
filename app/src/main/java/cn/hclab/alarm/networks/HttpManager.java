package cn.hclab.alarm.networks;

import java.util.ArrayList;
import java.util.List;

import cc.trity.library.net.RequestParameter;

/**
 * 封装url的参数操作类
 * Created by TryIT on 2016/2/18.
 */
public class HttpManager {
    private static final String TAG="HttpManager";
    public static List<RequestParameter> getReqParameters(String strAccessToken,String strOpenId){
        List<RequestParameter> requestParameters=new ArrayList<>();
        //填充请求数据
        RequestParameter parameter=new RequestParameter("access_token",strAccessToken);
        RequestParameter parameter1=new RequestParameter("uid",strOpenId);

        requestParameters.add(parameter);
        requestParameters.add(parameter1);

        return requestParameters;
    }
}
