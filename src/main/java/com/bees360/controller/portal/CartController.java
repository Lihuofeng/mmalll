package com.bees360.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bees360.common.Const;
import com.bees360.common.ResponseCode;
import com.bees360.common.ServiceResponse;
import com.bees360.pojo.User;
import com.bees360.service.CartService;
import com.bees360.service.UserService;
import com.bees360.vo.CartVo;

@Controller
@RequestMapping("/cart/")
public class CartController {
	
	@Autowired
	private  UserService userService;
	
	@Autowired
	private CartService cartService;

	/**
	 * 购物车列表
	 * @param session
	 * @return
	 */
	@RequestMapping("list.do")
	@ResponseBody
	public ServiceResponse<CartVo> list(HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return cartService.list(user.getId());
		
	}

	/**
	 * 加入购物车
	 * @param session
	 * @param userId
	 * @param productId
	 * @param count
	 * @return
	 */
	@RequestMapping("add.do")
	@ResponseBody
	public ServiceResponse<CartVo> add(HttpSession session,Integer productId,Integer count) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return cartService.add(user.getId(), productId, count);
		
	}
	
	/**
	 * 更新购物车
	 * @param session
	 * @param productId
	 * @param count
	 * @return
	 */
	@RequestMapping("update.do")
	@ResponseBody
	public ServiceResponse<CartVo> update(HttpSession session,Integer productId,Integer count) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return cartService.update(user.getId(), productId, count);
		
	}
	
	/**
	 * 删除购物车商品
	 * @param session
	 * @param productIds
	 * @return
	 */
	@RequestMapping("delete_product.do")
	@ResponseBody
	public ServiceResponse<CartVo> deleteProduct(HttpSession session,String productIds) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return cartService.deleteProduct(user.getId(), productIds);
		
	}
	
	/**
	 * 全选
	 * @param session
	 * @return
	 */
	@RequestMapping("select_all.do")
	@ResponseBody
	public ServiceResponse<CartVo> selectAll(HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return cartService.selectOrUnSelect(user.getId(), null, Const.Cart.CHECKED);
		
	}
	
	/**
	 * 全反选
	 * @param session
	 * @return
	 */
	@RequestMapping("un_select_all.do")
	@ResponseBody
	public ServiceResponse<CartVo> unSelectAll(HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return cartService.selectOrUnSelect(user.getId(), null, Const.Cart.UN_CHECKED);
		
	}
	
	/**
	 * 单选
	 * @param session
	 * @param productId
	 * @return
	 */
	@RequestMapping("select.do")
	@ResponseBody
	public ServiceResponse<CartVo> select(HttpSession session,Integer productId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return cartService.selectOrUnSelect(user.getId(), productId, Const.Cart.CHECKED);
		
	}
	
	/**
	 * 单反选
	 * @param session
	 * @param productId
	 * @return
	 */
	@RequestMapping("un_select.do")
	@ResponseBody
	public ServiceResponse<CartVo> unSelect(HttpSession session,Integer productId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return cartService.selectOrUnSelect(user.getId(), productId, Const.Cart.UN_CHECKED);
		
	}
	
	/**
	 * 查询当前用户的购物车里面的产品数量,如果一个产品有10个,那么数量就是10.
	 * @param session
	 * @return
	 */
	@RequestMapping("get_cart_product_count.do")
	@ResponseBody
	public ServiceResponse<Integer> getCartProductCount(HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return cartService.getCartProductCount(user.getId());
		
	}
}
