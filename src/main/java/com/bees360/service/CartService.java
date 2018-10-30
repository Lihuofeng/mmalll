package com.bees360.service;

import com.bees360.common.ServiceResponse;
import com.bees360.vo.CartVo;

public interface CartService {
	ServiceResponse<CartVo> add(Integer userId,Integer productId,Integer count);
	
	ServiceResponse<CartVo> update(Integer userId,Integer productId,Integer count);
	
	ServiceResponse<CartVo> deleteProduct(Integer userId,String productIds);

	ServiceResponse<CartVo> list (Integer userId);
	
	ServiceResponse<CartVo> selectOrUnSelect (Integer userId,Integer productId,Integer checked);
	
	ServiceResponse<Integer> getCartProductCount(Integer userId);
}
