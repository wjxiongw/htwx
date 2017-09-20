package com.wy.wx.util;

public class UserInfoUtil {
	 
	//获取code的请求地址
    public static String Get_Code = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=STAT#wechat_redirect";
    //替换字符串
    public static String getCode(String APPID, String REDIRECT_URI,String SCOPE) {
        return String.format(Get_Code,APPID,REDIRECT_URI,SCOPE);
    }
 
    //获取Web_access_tokenhttps的请求地址
    public static String Web_access_tokenhttps = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    //替换字符串
    public static String getWebAccess(String APPID, String SECRET,String CODE) {
        return String.format(Web_access_tokenhttps, APPID, SECRET,CODE);
    }
 
//    public static void main(String[] args) {
//        String  REDIRECT_URI = "https://qiangqiangchen.55555.io/vote.do";
//        String SCOPE = "snsapi_userinfo";
//        //appId
//        String appId = "wx69b8accef39ebb40";
// 
//        String getCodeUrl = getCode(appId, REDIRECT_URI, SCOPE);
//        System.out.println("getCodeUrl:"+getCodeUrl);
//    }
    
  
    
}