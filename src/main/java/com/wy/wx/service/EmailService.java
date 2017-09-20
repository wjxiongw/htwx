package com.wy.wx.service;


import java.util.List;

import com.wy.wx.model.VpnEmail;
public interface EmailService {
	
	public int insertEmail(VpnEmail ve );
	
	public List<VpnEmail> getVpnEmail( String ip ,String strEmail );
	
	public List<VpnEmail> queryAll();
	
}
