package com.wy.wx.weixin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.wy.wx.config.QyWeixinConfig;
import com.wy.wx.weixin.model.AccessToken;

/**
 * 获取企业号票据access_token * 
 * 思路：将access_token以文件形势保存到服务端
 * @author xiongw
 *
 */
public class QyWeixinUtil {
	//文件路径
	private static final String PATH_STR = QyWeixinUtil.class.getClass().getResource("/qytoken.properties").getPath();
	/**
	 * 获取access_token 字符串
	 * @throws IOException
	 */
	public static String getQyAccessToken() {
		String access_token=null;
		Properties propFlies = new Properties();
		String path = ifWinLinux(PATH_STR);
		InputStream is = null;
		OutputStream os = null;
		try {
			//根据文件时间判断是否要重新获取accessToken
			File f= new File(path);
			if(f.exists() && f.length()>0){
				Date curDate = new Date();
		        Date fdta = new Date(f.lastModified());		        
		        if(fdta.getTime()<curDate.getTime()-7000*1000){//微信AccessToken效期7200秒，此处以7000秒判断
		        	String url = QyWeixinConfig.ACCESS_TOKEN_URL;
		    		JSONObject jsonObject = doGetStr(url);
		    		if (jsonObject != null && jsonObject.getString("errcode").equals("0")) {
		    			access_token = jsonObject.getString("access_token");
		    			// 写入文件
						os = new FileOutputStream(new File(path));
						propFlies.setProperty("access_token", access_token);
						propFlies.store(os, "");
						os.flush();
						os.close();
		    		}					
		        }else {
		        	is = new FileInputStream(new File(path));
					propFlies.load(is);
					Map map = (Map) propFlies;
					access_token = (String) map.get("access_token");
					is.close();
		        }
			}else {
				String url = QyWeixinConfig.ACCESS_TOKEN_URL;
	    		JSONObject jsonObject = doGetStr(url);
	    		if (jsonObject != null && jsonObject.getString("errcode").equals("0")) {
	    			access_token = jsonObject.getString("access_token");
	    			// 写入文件
					os = new FileOutputStream(new File(path));
					propFlies.setProperty("access_token", access_token);
					propFlies.store(os, "");
					os.flush();
					os.close();
	    		}				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null)
					os.close();
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return access_token;
	}
	
	

	/**
	 * get请求
	 * 企业微信的所有接口都会返回errmsg和errcode形势的JSON字符串
	 * @param url	 * 
	 */
	public static JSONObject doGetStr(String url)
			throws ClientProtocolException, IOException {
		url = getHtUrl(url);
		JSONObject jsonObject = null;
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = httpclient.execute(httpGet);
		HttpEntity httpEntity = response.getEntity();
		if (httpEntity != null) {
			String result = EntityUtils.toString(httpEntity, "UTF-8");
			jsonObject = JSONObject.parseObject(result);
		}
		return jsonObject;
	}

	/**
	 * post请求
	 * 
	 * @param url
	 * @param outStr参数 如：json对象、
	 */
	public static JSONObject doPostStr(String url, String outStr)
			throws ClientProtocolException, IOException {
		url = getHtUrl(url);
		JSONObject jsonObject = null;
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost hp = new HttpPost(url);
		hp.setEntity(new StringEntity(outStr, "UTF-8"));
		HttpResponse response = httpclient.execute(hp);
		if(response.getEntity()!=null) {
			String result = EntityUtils.toString(response.getEntity(), "UTF-8");
			jsonObject = JSONObject.parseObject(result);
		}
		return jsonObject;
	}

	/**
	 * ture windows false linux
	 * 返回响应的存储路径
	 * @return
	 */
	public static String ifWinLinux(String path_url) {
		String path = null;
		String os = System.getProperty("os.name");
		if (os.toLowerCase().startsWith("win")) {
			path = path_url.replace("%20", " ").replace("target/classes/","src/main/resources/");
			path = path.substring(1, path.length());
		} else {
			path = path_url.replace("target/classes/", "src/main/resources/");
		}
		return path;
	}
	
	/**
	 * 在华泰内网访问外网，要么用代理要么转成内网访问
	 * @param reqURL
	 * @return
	 */
	public static String getHtUrl(String reqURL){
//		if(reqURL.indexOf("https://api.weixin.qq.com/cgi-bin")>-1){
//    		reqURL=reqURL.replace("https://api.weixin.qq.com/cgi-bin","http://10.2.19.88:180/wxapi");
//    	}else if(reqURL.indexOf("https://mp.weixin.qq.com/cgi-bin")>-1){
//    		reqURL=reqURL.replace("https://mp.weixin.qq.com/cgi-bin","http://10.2.19.88:180/wxmp");
//    	}else if(reqURL.indexOf("mmbiz.qpic.cn")>-1){
//    		reqURL=reqURL.replace("mmbiz.qpic.cn","10.2.19.88:180");
//    	}else if(reqURL.indexOf("wx.qlogo.cn")>-1){
//    		reqURL=reqURL.replace("wx.qlogo.cn","10.2.19.88:180");
//    	}else if(reqURL.indexOf("file.api.weixin.qq.com/cgi-bin")>-1){
//    		reqURL=reqURL.replace("file.api.weixin.qq.com/cgi-bin","10.2.19.88:180/wxfile");
//    	}else if(reqURL.indexOf("https://qyapi.weixin.qq.com/cgi-bin")>-1){
//    		reqURL=reqURL.replace("https://qyapi.weixin.qq.com/cgi-bin","http://10.2.19.88:180/wxqyapi");
//    	}else if(reqURL.indexOf("http://shp.qpic.cn/bizmp")>-1){
//    		reqURL=reqURL.replace("http://shp.qpic.cn/bizmp","http://10.2.19.88:180/bizmp");
//    	}else if(reqURL.indexOf("http://open.weixin.qq.com")>-1){
//    		reqURL=reqURL.replace("http://open.weixin.qq.com","http://10.2.19.88:180/open");
//    	}else if(reqURL.indexOf("https://api.mch.weixin.qq.com/pay")>-1){
//    		reqURL=reqURL.replace("https://api.mch.weixin.qq.com/pay","http://10.2.19.88:180/pay");
//    	}else if(reqURL.indexOf("https://api.weixin.qq.com/sns")>-1){
//    		reqURL=reqURL.replace("https://api.weixin.qq.com/sns","http://10.2.19.88:180/sns");
//    	}
		return reqURL;
	}
}
