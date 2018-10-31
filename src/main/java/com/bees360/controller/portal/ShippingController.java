package com.bees360.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bees360.common.Const;
import com.bees360.common.ResponseCode;
import com.bees360.common.ServiceResponse;
import com.bees360.pojo.Shipping;
import com.bees360.pojo.User;
import com.bees360.service.ShippingService;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/shipping/")
public class ShippingController {
	
	@Autowired
	private ShippingService shippingService;
	
	@RequestMapping("add.do")
	@ResponseBody
	public ServiceResponse add(HttpSession session,Shipping shipping) {
		User user =(User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return shippingService.add(user.getId(), shipping);
	}
	
	@RequestMapping("del.do")
	@ResponseBody
	public ServiceResponse del(HttpSession session,Integer shippingId) {
		User user =(User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return shippingService.del(user.getId(), shippingId);
	}
	
	@RequestMapping("update.do")
	@ResponseBody
	public ServiceResponse update(HttpSession session,Shipping shipping) {
		User user =(User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return shippingService.update(user.getId(), shipping);
	}
	
	@RequestMapping("select.do")
	@ResponseBody
	public ServiceResponse select(HttpSession session,Integer shippingId) {
		User user =(User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return shippingService.select(user.getId(), shippingId);
	}
	
	@RequestMapping("list.do")
	@ResponseBody
	public ServiceResponse<PageInfo> list(HttpSession session, @RequestParam(value="pageNum",defaultValue="1")int pageNum, @RequestParam(value="paageSize",defaultValue="10")int pageSize) {
		User user =(User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return shippingService.list(user.getId(), pageNum, pageSize);
	}
}
