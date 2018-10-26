package com.bees360.controller.backend;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bees360.common.Const;
import com.bees360.common.ResponseCode;
import com.bees360.common.ServiceResponse;
import com.bees360.pojo.User;
import com.bees360.service.CategoryService;
import com.bees360.service.UserService;

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

	@Autowired 
	private UserService userService;

	@Autowired
	private CategoryService categoryService;

	/**
	 * 添加分类
	 * @param session
	 * @param categoryName
	 * @param parentId
	 * @return
	 */
	@RequestMapping("add_category.do")
	@ResponseBody
	public ServiceResponse addCategory(HttpSession session,String categoryName,@RequestParam(value="parentId",defaultValue="0" )int parentId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
		}
		//校验是否是管理员
		if(userService.chekAdminRole(user).isSuccess()) {
			//是管理员
			//增加处理分类的逻辑
			return categoryService.addCategory(categoryName, parentId);

		}else {
			return ServiceResponse.createByErrorMessage("无权限操作，需要管理员权限");
		}
	}


	/**
	 * 修改分类名字
	 */
	@RequestMapping("set_category_name.do")
	@ResponseBody
	public ServiceResponse setCategoryName(HttpSession session,Integer categoryId,String categoryName) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
		}
		if(userService.chekAdminRole(user).isSuccess()) {
			//更新categoryName
			return categoryService.updateCategoryName(categoryId, categoryName);
		}
		else {
			return ServiceResponse.createByErrorMessage("无权限操作，需要管理员权限");
		}
	}
	
	
	/**
	 * 获取平级的categoryId
	 */
	@RequestMapping("get_category.do")
	@ResponseBody
	public  ServiceResponse getChilerenParallCategory(HttpSession session,@RequestParam(value="categoryId",defaultValue="0")Integer categoryId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
		}
		if(userService.chekAdminRole(user).isSuccess()) {
			//查询子节点的category信息，并且不递归，保持平级
			return categoryService.getChilerenParallCategory(categoryId);
		}
		else {
			return ServiceResponse.createByErrorMessage("无权限操作，需要管理员权限");
		}	
	
	}
	/**
	 *查询当前categoryId 并且递归查询子节点的categoryId 
	 */
	@RequestMapping("get_deep_category.do")
	@ResponseBody
	public ServiceResponse getCategoryAndDeepChilerenCategory(HttpSession session,@RequestParam(value="categoryId",defaultValue="0")Integer categoryId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
		}
		if(userService.chekAdminRole(user).isSuccess()) {
			//查询当前节点的id 和递归子节点的id
			return categoryService.selectCategoryAndChildrenById(categoryId);
		}
		else {
			return ServiceResponse.createByErrorMessage("无权限操作，需要管理员权限");
		}			
	}
	
}
