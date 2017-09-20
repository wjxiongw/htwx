package com.wy.wx.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.wy.wx.model.WxUser;

/**
 * 
 * @author wy
 *持久层接口，由spring自动生成其实现
 */
public interface WxUserService extends JpaRepository<WxUser, Integer>, JpaSpecificationExecutor<WxUser> {

    /** 通过邮箱获取对象 */
	WxUser findByEmail(String email);
	
	
	WxUser findByUserid(String userid);

	List<WxUser> findByUserName(String userName);
	
    @Query("SELECT t FROM WxUser t WHERE t.id=?1")
    WxUser findByUidAndRid(Integer id);
    
    @Query("FROM WxUser ")
    List<WxUser> getListWxUsers();
    
    
    
}
