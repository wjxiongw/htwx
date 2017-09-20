package com.wy.wx.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wy.wx.dao.EmailDao;
import com.wy.wx.model.VpnEmail;
import com.wy.wx.service.EmailService;


@Service
public class EmailServiceImpl implements EmailService {
	
	@Resource
	private EmailDao emailDao;

	public int insertEmail(VpnEmail ve) {
		return emailDao.insertEmail(ve);
	}

	public List<VpnEmail> getVpnEmail( String ip ,String strEmail) {
		return emailDao.getVpnEmail(ip , strEmail );
	}

	public List<VpnEmail> queryAll() {
		return  emailDao.queryAll();
	}
}
