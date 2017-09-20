package com.wy.wx.weixin.model;

/**
 * 
 * 文图详情
 * @author xiongw
 *
 */
public class News {

	/**
	 * 标头
	 */
	private String Title;
	/**
	 * 内容
	 */
	private String Description;
	/**
	 * 图片地址
	 */
	private String PicUrl;
	/**
	 * 跳转地址
	 */
	private String Url;
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getPicUrl() {
		return PicUrl;
	}
	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}
	
	
	
	
}
