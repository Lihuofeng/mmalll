package com.bees360.controller.backend;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bees360.common.Const;
import com.bees360.common.ServiceResponse;
import com.bees360.pojo.User;
import com.bees360.service.UserService;

@Controller
@RequestMapping("/manage/user/")
public class UserManageController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="login.do",method=RequestMethod.POST)
	@ResponseBody
	public ServiceResponse<User> login(String username,String password,HttpSession session){
		 ServiceResponse<User> response = userService.login(username, password);
		 if(response.isSuccess()) {
			 User user =response.getData();
			 if(user.getRole()==Const.Role.ROLE_ADMIN) {
				 //说明登录的是管理员
				 session.setAttribute(Const.CURRENT_USER, user);
				 return response;
			 }else {
				 return ServiceResponse.createByErrorMessage("不是管理员，无法登录");
			 }
		 }
		 return response;
	}
}
