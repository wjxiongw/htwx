package com.wy.wx.config;
/**企业号相关配置文件信息
 * 
 * @author wy
 *
 */
public class QyWeixinConfig {
	//门店互动平台
	public static final String CORPID = "wx19d8777d071af48a";
	public static final String AGENTID = "0";
	public static final String SECRET = "T0IkUMgjp33-sQ4zwPl08M9Yqw51-wSsKVlgju3Co3-J6YoLURBKUTFDZjbhBCa5";
	//获取access_token
	public static final String ACCESS_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+CORPID+"&corpsecret="+SECRET;
	//读取成员
	public static final String READ_USRE_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&userid=USERID";
	//发送消息
	public static final String SEND_MSG_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=ACCESS_TOKEN";
	//发送图片
	public static final String SEND_PIC_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=ACCESS_TOKEN";
}
