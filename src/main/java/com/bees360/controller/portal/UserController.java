package com.bees360.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user/")
public class UserController {
	/**
	 * 用户登录
	 */
	@RequestMapping(value="login.do", method=RequestMethod.POST)
	@ResponseBody
	public Object login(String username,String password,HttpSession session ) {
		
		return null;
	}
}
