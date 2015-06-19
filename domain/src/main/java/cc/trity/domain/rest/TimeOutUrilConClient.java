package cc.trity.domain.rest;

import java.io.IOException;
import java.net.HttpURLConnection;

import retrofit.client.Request;
import retrofit.client.UrlConnectionClient;

/**设置retrofit请求timeout的配置
 * Created by TryIT on 2015/5/22.
 */
public class TimeOutUrilConClient extends UrlConnectionClient {
    @Override
    protected HttpURLConnection openConnection(Request request) throws IOException {//抛出异常，给上一层进行处理
        HttpURLConnection connection= super.openConnection(request);
        connection.setConnectTimeout(15 * 1000);//设置请求连接时间
        connection.setReadTimeout(30 * 1000);//设置请求读取时间
        return connection;
    }
}
