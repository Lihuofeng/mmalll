package com.bees360.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bees360.common.Const;
import com.bees360.common.ResponseCode;
import com.bees360.common.ServiceResponse;
import com.bees360.pojo.User;
import com.bees360.service.UserService;

@Controller
@RequestMapping("/user/")
public class UserController {
	@Autowired
	private UserService userService;
	/**
	 * 用户登录
	 */
	@RequestMapping(value="login.do", method=RequestMethod.POST)
	@ResponseBody
	public ServiceResponse<User> login(String username,String password,HttpSession session ) {
		ServiceResponse<User> login = userService.login(username, password);
		if(login.isSuccess()) {
			session.setAttribute(Const.CURRENT_USER, login.getData());
		}
		return login;
	}
	
	/**
	 * 注销登录
	 */
	@RequestMapping(value ="logout.do", method =RequestMethod.POST)
	@ResponseBody
	public ServiceResponse<String> logout(HttpSession session){
		session.removeAttribute(Const.CURRENT_USER);
		return ServiceResponse.createBySuccess();
	}
	
	/**
	 * 注册
	 * @param user
	 * @return
	 */
	@RequestMapping(value="register.do", method=RequestMethod.POST)
	@ResponseBody
	public ServiceResponse<String> register(User user){
		return userService.register(user);
	}
	
	/**
	 * 实时反馈前端信息
	 */
	@RequestMapping(value="check_valid.do", method=RequestMethod.POST)
	@ResponseBody
	public ServiceResponse<String> checkValid(String str,String type){
		return userService.checkValid(str, type);
	}
	
	/**
	 * 获取用户信息
	 */
	@RequestMapping(value="get_user_info.do", method=RequestMethod.POST)
	@ResponseBody
	public ServiceResponse<User> getUserInfo(HttpSession session){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user!=null) {
			return ServiceResponse.createBySuccess(user);
		}
		return ServiceResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
	}
	
	/**
	 * 忘记密码
	 */
	@RequestMapping(value="forget_get_question.do", method=RequestMethod.POST)
	@ResponseBody
	public ServiceResponse<String> forgetGetQuestion(String username){
		return userService.selectQuestion(username);
	}
	
	/**
	 * 校验问题答案
	 */
	@RequestMapping(value="forget_check_answer.do", method=RequestMethod.POST)
	@ResponseBody
	public ServiceResponse<String> forgetCheckAnswer(String username,String question,String answer){
		return userService.checkAnsewer(username, question, answer);
	}
	
	/**
	 * 在忘记密码中重置密码
	 */
	@RequestMapping(value="forget_rest_password.do", method=RequestMethod.POST)
	@ResponseBody
	public ServiceResponse<String> forgetRestPassword(String username,String passwordNew,String forgetToken){
		return userService.forgetRestPassword(username, passwordNew, forgetToken);
	}
	
	/**
	 * 登录状态下重置密码
	 */
	@RequestMapping(value="rest_password.do", method=RequestMethod.POST)
	@ResponseBody
	public ServiceResponse<String> restPassword(HttpSession session, String passwordOld,String passwordNew){
		User user =(User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorMessage("用户未登录！");
		}
		return userService.restPassword(passwordOld, passwordNew, user);
	}
	
	/**
	 * 更新用户个人信息
	 * 
	 */
	@RequestMapping(value="update_information.do", method=RequestMethod.POST)
	@ResponseBody
	public ServiceResponse<User> update_information(HttpSession session,User user){
		User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
		if(currentUser==null) {
			return ServiceResponse.createByErrorMessage("用户未登录！");
		}
		user.setId(currentUser.getId());
		user.setUsername(currentUser.getUsername());
		ServiceResponse<User> response = userService.update_information(user);
		if(response.isSuccess()) {
			session.setAttribute(Const.CURRENT_USER, response.getData());
		}
		return response;
	}
	
	/**
	 * 获取用户详细信息
	 */
	@RequestMapping(value="get_information.do", method=RequestMethod.POST)
	@ResponseBody
	public ServiceResponse<User> get_information(HttpSession session,User user){
		User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
		if(currentUser==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，需要强制登录status=10");
		}
		return userService.getInformation(currentUser.getId());
	}
}
