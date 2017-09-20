package com.wy.wx.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="wx_user")
public class WxUser {
	@Id
	@GeneratedValue
    private Integer id;
    private String userid;
    private String userName;
    private String mobile;
    private String email;
    private String status;
    private String hxUser;
    private String eaCode;
    private String hrCode;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getHxUser() {
		return hxUser;
	}
	public void setHxUser(String hxUser) {
		this.hxUser = hxUser;
	}
	public String getEaCode() {
		return eaCode;
	}
	public void setEaCode(String eaCode) {
		this.eaCode = eaCode;
	}
	public String getHrCode() {
		return hrCode;
	}
	public void setHrCode(String hrCode) {
		this.hrCode = hrCode;
	}

    
}