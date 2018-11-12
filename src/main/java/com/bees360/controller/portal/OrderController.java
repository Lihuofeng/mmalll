package com.bees360.controller.portal;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.bees360.common.Const;
import com.bees360.common.ResponseCode;
import com.bees360.common.ServiceResponse;
import com.bees360.pojo.User;
import com.bees360.service.OrderService;
import com.google.common.collect.Maps;

@Controller
@RequestMapping("/order/")
public class OrderController {
	
	private static final Logger logger =LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
	private OrderService orderService;

	
	
	
	@RequestMapping("create.do")
	@ResponseBody
	public ServiceResponse create(HttpSession session,Integer shippingId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return orderService.createOrder(user.getId(), shippingId);
	}
	
	/**
	 * 取消订单
	 * @param session
	 * @param orderNo
	 * @return
	 */
	@RequestMapping("cancel.do")
	@ResponseBody
	public ServiceResponse cancel(HttpSession session,Long orderNo) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return orderService.cancel(user.getId(), orderNo);
	}
	
	@RequestMapping("get_order_cart_product.do")
	@ResponseBody
	public ServiceResponse getOrderCartProduct(HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return orderService.getOrderCartProduct(user.getId());
	}
	
	
	@RequestMapping("detail.do")
	@ResponseBody
	public ServiceResponse detail(HttpSession session,Long orderNo) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return orderService.getOrderDetail(user.getId(), orderNo);
	}
	
	@RequestMapping("list.do")
	@ResponseBody
	public ServiceResponse list(HttpSession session,@RequestParam(value="pageNum",defaultValue="1")int pageNum,@RequestParam(value="pageSize",defaultValue="10")int pageSize) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return orderService.getOrderList(user.getId(), pageNum, pageSize);
	}
	
	
	
	
	
	
	@RequestMapping("pay.do")
	@ResponseBody
	public ServiceResponse pay(HttpSession session,Long orderNo,HttpServletRequest request) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		String path =request.getSession().getServletContext().getRealPath("upload");
		return orderService.pay(orderNo, user.getId(), path);
	}
	
	
	@RequestMapping("alipay_callback.do")
	@ResponseBody
	public Object alipayCallback(HttpServletRequest request) {
		Map<String,String> parms = Maps.newHashMap();
		
		Map requestParms = request.getParameterMap();
		for(Iterator iter = requestParms.keySet().iterator();iter.hasNext();) {
			String name =(String) iter.next();
			String[] values =(String[]) requestParms.get(name);
			String valueStr="";
			for(int i=0;i<values.length;i++) {
				valueStr =(i==values.length-1)?valueStr+values[i]:valueStr+values[i]+",";
			}
			parms.put(name, valueStr);
		}
		logger.info("支付宝回调，sign:{},trade_status:{},参数:{}",parms.get("sign"),parms.get("trade_status"),parms.toString());
		
		//验证回调在的正确性，是不是支付宝发的，并且要避免重复通知
		parms.remove("sign_type");
		try {
			boolean alipayRSACheckedV2 =AlipaySignature.rsaCheckV2(parms, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());
			
			if(!alipayRSACheckedV2) {
				return ServiceResponse.createByErrorMessage("非法请求，验证不通过");
			}
		} catch (AlipayApiException e) {
			logger.error("支付宝验证回调异常",e);
			e.printStackTrace();
		}
		
		ServiceResponse serviceResponse = orderService.aliCallback(parms);
		if(serviceResponse.isSuccess()) {
			return Const.AlipayCallback.RESPONSE_SUCCESS;
		}
		return Const.AlipayCallback.RESPONSE_FAILED;
	}
	
	
	
	@RequestMapping("query_order_pay_status.do")
	@ResponseBody
	public ServiceResponse<Boolean> queryOrderPayStatus(HttpSession session,Long orderNo) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		
		ServiceResponse serviceResponse = orderService.queryOrderPayStatus(user.getId(), orderNo);
		if(serviceResponse.isSuccess()) {
			return ServiceResponse.createBySuccess(true);
		}
		return ServiceResponse.createBySuccess(false);
	}
	
}
