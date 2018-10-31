package com.bees360.service;

import com.bees360.common.ServiceResponse;
import com.bees360.pojo.Shipping;
import com.github.pagehelper.PageInfo;

public interface ShippingService {
	
	ServiceResponse add(Integer userId,Shipping shipping);
	
	ServiceResponse<String> del(Integer userId,Integer shippingId);
	
	ServiceResponse update(Integer userId,Shipping shipping);
	
	ServiceResponse<Shipping> select(Integer userId,Integer shippingId);
	
	ServiceResponse<PageInfo> list(Integer userId,int pageNum,int pageSize);

}
