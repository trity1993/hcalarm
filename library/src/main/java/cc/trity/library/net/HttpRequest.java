package cc.trity.library.net;

import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cc.trity.library.utils.LogUtils;

/**
 * 执行http请求操作
 * Created by TryIT on 2016/2/4.
 */
public class HttpRequest implements Runnable {
    private static final String TAG="HttpRequest";
    private URLData urlData = null;
    private RequestCallback requestCallback = null;
    private List<RequestParameter> parameter = null;
    private String urlStr = null;
//    private HttpURLConnection httpURLConnection;

    protected Handler handler;

    public HttpRequest(final URLData data,final List<RequestParameter> parameter,
                      final RequestCallback callback){
        this.urlData=data;
        this.urlStr=urlData.getUrl();
        this.parameter=parameter;
        this.requestCallback=callback;

        handler=new Handler();
    }

    @Override
    public void run() {
        HttpURLConnection httpURLConnection=null;
        try {
            String netType=urlData.getNetType();
            if(netType.equals("get")){
                //设置参数得到url
                final StringBuffer paramBuffer = new StringBuffer();
                if ((parameter != null) && (parameter.size() > 0)) {
                    for(RequestParameter p:parameter){
                        if(paramBuffer.length()==0){
                            paramBuffer.append(p.getName()+"="+p.getValue());
                        }else{
                            paramBuffer.append("&"+p.getName()+"="+p.getValue());
                        }
                    }
                }
                String newUrl=urlStr+"?"+paramBuffer.toString();
                LogUtils.d(TAG,newUrl);

                URL url=new URL(newUrl);
                httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);
                //额外设置
                httpURLConnection.setConnectTimeout(8000);
                httpURLConnection.setReadTimeout(8000);
                httpURLConnection.connect();//连接

            }else if(netType.equals("post")){
                URL url=new URL(urlStr);
                httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoInput(true);
                //额外设置
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.setReadTimeout(8000);
                httpURLConnection.connect();//连接
                //发送post数据
                DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
                //添加参数
                StringBuffer sBuffer=new StringBuffer();
                if(parameter!=null&&parameter.size()>0){
                    for(RequestParameter pTmp:parameter){
                        if(sBuffer.length()==0){
                            sBuffer.append(pTmp.getName()+"="+pTmp.getValue());
                        }else{
                            sBuffer.append("&"+pTmp.getName()+"="+pTmp.getValue());
                        }
                    }
                    out.writeBytes(sBuffer.toString());
                }
            }else{
                return ;
            }

            //获取返回的数据
            if(httpURLConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                InputStream inputStream=httpURLConnection.getInputStream();
                if(inputStream!=null){
                    BufferedReader bufReader=new BufferedReader(new InputStreamReader(inputStream));
                    String line = null;
                    final StringBuffer sbuf = new StringBuffer();
                    while ((line = bufReader.readLine()) != null) {
                        sbuf.append(line);
                    }
                    //成功进行回调
                    if(requestCallback!=null){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                requestCallback.onSuccess(sbuf.toString());

                            }
                        });
                    }
                }else{
                    handleNetworkError("网络异常");
                }
            }else{
                handleNetworkError("网络异常");
            }

        }catch (Exception e){
            LogUtils.e(TAG, Log.getStackTraceString(e));
            handleNetworkError("网络异常");
        }finally {
            if (httpURLConnection != null){
                httpURLConnection.disconnect();
                httpURLConnection=null;
            }
        }
    }

    public void handleNetworkError(final String errorMsg) {
        if ((requestCallback != null)) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    HttpRequest.this.requestCallback.onFail(errorMsg);
                }
            });
        }
    }
}
