package com.wy.wx.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.wy.wx.model.VpnEmail;
import com.wy.wx.service.EmailService;
import com.wy.wx.util.SHA;
import com.wy.wx.weixin.model.TextMessage;
import com.wy.wx.weixin.util.MessageUtil;
import com.wy.wx.weixin.util.WeixinAccessTokenUtil;

import springfox.documentation.annotations.ApiIgnore;

/**
 * 微信公众号controller
 * @author xiongw
 *
 */
@Controller
@ApiIgnore  //忽略在swagger中显示
@RequestMapping("/")
public class WeiXinController {
	
	@Autowired
	private EmailService emailService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@ResponseBody
	@RequestMapping("/")
    public String index() {
        return "微信开发中……";
    }
	/**
	 * 微信第一次接口验证
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("getInfo")
	public void resultDataInfo(HttpServletRequest request ,HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
	 	String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		
		String method = request.getMethod();
		System.out.println(method+"请求,微信发送验证信息：["+signature+"], ["+timestamp+"],["+nonce +"],"+nonce+","+echostr);
		
		PrintWriter pw  = response.getWriter();
		if(checkRequest(signature, timestamp, nonce)){
			pw.print(sendInfo(request,response));
			System.out.println("微信验证成功");
		} 
	}
	
	@ResponseBody
	@RequestMapping("/getUser")
	public String resultWxUserInfo(HttpServletRequest request , HttpServletResponse response) throws IOException{
		String access_token = WeixinAccessTokenUtil.getStrToken();
		String openid = request.getParameter("openid");
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+access_token+"&openid="+openid+"&lang=zh_CN";
		JSONObject jsonObject = WeixinAccessTokenUtil.doGetStr(url);
		return jsonObject.toJSONString();
	}	
	/**
	 * 消息处理主程序
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String sendInfo(HttpServletRequest request ,HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		Map<String, String> map = MessageUtil.xmlToMap(request);
	 	String toUserName = map.get("ToUserName");
		String fromUserName = map.get("FromUserName");
		String createTime = map.get("CreateTime");
		String msgType = map.get("MsgType");
		String content = map.get("Content");
		String msgId = map.get("MsgId");
		String msg = null;
		
		
		if(MessageUtil.MESSAGE_TEXT.equals(msgType)){//文本处理
			
			if("1".equals(content)){
				return msg = MessageUtil.initText(toUserName, fromUserName, MessageUtil.resultInfo1());
			}else if("2".equals(content)){
				return msg = MessageUtil.initNewsMessage(toUserName, fromUserName, MessageUtil.resultInfo2());
			}else if("?".equals(content) || "？".contains(content)){
				return msg = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
			}else if("v".equals(content) || "V".equals(content)){
				StringBuffer sb = new StringBuffer();
				List<VpnEmail> list = emailService.queryAll();
				for(VpnEmail vp :list){
					sb.append(vp.getEmail()+"\n");
				}
				return msg = MessageUtil.initText(toUserName, fromUserName,sb.toString());
			}
			
			TextMessage text = new TextMessage();
			text.setFromUserName(toUserName);
			text.setToUserName(fromUserName);
			text.setMsgType("text");
			text.setCreateTime(sdf.format(new Date()));
			text.setContent("[欢迎开启公众号开发者模式]你发送的消息："+content+","+sdf.format(new Date()));
			msg = MessageUtil.textMessageToXml(text);
		
		
		}else if(MessageUtil.MESSAGE_EVENT.equals(msgType)){//关注事件
			
			String eventType = map.get("Event");
			System.out.println(eventType);
			if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){
				msg = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
			}
			
		}
		System.out.println(msg);
		return msg;
	}
	
	
	
	
	 
	
	
	
	
	
	
	
	
	/**
	 * 验证检查
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	private boolean checkRequest(String signature ,String timestamp ,String nonce ){
		StringBuffer content = new StringBuffer();
	 String [] arr = new String[]{"openwan",timestamp,nonce};
		//排序
		Arrays.sort(arr);
		for(int i = 0 ; i<arr.length ; i++){
		 content.append(arr[i]);
		} 
		//加密
		String  temp = SHA.encode(content.toString());
		return temp.equals(signature); 
	}

}
