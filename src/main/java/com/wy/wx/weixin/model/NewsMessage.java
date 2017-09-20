package com.wy.wx.weixin.model;

import java.util.List;

/**
 * 文图消息
 * @author xiongw
 *
 */
public class NewsMessage extends BaseMessage{
	
	/**
	 * 文图消息个数
	 */
	private int ArticleCount ;
	
	/**
	 * 文图消息
	 */
	private List<News> Articles;
	
	
	
	public int getArticleCount() {
		return ArticleCount;
	}
	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}
	public List<News> getArticles() {
		return Articles;
	}
	public void setArticles(List<News> articles) {
		Articles = articles;
	}
	
	
	

}
