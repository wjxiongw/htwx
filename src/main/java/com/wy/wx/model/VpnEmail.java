package com.wy.wx.model;

import java.io.Serializable;

public class VpnEmail implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	
	private String email;
	
	private String timestampDate;
	
	private String ipAddress ;
	
	private String status;
	
	private String startDate;
	
	private String endDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTimestampDate() {
		return timestampDate;
	}

	public void setTimestampDate(String timestampDate) {
		this.timestampDate = timestampDate;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "VpnEmail [id=" + id + ", email=" + email + ", timestampDate="
				+ timestampDate + ", ipAddress=" + ipAddress + ", status="
				+ status + ", startDate=" + startDate + ", endDate=" + endDate
				+ "]";
	}

	
	
	
}
