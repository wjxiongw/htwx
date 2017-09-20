package com.wy.wx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.wy.wx.model.VpnEmailCode;

 @Mapper
public interface EmailCodeDao {
	
	public void insertEmailCode(VpnEmailCode vec);
	
	public List<VpnEmailCode> queryListCode(@Param(value="str") String str);
	
	public void updateEmailCode( VpnEmailCode vec);
	
	public VpnEmailCode getEmailCode(int id);
	
	
	public List<VpnEmailCode> queryAll();


}
