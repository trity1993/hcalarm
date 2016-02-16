package cc.trity.model.entities;

/**openid access_token expires_in用来保证对其他权限的获取
 * Created by TryIT on 2015/6/11.
 */
public class Authentication {
    private String openId;
    private String accessToken;
    private String freshAccessToken;//微博独有的，可延长有效期，认证的时候需要
    private String expiresIn;
    public Authentication(){}

    public Authentication( String openid,String access_token,String expires_in){
        this.openId=openid;
        this.accessToken=access_token;
        this.expiresIn=expires_in;
    }
    public Authentication( String openid,String access_token,String expires_in,String fresh_access_token){
        this.openId=openid;
        this.accessToken=access_token;
        this.expiresIn=expires_in;
        this.freshAccessToken=fresh_access_token;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getFreshAccessToken() {
        return freshAccessToken;
    }

    public void setFreshAccessToken(String freshAccessToken) {
        this.freshAccessToken = freshAccessToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }
}
