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
import com.wy.wx.weixin.model.AccessToken;

/**
 * 获取票据access_token * 
 * @author xiongw
 *
 */
public class WeixinAccessTokenUtil {

	private static final String APPID = "wx6cd4a9a5c94af773";
	private static final String APPSECRET = "1d9262d310ffc771ee6988cdddf501f2";

	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

	private static final String PATH_STR = WeixinAccessTokenUtil.class
			.getClass().getResource("/token.properties").getPath();

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * get请求
	 * 
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static JSONObject doGetStr(String url)
			throws ClientProtocolException, IOException {
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
	 * @param outStr
	 *            参数 如：json对象、
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static JSONObject doPostStr(String url, String outStr)
			throws ClientProtocolException, IOException {
		JSONObject jsonObject = null;
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost hp = new HttpPost(url);
		hp.setEntity(new StringEntity(outStr, "UTF-8"));
		HttpResponse response = httpclient.execute(hp);
		String result = EntityUtils.toString(response.getEntity(), "UTF-8");
		jsonObject = JSONObject.parseObject(result);
		return jsonObject;
	}

	/**
	 * 获取access_token
	 * 
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static AccessToken getAccessToken() throws ClientProtocolException,
			IOException {

		AccessToken token = new AccessToken();

		String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace(
				"APPSECRET", APPSECRET);

		JSONObject jsonObject = doGetStr(url);

		if (jsonObject != null) {
			token.setToken(jsonObject.getString("access_token"));
			token.setExpiresIn(jsonObject.getIntValue("expires_in"));

		}
		return token;
	}

	
	/**
	 * 获取access_token 字符串
	 * @throws IOException
	 */
	public static String getStrToken() {
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
		        	AccessToken atoken = getAccessToken();
		        	access_token = atoken.getToken();
					// 写入文件
					os = new FileOutputStream(new File(path));
					propFlies.setProperty("access_token", access_token);
					propFlies.store(os, "");
					os.flush();
					os.close();
		        }else {
		        	is = new FileInputStream(new File(path));
					propFlies.load(is);
					Map map = (Map) propFlies;
					access_token = (String) map.get("access_token");
					is.close();
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
	 * 发送post请求，准备创建菜单
	 * 
	 * @param token
	 * @param menu
	 * @return
	 */
	public static int createMenu(String token, String menu) {
		String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject json = null;
		int result = 0;
		try {
			json = doPostStr(url, menu);
			if (json != null) {
				result = json.getIntValue("errcode");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * 创建自定义菜单
	 */
	public static void send() {
		String menu = JSONObject.toJSONString(MessageUtil.initMenu());
		System.out.println("菜单对象 :" + menu);
		// 从文件中获取token
		String token = getToToken(null).get("access_token");
		int resultCode = 0;
		// 验证token
		if (token == null) {
			System.out.println("token 为空 ,重新获取");
			resultCode = WeixinAccessTokenUtil.createMenu(
					timeOutNewsGetToken(), menu);// 重新调用
		}

		if (token != null) {
			// 发送post 请求，创建菜单
			resultCode = WeixinAccessTokenUtil.createMenu(token, menu);
			if (resultCode == 40001) {// token失效
				System.out.println("access_token 失效从新获取");
				resultCode = WeixinAccessTokenUtil.createMenu(
						timeOutNewsGetToken(), menu);
				if (resultCode == 0) {
					System.out.println("access_token 重新获取成功,创建菜单成功");
				}
			} else if (resultCode == 0) {
				System.out.println("创建菜单成功");
			} else if (resultCode == 48001) {
				System.out.println("微信接口未授权");
			}
		}

	}

	/**
	 * 获取access_token 写文件保存<br/>
	 * 参数 token = null || "" ,都不重写access_token值
	 * 
	 * @param token
	 * @throws IOException
	 */
	public static Map<String, String> getToToken(String token, String... str) {
		Properties propFlies = new Properties();
		String path = ifWinLinux(PATH_STR);
		InputStream is = null;
		OutputStream os = null;
		Map<String, String> map = null;

		try {
			// 读
			is = new FileInputStream(new File(path));
			propFlies.load(is);
			map = (Map) propFlies;
			if (!"".equals(token) && null != token) {
				// 写
				os = new FileOutputStream(new File(path));
				propFlies.setProperty("access_token", token);
				propFlies.store(os, "");
				os.flush();
				os.close();
			}
			is.close();
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
		return map;
	}

	/**
	 * ture windows false linux
	 * 返回响应的url
	 * @return
	 */
	public static String ifWinLinux(String path_url) {
		String path = null;
		String os = System.getProperty("os.name");
		if (os.toLowerCase().startsWith("win")) {
			path = path_url.replace("%20", " ").replace("target/classes/",
					"src/main/resources/");
			path = path.substring(1, path.length());
		} else {
			path = path_url.replace("target/classes/", "src/main/resources/");
		}

		return path;
	}

	/**
	 * accessToken 超时或者没有取到token，都从获取新的token
	 * 
	 * @return
	 */
	public static String timeOutNewsGetToken() {
		AccessToken accessToken = null;
		String token = null;
		try {
			accessToken = getAccessToken();
			token = accessToken.getToken();
			getToToken(accessToken.getToken());
			System.out.println(accessToken.getToken());
			System.out.println(accessToken.getExpiresIn());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return token;
	}

}
