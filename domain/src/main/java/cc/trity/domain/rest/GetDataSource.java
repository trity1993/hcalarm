package cc.trity.domain.rest;

import retrofit.RestAdapter;

/**
 * Created by TryIT on 2015/6/11.
 */
public class GetDataSource<I,O> {
    private cc.trity.domain.rest.UriApi<I, O> uriApi;
    private static GetDataSource getDataSource;
    private GetDataSource(){//默认使用私有的构造函数
//        RestAdapter restAdapter=new RestAdapter.Builder()
//                .setEndpoint(Common.API_URL).setClient(new TimeOutUrilConClient()).setLogLevel(RestAdapter.LogLevel.FULL)
//                .build();
//        uriApi=restAdapter.create(cc.trity.domain.rest.UriApi.class);
    }
    public GetDataSource(String API_URL){
        RestAdapter restAdapter=new RestAdapter.Builder()
                .setEndpoint(API_URL).setClient(new TimeOutUrilConClient()).setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        uriApi=restAdapter.create(cc.trity.domain.rest.UriApi.class);
    }
    public static GetDataSource getInstance() {

        if (getDataSource == null)
            getDataSource = new GetDataSource();

        return getDataSource;
    }
    //得到单个数据的时候
    public O getData(I map,String path){
        return uriApi.getMutiData(path,map);
    }
}
