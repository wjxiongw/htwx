package com.wy.wx.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wy.wx.dao.EmailCodeDao;
import com.wy.wx.model.VpnEmailCode;
import com.wy.wx.service.EmailCodeService;


@Service
public class EmailCodeServiceImpl implements EmailCodeService {

	@Resource
	private EmailCodeDao emailCode;
	
	public void insertEmailCode(VpnEmailCode vec) {
		emailCode.insertEmailCode(vec);
	}

	public List<VpnEmailCode> queryListCode(String str) {
		return emailCode.queryListCode(str);
	}

	public void updateEmailCode(VpnEmailCode vec) {
		emailCode.updateEmailCode(vec);
	}

	public VpnEmailCode getEmailCode(int id) {
		
		return  emailCode.getEmailCode(id);
	}

	public List<VpnEmailCode> queryAll() {
		return emailCode.queryAll();
	}

}
