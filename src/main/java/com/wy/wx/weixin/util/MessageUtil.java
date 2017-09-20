package com.wy.wx.weixin.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import com.wy.wx.weixin.model.News;
import com.wy.wx.weixin.model.NewsMessage;
import com.wy.wx.weixin.model.TextMessage;
import com.wy.wx.weixin.model.button.Button;
import com.wy.wx.weixin.model.button.ClickButton;
import com.wy.wx.weixin.model.button.Menu;
import com.wy.wx.weixin.model.button.ViewButton;
import com.thoughtworks.xstream.XStream;

/**
 * 消息处理类
 * @author xiongw
 *
 */
public class MessageUtil {
	
	/**
	 * 文本类型
	 */
	public static final String MESSAGE_TEXT = "text";
	public static final String MESSAGE_NEWS = "news";
	public static final String MESSAGE_IMAGE ="image";
	public static final String MESSAGE_VOICE ="voice";
	public static final String MESSAGE_VIDEO ="video";
	public static final String MESSAGE_LINK ="link";
	public static final String MESSAGE_LOCATION ="location";
	public static final String MESSAGE_EVENT ="event";
	public static final String MESSAGE_SUBSCRIBE ="subscribe";
	public static final String MESSAGE_UNSUBSCRIBE ="unsubscribe";
	public static final String MESSAGE_CLICK ="CLICK";
	public static final String MESSAGE_VIEW ="VIEW";
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	

	/**
	 * xml 转 map
	 * @param req
	 * @return
	 */
	public static Map<String, String> xmlToMap(HttpServletRequest req) {

		Map<String, String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();
		InputStream ins = null;
		Document doc = null;
		Element root = null;
		try {
			ins = req.getInputStream();
			doc = reader.read(ins);
			root = doc.getRootElement();
			List<Element> list = root.elements();
			for(Element e : list){
				map.put(e.getName(), e.getText());
			}
		} catch (IOException e) {
			try {
				ins.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (DocumentException e) {
			try {
				ins.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return map;
	}

	//-----------------------------------文本信息转化----------------------------------------------//
	
	/**
	 * 文本信息转化xml
	 * @param textMeasurer
	 * @return
	 */
	public static String textMessageToXml(TextMessage textMeasurer){
		
		XStream xs = new XStream();
		xs.alias("xml", textMeasurer.getClass());
		return xs.toXML(textMeasurer);
	}
	
	
	//---------------------------------------------------------------------------------//
	
	
	//------------------------------------文图信息---------------------------------------------//
	/**
	 * 文图信息转化xml
	 * @param textMeasurer
	 * @return
	 */
	public static String newsMessageToXml(NewsMessage newsMessage){
		
		XStream xs = new XStream();
		xs.alias("xml", newsMessage.getClass());
		xs.alias("item", new News().getClass());
		return xs.toXML(newsMessage);
	}
	
	/**
	 * 图文消息组装
	 * @param toUserName
	 * @param fromUserName
	 * @param content
	 * @return
	 */
	public static String initNewsMessage(String toUserName,String fromUserName,String content){
		String message = null ;
		List<News> newsList = new ArrayList<News>();
		NewsMessage nm = new NewsMessage();
	
		
//		List<DataInfo> df = JUhe.xiaoHuaData();
//		
//		for(int i=0 ;i <5 ; i++){
//			News  n = new News();
//			n.setTitle(df.get(i).getContent());
//			n.setPicUrl(df.get(i).getUrl());
//			n.setUrl(df.get(i).getUrl());
//			n.setDescription("天凉了！天凉了！出差一个人住酒店，半夜电话响了，是一个男孩的声音：“秋夜漫漫，是不是感到空虚寂寞冷？提供上门服务哦。”我鼓足勇气说：“好的。”没多久就听到了敲门声，打开门果然是个帅气小鲜肉，一边从随身的包里掏东西一边说：“只要88元，100%全棉秋裤，买一件吧，穿上就不冷了。”剑神葡萄）");
//			newsList.add(n);
//		}
//		
//		
		News  n1 = new News();
//		n1.setTitle("秋夜漫漫，是不是感到空虚寂寞冷？提供上门服务哦");
//		n1.setPicUrl("http://openwan.cn/assets/img/backgrounds/2.jpg");
//		n1.setUrl("www.wy.wx.cn");
//		n1.setDescription("天凉了！天凉了！出差一个人住酒店，半夜电话响了，是一个男孩的声音：“秋夜漫漫，是不是感到空虚寂寞冷？提供上门服务哦。”我鼓足勇气说：“好的。”没多久就听到了敲门声，打开门果然是个帅气小鲜肉，一边从随身的包里掏东西一边说：“只要88元，100%全棉秋裤，买一件吧，穿上就不冷了。”剑神葡萄）");
//		
		
		
		newsList.add(n1);
		nm.setToUserName(fromUserName);
		nm.setFromUserName(toUserName);
		nm.setCreateTime(sdf.format(new Date()));
		nm.setMsgType(MESSAGE_NEWS);
		nm.setArticles(newsList);
		nm.setArticleCount(newsList.size());
		
		message = newsMessageToXml(nm);
		
		return message;
	}
	
	
	//---------------------------------------------------------------------------------//
	
	
	
	
	/** 
	 * return 主菜单
	 * @param toUserName
	 * @param fromUserName
	 * @param content
	 * @return
	 */
	public static String initText (String toUserName,String fromUserName,String content){
		TextMessage text = new TextMessage();
		text.setFromUserName(toUserName);
		text.setToUserName(fromUserName);
		text.setCreateTime(sdf.format(new Date()));
		text.setMsgType(MESSAGE_TEXT);
		text.setContent(content );
		return  MessageUtil.textMessageToXml(text);
	}
	
	
	/**
	 * 关注菜单
	 * @return
	 */
	public static String menuText(){
		StringBuffer sb = new StringBuffer();
		sb.append("欢迎关注OpenWan公众号\n");
		sb.append("菜单提示：\n");
		sb.append("1.介绍\n");
		sb.append("2.唠嗑\n");
		sb.append("3.what ？\n");
		return sb.toString();
	}
	
	/**
	 * 菜单1
	 * @return
	 */
	public static String resultInfo1(){
		StringBuffer sb = new StringBuffer();
		sb.append("我开始的减肥了看似简单福利局");
		return sb.toString();
	}
	
	/**
	 * 菜单2
	 * @return
	 */
	public static String resultInfo2(){
		StringBuffer sb = new StringBuffer();
		sb.append("和我来辣烤");
		return sb.toString();
	}
	
	/**
	 * 组装按钮菜单
	 * @return
	 */
	public static Menu initMenu(){
		Menu menu = new Menu();
		ClickButton clickButton = new ClickButton();
		
		clickButton.setName("click菜单");
		clickButton.setKey("11");
		clickButton.setType("click");
		
		ViewButton viewButton = new ViewButton();
		
		viewButton.setName("view菜单");
		viewButton.setType("view");
		viewButton.setUrl("http://www.wy.wx.cn");
		
		ClickButton clickButton2 = new ClickButton();
		
		clickButton2.setName("扫码事件");
		clickButton2.setKey("22");
		clickButton2.setType("scancode_push");
		
		ClickButton clickButton3 = new ClickButton();
		
		clickButton3.setName("地理位置");
		clickButton3.setKey("33");
		clickButton3.setType("location_select");
		
		Button button = new Button();
		button.setName("菜单");
		button.setSub_button(new Button[]{clickButton2,clickButton3});
		
		menu.setButton(new Button[]{clickButton,viewButton,button});
		return  menu;
	}
	
}
