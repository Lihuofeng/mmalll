package com.bees360.service;

import com.bees360.common.ServiceResponse;
import com.bees360.pojo.User;

public interface UserService {
	ServiceResponse<User> login(String username,String password);
	
	ServiceResponse<String> register(User user);
	
	ServiceResponse<String> checkValid(String str,String type);
	
	ServiceResponse selectQuestion(String username);
	
	ServiceResponse<String> checkAnsewer(String username,String question,String answer);
	
	ServiceResponse<String> forgetRestPassword(String username,String passwordNew,String forgetToken);
	
	ServiceResponse<String> restPassword( String passwordOld,String passwordNew,User user);
	
	ServiceResponse<User> update_information(User user);
	
	ServiceResponse<User> getInformation(Integer userId);

	ServiceResponse chekAdminRole(User user);
}
