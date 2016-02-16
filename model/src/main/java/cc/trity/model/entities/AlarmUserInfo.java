package cc.trity.model.entities;
/*
 * 存储用户数据
 */
public class AlarmUserInfo {
    /*
     * 电话，密码，昵称，性别，头像，签名，次数
     */
    private String phoneNum;
    private String password;
    private String nickName;
    private String sex;
    private String head;//头像的ImageUrl地址
    private String says;
    private String num;
    private Authentication authentication;//存储验证的信息

    public AlarmUserInfo() {

    }

    public AlarmUserInfo(String phoneNum, String password) {
        this.password=password;
        this.phoneNum=phoneNum;

    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getSays() {
        return says;
    }

    public void setSays(String says) {
        this.says = says;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }
}
