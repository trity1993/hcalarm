package cc.trity.model.entities;

/**openid access_token expires_in用来保证对其他权限的获取
 * Created by TryIT on 2015/6/11.
 */
public class Authentication {
    private String openid;
    private String access_token;
    private String fresh_access_token;//微博独有的，可延长有效期，认证的时候需要
    private String expires_in;
    public Authentication(){}

    public Authentication( String openid,String access_token,String expires_in){
        this.openid=openid;
        this.access_token=access_token;
        this.expires_in=expires_in;
    }
    public Authentication( String openid,String access_token,String expires_in,String fresh_access_token){
        this.openid=openid;
        this.access_token=access_token;
        this.expires_in=expires_in;
        this.fresh_access_token=fresh_access_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getFresh_access_token() {
        return fresh_access_token;
    }

    public void setFresh_access_token(String fresh_access_token) {
        this.fresh_access_token = fresh_access_token;
    }
}
