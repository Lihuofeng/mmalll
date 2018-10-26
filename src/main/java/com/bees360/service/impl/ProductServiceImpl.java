package com.bees360.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bees360.common.Const;
import com.bees360.common.ResponseCode;
import com.bees360.common.ServiceResponse;
import com.bees360.dao.CategoryMapper;
import com.bees360.dao.ProductMapper;
import com.bees360.pojo.Category;
import com.bees360.pojo.Product;
import com.bees360.service.CategoryService;
import com.bees360.service.ProductService;
import com.bees360.utils.DateTimeUtil;
import com.bees360.utils.PropertiesUtil;
import com.bees360.vo.ProductDetailVo;
import com.bees360.vo.ProductListVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
@Service("productService")
public class ProductServiceImpl implements ProductService {

	/**
	 * 保存或修改商品
	 */
	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private CategoryMapper categoryMapper;
	
	@Autowired
	private CategoryService categoryService;

	public ServiceResponse saveOrUpdateProduct(Product product) {
		if(product!=null) {
			if(StringUtils.isNotBlank(product.getSubImages())) {
				String[] subImagetArray=product.getSubImages().split(",");
				if(subImagetArray.length>0) {
					product.setMainImage(subImagetArray[0]);
				}
			}
			if(product.getId()!=null) {
				int rowCount = productMapper.updateByPrimaryKey(product);
				if(rowCount>0) {
					return ServiceResponse.createBySuccessMessage("更新产品成功");
				}else {
					return ServiceResponse.createByErrorMessage("更新产品失败");
				}
			}else {
				int rowCount = productMapper.insert(product);
				if(rowCount>0) {
					return ServiceResponse.createBySuccessMessage("新增产品成功");
				}else {
					return ServiceResponse.createByErrorMessage("新增产品失败");
				}
			}
		}
		return ServiceResponse.createByErrorMessage("新增或更新产品失败");
	}


	/**
	 * 修改产品销售状态
	 */
	public ServiceResponse<String> setSaleStatus(Integer productId,Integer status){
		if(productId==null||status==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());

		}
		Product product = new Product();
		product.setId(productId);
		product.setStatus(status);
		int rowCount = productMapper.updateByPrimaryKeySelective(product);
		if(rowCount>0) {
			return ServiceResponse.createBySuccess("修改产品销售装状态成功");
		}
		return ServiceResponse.createByErrorMessage("修改产品销售装状态失败");
	}


	/**
	 * 后台获取商品详情
	 */

	public ServiceResponse<ProductDetailVo> manageProductDetail(Integer productId){
		if(productId==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		Product product= productMapper.selectByPrimaryKey(productId);
		if(product==null) {
			return ServiceResponse.createByErrorMessage("产品已下架或者删除");
		}
		ProductDetailVo productDetailVo= assembleProductDetailVo(product);
		return ServiceResponse.createBySuccess(productDetailVo);

	}



	private ProductDetailVo assembleProductDetailVo(Product product) {
		ProductDetailVo productDetailVo= new ProductDetailVo();
		productDetailVo.setId(product.getId());
		productDetailVo.setSubtitle(product.getSubtitle());
		productDetailVo.setPrice(product.getPrice());
		productDetailVo.setMainImages(product.getMainImage());
		productDetailVo.setSubImages(product.getSubImages());
		productDetailVo.setCategoruId(product.getCategoryId());
		productDetailVo.setDetail(product.getDetail());
		productDetailVo.setName(product.getName());
		productDetailVo.setStatus(product.getStatus());
		productDetailVo.setStock(product.getStock());

		//imgeHost
		productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://image.mall.com/"));
		//parentCategoryId
		Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
		if(category==null) {
			productDetailVo.setParentCategoryId(0);//默认根节点
		}else {
			productDetailVo.setParentCategoryId(category.getParentId());
		}
		//createTime
		productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
		//updateTime
		productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
		return productDetailVo;
	}


	public ServiceResponse<PageInfo> getProductList(int pageNum,int pageSize) {
		//startPage--start
		PageHelper.startPage(pageNum,pageSize);
		List<Product> productList =productMapper.selectList();
		List<ProductListVo> productListVoList = Lists.newArrayList();
		for (Product productItem : productList) {
			ProductListVo productListVo = assembleProductListVo(productItem);
			productListVoList.add(productListVo);
		}
		//pageHelper--结束
		PageInfo pageResult = new PageInfo(productList);
		pageResult.setList(productListVoList);
		return ServiceResponse.createBySuccess(pageResult);
	}


	public ProductListVo assembleProductListVo(Product product) {
		ProductListVo productListVo  = new ProductListVo();
		productListVo.setId(product.getId());
		productListVo.setCategoryId(product.getCategoryId());
		productListVo.setName(product.getName());
		productListVo.setSubtitle(product.getSubtitle());
		productListVo.setMainImage(product.getMainImage());
		productListVo.setPrice(product.getPrice());
		productListVo.setStatus(product.getStatus());
		productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://image.mall.com/"));
		return productListVo;
	}



	public ServiceResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize){
		PageHelper.startPage(pageNum, pageSize);
		if(StringUtils.isNotBlank(productName)) {
			productName = new StringBuilder().append("%").append(productName).append("%").toString();
		}
		List<Product> productList = productMapper.selectByNameAndProductId(productName, productId);
		List<ProductListVo> productListVoList = Lists.newArrayList();
		for (Product productItem : productList) {
			ProductListVo productListVo = assembleProductListVo(productItem);
			productListVoList.add(productListVo);
		}
		//pageHelper--结束
		PageInfo pageResult = new PageInfo(productList);
		pageResult.setList(productListVoList);
		return ServiceResponse.createBySuccess(pageResult);
	}
	
	/**
	 * 前台商品详情
	 */
	public ServiceResponse<ProductDetailVo> getProductDetail(Integer productId){
		if(productId==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		Product product= productMapper.selectByPrimaryKey(productId);
		if(product==null) {
			return ServiceResponse.createByErrorMessage("产品已下架或者删除");
		}
		if(product.getStatus()!=Const.ProductStatusEnum.ON_SALE.getCode()) {
			return ServiceResponse.createByErrorMessage("产品已下架或者删除");
		}
		ProductDetailVo productDetailVo= assembleProductDetailVo(product);
		return ServiceResponse.createBySuccess(productDetailVo);
	}
	
	/**
	 * 
	 */
	public ServiceResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy){
		if(StringUtils.isBlank(keyword)&&categoryId==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		List<Integer> categoryIdList = new ArrayList<Integer>();
		if(categoryId!=null) {
			Category category = categoryMapper.selectByPrimaryKey(categoryId);
			if(category==null&&StringUtils.isBlank(keyword)) {
				//没有该分类，且没有关键字,返回空结果集
				PageHelper.startPage(pageNum,pageSize);
				List<Product> productListVoList = Lists.newArrayList();
				PageInfo pageInfo= new PageInfo<>(productListVoList);
				return ServiceResponse.createBySuccess(pageInfo);
			}
			categoryIdList=categoryService.selectCategoryAndChildrenById(category.getId()).getData();
		}
		if(StringUtils.isNotBlank(keyword)) {
			keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
		}
		PageHelper.startPage(pageNum,pageSize);
		//排序
		if(StringUtils.isNotBlank(orderBy)) {
			if(Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
				String[] orderByArray = orderBy.split("_");
				PageHelper.orderBy(orderByArray[0]+" "+orderByArray[1]);
			}
		}
		List<Product> productList =productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword, categoryIdList.size()==0?null:categoryIdList);
		List<ProductListVo> productListVoList =Lists.newArrayList();
		for (Product product : productList) {
			ProductListVo productListVo = assembleProductListVo(product);
			productListVoList.add(productListVo);
		}
		PageInfo pageInfo= new PageInfo(productList);
		pageInfo.setList(productListVoList);
		return ServiceResponse.createBySuccess(pageInfo);
	}

}
