package com.wy.wx.weixin.jh;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wy.wx.weixin.util.WeixinAccessTokenUtil;

public class JUhe {

	private static final String PATH_STR = WeixinAccessTokenUtil.class
			.getClass().getResource("/key.properties").getPath();

	private static final String XIN_WEN_URL = "http://v.juhe.cn/toutiao/index";

	private static final String XIAO_HUA_URL = "http://japi.juhe.cn/joke/img/text.from";

	/**
	 * 发送api请求
	 * 
	 * @param url
	 *            访问接口地址
	 * @param key
	 *            请求key
	 * @param map
	 *            参数
	 * @return
	 */
	public static JSONObject doSendKey(String url, String key,
			Map<String, String> map) {
		// 获取文件路径
		String path = WeixinAccessTokenUtil.ifWinLinux(PATH_STR);
		String resulKey = null;

		JSONObject json = null;
		Properties p = new Properties();

		try {
			p.load(new FileInputStream(new File(path)));
			resulKey = p.get(key).toString();
			StringBuffer sb = new StringBuffer();
			Set<String> keys = map.keySet();
			Iterator<String> it = keys.iterator();
			while (it.hasNext()) {
				String str = it.next();
				sb.append("&" + str + "=" + map.get(str));
			}
			url = url + "?key=" + resulKey + sb.toString();
			System.out.println("请求API --->> :" + url);
			// 发送请求
			json = WeixinAccessTokenUtil.doGetStr(url);

			System.out.println(resulKey);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return json;
	}

	/**
	 * 调用api 拿到的笑话集合数据
	 * @return
	 */
	public static List<DataInfo> xiaoHuaData() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("page", "1");
		map.put("pagesize", "20");
		JSONObject json = doSendKey(XIAO_HUA_URL, "xiaohua_key", map);
		String resultcode = (String)json.get("resultcode");
		if(resultcode!=null && resultcode.equals("OK")) {
			JSONObject data = (JSONObject) json.get("result");
			return JSONObject.parseArray(data.get("data").toString(),
				DataInfo.class);
		}else {
			System.out.println("请求数据失败！");
			return null;
		}
	}

//	public static void main(String[] args) {
//		List<DataInfo> d =xiaoHuaData();
//		if(d!=null) {
//			for (DataInfo dataInfo : d) {
//				System.out.println(dataInfo.getContent());
//			}
//		}
//	}

}
