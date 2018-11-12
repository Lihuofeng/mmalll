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
import com.bees360.service.OrderService;
import com.bees360.service.UserService;
import com.bees360.vo.OrderVo;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/manage/order")
public class OrderManageController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderService orderService;
	
	@RequestMapping("list.do")
	@ResponseBody
	public ServiceResponse<PageInfo> orderList(HttpSession session,@RequestParam(value="pageNum",defaultValue="1")int pageNum,@RequestParam(value="pageSize",defaultValue="10")int pageSize){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
		}
		if(userService.chekAdminRole(user).isSuccess()) {
			return orderService.manageList(pageNum, pageSize);
		}else {
			return ServiceResponse.createByErrorMessage("无权限操作");
		}
	}
	
	@RequestMapping("detail.do")
	@ResponseBody
	public ServiceResponse<OrderVo> orderDetail(HttpSession session,Long orderNo){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
		}
		if(userService.chekAdminRole(user).isSuccess()) {
			return orderService.manageDetail(orderNo);
		}else {
			return ServiceResponse.createByErrorMessage("无权限操作");
		}
	}
	
	
	@RequestMapping("search.do")
	@ResponseBody
	public ServiceResponse<PageInfo> orderSearch(HttpSession session,Long orderNo,@RequestParam(value="pageNum",defaultValue="1")int pageNum,@RequestParam(value="pageSize",defaultValue="10")int pageSize){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
		}
		if(userService.chekAdminRole(user).isSuccess()) {
			return orderService.manageSearch(orderNo, pageNum, pageSize);
		}else {
			return ServiceResponse.createByErrorMessage("无权限操作");
		}
	}
	
	
	@RequestMapping("send_goods.do")
	@ResponseBody
	public ServiceResponse<String> orderSendGoods(HttpSession session,Long orderNo){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
		}
		if(userService.chekAdminRole(user).isSuccess()) {
			return orderService.manageSendGoods(orderNo);
		}else {
			return ServiceResponse.createByErrorMessage("无权限操作");
		}
	}
	
}
