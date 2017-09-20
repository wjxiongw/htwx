package com.wy.wx.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wy.wx.model.WxUser;
import com.wy.wx.service.impl.WxUserServiceImpl;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value="/wx/users")

public class WxUserController {

	@Autowired
    private WxUserServiceImpl wxUserServiceImpl;
	private static Logger log = Logger.getLogger(WxUserController.class);
   // static Map<Long, User> users = Collections.synchronizedMap(new HashMap<Long, User>());

    @ApiOperation(value="获取用户列表", notes="")
    @RequestMapping(value={""}, method= RequestMethod.GET)
    public List<WxUser> getUserList() {
    	List<WxUser> list = wxUserServiceImpl.getListWxUsers();
        return list;
    }

    @ApiOperation(value="创建用户", notes="根据User对象创建用户")
    @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "WxUser")
    @RequestMapping(value="", method=RequestMethod.POST)
    public String postUser(@RequestBody WxUser user) {
    	wxUserServiceImpl.add(user);
    	List<WxUser> list = wxUserServiceImpl.getListWxUsers();
    	log.info("User Size:"+list.size());
        return "success";
    }
    @ApiOperation(value="获取用户详细信息", notes="根据url的id来获取用户详细信息")
    @ApiImplicitParam(paramType = "path",name = "id", value = "用户ID", required = true, dataType = "Integer")
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public WxUser getUser(@PathVariable("id")  Integer id) {
    	System.out.println("=========getUser========="+id);
    	WxUser user = wxUserServiceImpl.loadWxUserById(id);
        return user;
    }
    
//    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
//    public String putUser(@PathVariable Long id, @RequestBody User user) {
//    	System.out.println("===========putUser======="+id);
//        User u = users.get(id);
//        u.setName(user.getName());
//        u.setAge(user.getAge());
//        users.put(id, u);
//        return "success";
//    }
//
//    @ApiOperation(value="删除用户", notes="根据url的id来指定删除对象")
//    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long")
//    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
//    public String deleteUser(@PathVariable Long id) {
//        users.remove(id);
//        return "success";
//    }
}