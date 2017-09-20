package com.wy.wx.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wy.basic.annotations.AdminAuth;
import com.wy.basic.annotations.Token;
import com.wy.basic.exception.SystemException;
import com.wy.basic.tools.TokenTools;
import com.wy.basic.utils.PageableUtil;
import com.wy.basic.utils.ParamFilterUtil;
import com.wy.wx.model.WxUser;
import com.wy.wx.service.WxUserService;
import com.wy.wx.service.impl.WxUserServiceImpl;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping(value="/wx/users")
@ApiIgnore  //忽略在swagger中显示
@AdminAuth(name="用户管理", orderNum=2, psn="系统应用", pentity=0, porderNum=3) //一级菜单
public class WxUserController {

	private static Logger log = Logger.getLogger(WxUserController.class);
	@Autowired
    private WxUserService service;
	
	@AdminAuth(name="用户管理", orderNum=1, icon="fa fa-list-ul", type="1")
    @RequestMapping(value="list", method= RequestMethod.GET)
    public String list(Model model, Integer page, HttpServletRequest request) {
		
        Page<WxUser> datas = service.findAll(new ParamFilterUtil<WxUser>().buildSearch(model, request),
                PageableUtil.basicPage(page));
        model.addAttribute("datas", datas);
        return "wx/qyuser/list";
    }
	
	/**
	 * 根据企业号用户ID，获取详情
	 * @param userid
	 * @return
	 */
	
	@Token(flag=Token.READY)
    @AdminAuth(name = "添加用户", orderNum = 2, icon="icon-plus")
    @RequestMapping(value="add", method=RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
		WxUser user = new WxUser();
		user.setStatus("1");
        model.addAttribute("user", user);
        return "wx/qyuser/add";
    }

    /** 添加POST */
    @Token(flag=Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, WxUser user, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
        	WxUser u = service.findByUserid(user.getUserid());
            if(u!=null) {throw new SystemException("企业号ID【"+user.getUserid()+"】已经存在，不可重复添加！");}
            user.setStatus("1");
            service.save(user);
        }
        return "redirect:qyuser/list";
    }
    
	
}