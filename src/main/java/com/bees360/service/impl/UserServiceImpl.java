package com.bees360.service.impl;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bees360.common.Const;
import com.bees360.common.ServiceResponse;
import com.bees360.common.TokenCache;
import com.bees360.dao.UserMapper;
import com.bees360.pojo.User;
import com.bees360.service.UserService;
import com.bees360.utils.MD5Util;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	public ServiceResponse<User> login(String username, String password) {
		int resultCount =userMapper.checkUsername(username);
		if(resultCount==0) {
			return ServiceResponse.createByErrorMessage("用户名不存在！");
		}
		//密码登录MD5
		String md5Password = MD5Util.MD5EncodeUtf8(password);
		User user = userMapper.selectLogin(username, md5Password);
		if(user == null) {
			return ServiceResponse.createByErrorMessage("密码错误！");
		}
		user.setPassword(StringUtils.EMPTY);
		return ServiceResponse.createBySuccess("登录成功",user);
	}

	public ServiceResponse<String> register(User user){
		/*int resultCount = userMapper.checkUsername(user.getUsername());
		if(resultCount>0) {
			return ServiceResponse.createByErrorMessage("用户名已存在！");
		}*/

		ServiceResponse validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
		if(!validResponse.isSuccess()) {
			return validResponse;
		}

		/*resultCount=userMapper.checkEmail(user.getEmail());
		if(resultCount>0) {
			return ServiceResponse.createByErrorMessage("email已经存在");
			}*/

		validResponse = this.checkValid(user.getEmail(), Const.EMIAL);
		if(!validResponse.isSuccess()) {
			return validResponse;
		}

		user.setRole(Const.Role.ROLE_CUSTOMER);
		//MD5加密
		user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
		int resultCount = userMapper.insert(user);
		if(resultCount==0) {
			return ServiceResponse.createByErrorMessage("注册失败！");
		}
		return ServiceResponse.createByErrorMessage("注册成功！");
	}

	public ServiceResponse<String> checkValid(String str,String type){
		if(StringUtils.isNotBlank(type)) {
			//开始校验
			if(Const.USERNAME.equals(type)) {
				int resultCount = userMapper.checkUsername(str);
				if(resultCount>0) {
					return ServiceResponse.createByErrorMessage("用户名已存在！");
				}
			}
			if(Const.EMIAL.equals(type)) {
				int resultCount=userMapper.checkEmail(str);
				if(resultCount>0) {
					return ServiceResponse.createByErrorMessage("email已经存在");
				}
			}
		}else {
			return ServiceResponse.createByErrorMessage("参数错误！");
		}
		return ServiceResponse.createBySuccessMessage("校验成功");
	}

	public ServiceResponse selectQuestion(String username) {
		ServiceResponse validResponse = this.checkValid(username, Const.USERNAME);
		if(validResponse.isSuccess()) {
			//用户不存在
			return ServiceResponse.createByErrorMessage("用户不存在");
		}
		String question =userMapper.selectQuestionByUsername(username);
		if(StringUtils.isNoneBlank(question)) {
			return ServiceResponse.createBySuccess(question);
		}
		return ServiceResponse.createByErrorMessage("找回密码的问题是空的！");
	}


	public ServiceResponse<String> checkAnsewer(String username,String question,String answer){
		int resultCount = userMapper.checkAnswer(username, question, answer);
		if(resultCount>0) {
			//问题及问题答案是这个用户，并且是正确的
			String forgetToken = UUID.randomUUID().toString();
			TokenCache.setKey(TokenCache.TOKEN_PREFIX+username, forgetToken);
			return ServiceResponse.createBySuccess(forgetToken);
		}
		return ServiceResponse.createByErrorMessage("问题的答案错误");
	}


	public ServiceResponse<String> forgetRestPassword(String username,String passwordNew,String forgetToken){
		if(StringUtils.isBlank(forgetToken)) {
			return ServiceResponse.createByErrorMessage("参数错误,token需要传递");
		}
		ServiceResponse validResponse = this.checkValid(username, Const.USERNAME);
		if(validResponse.isSuccess()) {
			//用户不存在
			return ServiceResponse.createByErrorMessage("用户不存在");
		}
		String token =TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
		if(StringUtils.isBlank(token)) {
			return ServiceResponse.createByErrorMessage("token无效或者过期");
		}
		
		if(StringUtils.equals(forgetToken, token)) {
			String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
			int rowCount = userMapper.updatePasswordByUsername(username, md5Password);
			if(rowCount>0) {
				return ServiceResponse.createBySuccessMessage("修改密码成功！");
			}
		}else {
			return ServiceResponse.createByErrorMessage("token错误，请重新获取重置密码的 token");
		}
		return ServiceResponse.createByErrorMessage("修改密码失败");
	}
	
	public ServiceResponse<String> restPassword( String passwordOld,String passwordNew,User user){
		//防止横向越权，需要校验这个用户的旧密码，一定要指定是登录的这个用户，因为查询count(1),如果不指定id，查出来的结果就为true，count>0
		int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
		if(resultCount==0) {
			return ServiceResponse.createByErrorMessage("旧密码错误");
		}
		user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
		int updataCount = userMapper.updateByPrimaryKeySelective(user);
		if(updataCount>0) {
			return ServiceResponse.createBySuccessMessage("密码更新成功");
		}
		return ServiceResponse.createByErrorMessage("密码更新失败");
	}
	
	
	public ServiceResponse<User> update_information(User user){
		//username不能被更新
		//email需要进行一个校验，校验新的email是不是已经存在，并且存在的email如果相同，不能是当前用户
		int resuleCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
		if(resuleCount>0) {
			return ServiceResponse.createByErrorMessage("email已经存在，请更换email再更新");
		}
		
		User updateUser = new User();
		updateUser.setId(user.getId());
		updateUser.setEmail(user.getEmail());
		updateUser.setPhone(user.getPhone());
		updateUser.setQuestion(user.getQuestion());
		updateUser.setAnswer(user.getAnswer());
		int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
		if(updateCount>0) {
			return ServiceResponse.createBySuccess("更新个人信息成功", updateUser);
		}
		return ServiceResponse.createByErrorMessage("更新个人信息失败");
	}
	
	public ServiceResponse<User> getInformation(Integer userId){
		User user = userMapper.selectByPrimaryKey(userId);
		if(user==null) {
			return ServiceResponse.createByErrorMessage("找不到当前用户");
		}
		user.setPassword(StringUtils.EMPTY);
		return ServiceResponse.createBySuccess(user);
	}

}
