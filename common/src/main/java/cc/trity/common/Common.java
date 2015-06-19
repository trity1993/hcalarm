package cc.trity.common;

public class Common {
    //服务端
    public static final String API_URL ="http://entersgu.hclab.cn/entersgu/Handler/";
    //腾讯秘钥
    public static final String TENCENT_APP_ID="1104698782";//1104698782,222222
    public static final String TENCENT_APP_SECRET="BE4H8noJNntQX6Ny";
    public static final String TENCENT_GET_PERMISSION="get_user_info,add_t";
    //微博
    public static final String WEIBO_APP_ID="1276595402";//1276595402,测试：2045436852
    public static final String WEIBO_TAG_PHONE="weibo_phone";
    public static final String WEIBO_TAG_ALL="weibo_all";
    /*
    建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     */
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     *
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     *
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     *
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     * 以下大部分接口都是要进行申请的。可以使用qq进行互补。
     */
    public static final String SCOPE ="email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
    //微博具体接口获取
    //根据ID获取用户信息
    public static final String WEIBO_USER_SHOW_URL="https://api.weibo.com/2/";
    // 有米的秘钥
    public static final String YOU_MI_APP_ID="51cfb919563a13f8";
    public static final String YOU_MI_APP_SECRET="2253e375f73df580";

    public static final String KEY_FLARM_LIST = "alarmlist"; // 标示闹钟列表的存储
    public static final String ALARM_ID = "alarmID"; // 标示闹钟列表的存储
    public static final String CONFIG = "config";
    public static final String KEY_USERINFO = "userinfo";
    public static final String KEY_ADV = "adv";
    public static final String USERINFO_REGISTER = "USER_REG";
    public static final String USERINFO_LOGIN = "USER_LOGIN";
    public static final String YOUR_COMMAND = "YOUR_COMMAND";
    public static final String ADD_WAKEUP = "ADD_WAKEUP";
    public static final String GET_BEST_RECORD_NO = "GET_BEST_RECORD_NO";
    public static final String GET_BEST_RECORD = "GET_BEST_RECORD";
    public static final String GET_RANK = "GET_RANK";
    public static final String ADD_FRIEND = "ADD_FRIEND";
    public static final String ALARM_MSG = "ALARMMSG";
    public static final String LAUNCHER_COUNT = "LAUNCHER_COUNT";
    public static final int STATE_SUCCESS = 1;
    public static final int STATE_FAILURE = 0;
    public static final long DOUBLE_CLICK_INTERVAL = 2000;
    public static final int BITMAP_RADIUS = 100;
    public static final int TENCENT_LOGIN = 1;
    public static final int WEIBO_LOGIN = 2;
    public static final int WEIBO_MSM_LOGIN = 3;
    public static final int SHAKE_NUMBER = 100;
    public static final int SHAKE_SENSE_VALUE_THREE = 17;
    public static final int SHAKE_SENSE_VALUE_TWO = 13;
    public static final int SHAKE_SENSE_VALUE_ONE = 11;
}
