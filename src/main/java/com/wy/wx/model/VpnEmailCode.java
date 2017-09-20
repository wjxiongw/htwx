package com.wy.wx.model;

import java.io.Serializable;


public class VpnEmailCode  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private int id;
	
	private int emailId;
	
	private String code;
	
	private String status;
	
	private String timestampDate;
	
	private String updateDate;
	private String port;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	 
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public int getEmailId() {
		return emailId;
	}
	public void setEmailId(int emailId) {
		this.emailId = emailId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTimestampDate() {
		return timestampDate;
	}
	public void setTimestampDate(String timestampDate) {
		this.timestampDate = timestampDate;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	@Override
	public String toString() {
		return "VpnEmailCode [id=" + id + ", emailId=" + emailId + ", code="
				+ code + ", status=" + status + ", timestampDate="
				+ timestampDate + ", updateDate=" + updateDate + ", port="
				+ port + "]";
	}
	
	
	
	

}
