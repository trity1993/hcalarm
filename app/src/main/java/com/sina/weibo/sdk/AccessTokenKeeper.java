package com.sina.weibo.sdk;

import android.content.Context;
import android.content.SharedPreferences;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import cc.trity.model.entities.Authentication;

/**
 * 该类定义了微博授权时所需要的参数。
 *
 * @author SINA
 * @since 2013-10-07
 */
public class AccessTokenKeeper {
    private static final String PREFERENCES_NAME = "com_weibo_sdk_android";

    private static final String KEY_UID           = "uid";
    private static final String KEY_ACCESS_TOKEN  = "access_token";
    private static final String KEY_EXPIRES_IN    = "expires_in";
    private static final String KEY_REFRESH_TOKEN    = "refresh_token";

    /**
     * 保存 Token 对象到 SharedPreferences。
     *
     * @param context 应用程序上下文环境
     * @param token   Token 对象
     */
    public static Authentication writeAccessToken(Context context, Oauth2AccessToken token) {
        if (null == context || null == token) {
            return null;
        }
        Authentication authentication=new Authentication();
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(KEY_UID, token.getUid());
        authentication.setOpenid(token.getUid());

        editor.putString(KEY_ACCESS_TOKEN, token.getToken());
        authentication.setAccess_token(token.getToken());

        editor.putString(KEY_REFRESH_TOKEN, token.getRefreshToken());//在有效期内，再次打开授权，会自动延长授权的有效时间
        authentication.setFresh_access_token(token.getRefreshToken());
        long start=System.currentTimeMillis();
        authentication.setExpires_in("" + start + token.getExpiresTime());
        editor.putLong(KEY_EXPIRES_IN, start + token.getExpiresTime());//过期时间 = 用户授权时间 + 授权有效期；审核通过的时候为7天，这里要做响应的运算
        editor.commit();

        return authentication;
    }

    /**
     * 从 SharedPreferences 读取 Token 信息。
     *
     * @param context 应用程序上下文环境
     *
     * @return 返回 Token 对象
     */
    public static Oauth2AccessToken readAccessToken(Context context) {
        if (null == context) {
            return null;
        }

        Oauth2AccessToken token = new Oauth2AccessToken();
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        token.setUid(pref.getString(KEY_UID, ""));
        token.setToken(pref.getString(KEY_ACCESS_TOKEN, ""));
        token.setRefreshToken(pref.getString(KEY_REFRESH_TOKEN, ""));
        token.setExpiresTime(pref.getLong(KEY_EXPIRES_IN, 0));

        return token;
    }

    /**
     * 清空 SharedPreferences 中 Token信息。
     *
     * @param context 应用程序上下文环境
     */
    public static void clear(Context context) {
        if (null == context) {
            return;
        }

        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = pref.edit();
        //这里的配置不同，不用担心闹钟方面的也被清楚
        editor.clear();
        editor.commit();
    }
}