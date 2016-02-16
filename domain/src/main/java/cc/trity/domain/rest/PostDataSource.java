package cc.trity.domain.rest;

import retrofit.RestAdapter;

/**
 * Created by TryIT on 2015/6/11.
 */
public class PostDataSource<I,O> {
    private cc.trity.domain.rest.UriApi<I, O> uriApi;
    private static PostDataSource postDatabase;
    private PostDataSource(){//默认使用私有的构造函数，使用自家的url进行测试
//        RestAdapter restAdapter=new RestAdapter.Builder()
//                .setEndpoint(Common.API_URL).setClient(new TimeOutUrilConClient()).setLogLevel(RestAdapter.LogLevel.FULL)
//                .build();
//        uriApi=restAdapter.create(cc.trity.domain.rest.UriApi.class);
    }
    private PostDataSource(String API_URL){
        RestAdapter restAdapter=new RestAdapter.Builder()
                .setEndpoint(API_URL).setClient(new TimeOutUrilConClient()).setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        uriApi=restAdapter.create(cc.trity.domain.rest.UriApi.class);
    }
    public static PostDataSource getInstance() {

        if (postDatabase == null)
            postDatabase = new PostDataSource();

        return postDatabase;
    }
    public O postData(I i,String path){
        return uriApi.postData(path,i);
    }
}
