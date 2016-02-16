package cc.trity.domain.rest;


import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Created by TryIT on 2015/5/18.
 * 使用泛型，统一URL
 * I 表示输入内容，参数等
 * O 表示输出内容，返回值。
 * 通过path 改变路径来执行不同的操作。
 */
public interface UriApi<I,O> {

    @POST("/{path}")
    O postData(@Path("path") String path, @Body I i);

    @POST("/{path}")
    void postData(@Path("path") String path, @Body I i, Callback<O> callback);

    @GET("/{path}")
    O getMutiData(@Path("path") String path, @QueryMap I options);//使用get方法，只能传Map集合

    @GET("/{path}")
    void getMutiData(@Path("path") String path, @QueryMap Map<I, I> options, Callback<O> callback);


}
