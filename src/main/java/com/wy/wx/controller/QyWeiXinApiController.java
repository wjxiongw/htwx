package com.wy.wx.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.tools.javac.util.Log;
import com.wy.basic.annotations.AdminAuth;
import com.wy.basic.annotations.Token;
import com.wy.basic.exception.SystemException;
import com.wy.basic.model.User;
import com.wy.basic.tools.SecurityUtil;
import com.wy.basic.tools.TokenTools;
import com.wy.basic.utils.PageableUtil;
import com.wy.basic.utils.ParamFilterUtil;
import com.wy.wx.config.QyWeixinConfig;
import com.wy.wx.model.QyMsg;
import com.wy.wx.model.WxUser;
import com.wy.wx.service.WxUserService;
import com.wy.wx.util.SHA;
import com.wy.wx.weixin.model.TextMessage;
import com.wy.wx.weixin.util.CHexConver;
import com.wy.wx.weixin.util.MessageUtil;
import com.wy.wx.weixin.util.QyWeixinUtil;
import com.wy.wx.weixin.util.WeixinAccessTokenUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

/**
 * 企业微信公众号controller
 * @author xiongw
 *
 */
@RestController
@RequestMapping(value="/qyuser")
public class QyWeiXinApiController {
	private static Logger log = Logger.getLogger(QyWeiXinApiController.class);
	@Autowired
    private WxUserService service;
	
	@ApiOperation(value="获取指定企业号用户详细信息", notes="根据userid来获取用户详细信息")
    @ApiImplicitParam(paramType="query", name = "userid", value = "企业号ID", required = true, dataType = "String")
	@RequestMapping(value = "/getUser",method={RequestMethod.GET})
    public JSONObject getUser(HttpServletRequest req)throws Exception{
    	String userid = req.getParameter("userid");
		log.info("userid:"+userid);
		String accessToken=QyWeixinUtil.getQyAccessToken();
		log.info("userid:"+userid+"=========accessToken:"+accessToken);
		String url=QyWeixinConfig.READ_USRE_URL.replace("ACCESS_TOKEN", accessToken).replace("USERID", userid);
		return QyWeixinUtil.doGetStr(url);
    }
	
	//paramType 有五个可选值 ： path, query, body, header, form
	@ApiOperation(value="获取企业号用户id详细信息", notes="根据url的userid来获取用户详细信息")
    @ApiImplicitParam(paramType = "path",name = "userid", value = "企业用户ID", required = true, dataType = "String")
    @RequestMapping(value="/{userid}", method=RequestMethod.GET)
	public JSONObject getUser(@PathVariable String userid)throws Exception{
		String accessToken=QyWeixinUtil.getQyAccessToken();
		log.info("userid:"+userid+"=========accessToken:"+accessToken);
		String url=QyWeixinConfig.READ_USRE_URL.replace("ACCESS_TOKEN", accessToken).replace("USERID", userid);
		return QyWeixinUtil.doGetStr(url);
    }
	
	
	@ApiOperation(value="发送文本消息", notes="对指定用户发送文本消息")
	@ApiModelProperty(position = 1, dataType="java.util.List<String>", example = "PRD1, PRD2, PRD3")
	@ApiImplicitParams( value = {
	        @ApiImplicitParam(paramType = "query", name = "msgType", dataType = "String", required = true, value = "消息类型", defaultValue = "text"),
	        @ApiImplicitParam(paramType = "query", name = "userid", dataType = "String", required = true, value = "企业用户ID"),
	        @ApiImplicitParam(paramType = "query", name = "content", dataType = "String", required = true, value = "发送内容")
	})
	@RequestMapping(value="sendMsg",method = RequestMethod.POST)
	public JSONObject sendTxt(@RequestParam("userid") String userid,@RequestParam("msgType") String msgType,
			@RequestParam("content") String content)throws Exception{
		String accessToken=QyWeixinUtil.getQyAccessToken();
		String url=QyWeixinConfig.SEND_MSG_URL.replace("ACCESS_TOKEN", accessToken);
		JSONObject js = new JSONObject();
		js.put("touser", userid);
		js.put("msgtype", msgType);
		js.put("agentid", QyWeixinConfig.AGENTID);
		JSONObject txt = new JSONObject();
		txt.put("content", content);
		js.put("text",txt);
		log.info("发送内容:"+js.toString());
		JSONObject errJs= QyWeixinUtil.doPostStr(url, js.toString());
		if(errJs.getString("errcode").equals("40014")){// 重新获取access_token然后再发送
			accessToken=QyWeixinUtil.getQyAccessToken();
			url=QyWeixinConfig.SEND_MSG_URL.replace("ACCESS_TOKEN", accessToken);
			errJs = QyWeixinUtil.doPostStr(url, js.toString());	
		}
		return errJs;
    }
	
	@ApiOperation(value="根据js发送各类消息", notes="js发送消息")
    @ApiImplicitParam(name = "msg", value = "企业消息", required = true, dataType = "QyMsg")
    @RequestMapping(value="jsMsg", method=RequestMethod.POST)
    public JSONObject postUser(@RequestBody QyMsg msg)throws Exception{
		log.info("收到发送信息："+msg.getMsgType());
		JSONObject errJs = new JSONObject();		
		String msgType=msg.getMsgType();
		if(msgType==null || !(msgType.indexOf("text")>-1 
				|| msgType.indexOf("textcard")>-1 || msgType.indexOf("news")>-1)){
			errJs.put("errcode", "-1");
			errJs.put("errmsg", "消息类型只允许发送文本（text）、文本卡片（textcard）和单条图文(news)");
			return errJs;
		}
		
		String accessToken=QyWeixinUtil.getQyAccessToken();
		String url=QyWeixinConfig.SEND_MSG_URL.replace("ACCESS_TOKEN", accessToken);
		if("text".equals(msgType)){//文本处理	
			int f = 2048; //微信文本消息最多只能发 2048个字节长度内容
			String sendTxt=msg.getContent();
			String subStr=null;
			do{
				subStr = CHexConver.idgui(sendTxt,f);
				JSONObject js = new JSONObject();
				js.put("touser", msg.getUsers());
				js.put("totag", msg.getTags());
				js.put("msgtype", msgType);
				js.put("agentid", msg.getAgentId());
				JSONObject txt = new JSONObject();
				txt.put("content", subStr);
				js.put("text",txt);
				log.info("发送内容:"+js.toString());
				errJs = QyWeixinUtil.doPostStr(url, js.toString());	
				if(errJs.getString("errcode").equals("40014")){// 重新获取access_token然后再发送
					accessToken=QyWeixinUtil.getQyAccessToken();
					url=QyWeixinConfig.SEND_MSG_URL.replace("ACCESS_TOKEN", accessToken);
					errJs = QyWeixinUtil.doPostStr(url, js.toString());	
				}					
				sendTxt = sendTxt.substring(subStr.length());	
			}while(sendTxt.length()>0);			
		}else if("news".equals(msgType)){
			JSONObject js = new JSONObject();
			js.put("touser", msg.getUsers());
			js.put("totag", msg.getTags());
			js.put("msgtype", msgType);
			js.put("agentid", msg.getAgentId());	
			JSONArray na =new JSONArray();
			JSONObject j = new JSONObject();
			j.put("title", msg.getTitle());
			j.put("description", msg.getDescription());
			j.put("url", msg.getLinkUrl());
			j.put("picurl", msg.getMedPicUrl());
			JSONObject jsnews = new JSONObject();
			na.add(j);
			jsnews.put("articles", na);
			js.put("news", jsnews);
			log.info("发送内容:"+js.toString());
			errJs = QyWeixinUtil.doPostStr(url, js.toString());	
			if(errJs.getString("errcode").equals("40014")){// 重新获取access_token然后再发送
				accessToken=QyWeixinUtil.getQyAccessToken();
				url=QyWeixinConfig.SEND_MSG_URL.replace("ACCESS_TOKEN", accessToken);
				errJs = QyWeixinUtil.doPostStr(url, js.toString());	
			}
		}else if("textcard".equals(msgType)){ //卡片消息的展现形式非常灵活，支持使用br标签或者空格来进行换行处理，也支持使用div标签来使用不同的字体颜色，目前内置了3种文字颜色：灰色(gray)、高亮(highlight)、默认黑色(normal)，将其作为div标签的class属性即可，具体用法请参考上面的示例。
			JSONObject js = new JSONObject();
			js.put("touser", msg.getUsers());
			js.put("totag", msg.getTags());
			js.put("msgtype", msgType);
			js.put("agentid", msg.getAgentId());	
			JSONObject j = new JSONObject();
			j.put("title", msg.getTitle());
			j.put("description", msg.getDescription());
			j.put("url", msg.getLinkUrl());
			js.put("textcard", j);
			log.info("发送内容:"+js.toString());
			errJs = QyWeixinUtil.doPostStr(url, js.toString());	
			if(errJs.getString("errcode").equals("40014")){// 重新获取access_token然后再发送
				accessToken=QyWeixinUtil.getQyAccessToken();
				url=QyWeixinConfig.SEND_MSG_URL.replace("ACCESS_TOKEN", accessToken);
				errJs = QyWeixinUtil.doPostStr(url, js.toString());	
			}
		}
		return errJs;
    }
	
	@ApiOperation(value="获取所有用户列表", notes="")
    @RequestMapping(value={""}, method= RequestMethod.GET)
    public List<WxUser> getUserList() {
    	List<WxUser> list = service.getListWxUsers();
        return list;
    }

	
}
