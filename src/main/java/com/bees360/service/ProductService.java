package com.bees360.service;

import com.bees360.common.ServiceResponse;
import com.bees360.pojo.Product;
import com.bees360.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;

public interface ProductService {
	ServiceResponse saveOrUpdateProduct(Product product);
	
	ServiceResponse<String> setSaleStatus(Integer productId,Integer status);
	
	ServiceResponse<ProductDetailVo> manageProductDetail(Integer productId);
	
	ServiceResponse<PageInfo> getProductList(int pageNum,int pageSize);
	
	ServiceResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize);
	
	ServiceResponse<ProductDetailVo> getProductDetail(Integer productId);
	
	ServiceResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);
}
