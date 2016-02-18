package cc.trity.library.net;

/**
 * Created by TryIT on 2016/2/4.
 */
public class URLData {
    private String key;//用于不同类型的请求
    private long expires;//缓存设计，缓存多久时间
    private String netType;//get 还是post请求
    private String url;//连接

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
