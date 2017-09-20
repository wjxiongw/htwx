package com.wy.wx.service;

import java.util.List;

import com.wy.wx.model.VpnEmailCode;

public interface EmailCodeService {
	
	public void insertEmailCode(VpnEmailCode vec);
	
	public List<VpnEmailCode> queryListCode(String str);
	
	public void updateEmailCode(VpnEmailCode vec);
	
	public VpnEmailCode getEmailCode(int id);

	
	public List<VpnEmailCode> queryAll();
}
