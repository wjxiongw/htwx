package com.wy.wx.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wy.wx.model.WxUser;
import com.wy.wx.service.WxUserService;

/**
 * Created by zsl-pc on 2016/9/7.
 */
@Component
public class WxUserServiceImpl {

    @Autowired
    private WxUserService wxUserService;

    /**
     * 添加或删除用户角色对应关系，如果存在则删除，如果不存在则添加
     * @param userId 用户Id
     * @param roleId 角色Id
     */
    public void add(WxUser user) {
    	wxUserService.save(user);
    }
    
    public WxUser loadWxUserById(Integer id) {
        WxUser ur = wxUserService.findByUidAndRid(id);
        return ur;
    }
    
    public List<WxUser> getListWxUsers() {
    	List<WxUser> list = wxUserService.getListWxUsers();
        return list;
    }
}
