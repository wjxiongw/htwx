package com.wy.wx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.wy.wx.model.VpnEmail;
@Mapper
public interface EmailDao {
	
	public int insertEmail(VpnEmail ve );
	
	public List<VpnEmail> getVpnEmail(@Param(value="strAddr") String strAddr ,@Param(value="strEmail")String strEmail );
	
	
	public List<VpnEmail> queryAll();

}
