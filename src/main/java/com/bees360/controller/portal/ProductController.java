package com.bees360.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bees360.common.ServiceResponse;
import com.bees360.service.ProductService;
import com.bees360.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/product/")
public class ProductController {
	@Autowired
	private ProductService productService;

	/**
	 * 商品详情
	 * @param productId
	 * @return
	 */
	@RequestMapping("detail.do")
	@ResponseBody
	public ServiceResponse<ProductDetailVo> detail(Integer productId){
		return productService.getProductDetail(productId);
	}

	/**
	 * 前端搜索类表
	 */
	@RequestMapping("list.do")
	@ResponseBody
	public ServiceResponse<PageInfo> list(@RequestParam(value="keyword",required =false)String keyword,
										  @RequestParam(value="categoryId",required =false)Integer categoryId,
										  @RequestParam(value="pageNum",defaultValue ="1")int pageNum,
										  @RequestParam(value="pageSize",defaultValue ="10")int pageSize,
										  @RequestParam(value="orderBy",defaultValue ="")String orderBy){
		
		
		return productService.getProductByKeywordCategory(keyword, categoryId, pageNum, pageSize, orderBy);

	}
}
