package com.bees360.service.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bees360.common.ServiceResponse;
import com.bees360.dao.CategoryMapper;
import com.bees360.pojo.Category;
import com.bees360.service.CategoryService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
@Service("categoryService")

public class CategoryServiceImpl implements CategoryService {

	private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
	
	@Autowired
	private CategoryMapper categoryMapper;
	
	public ServiceResponse addCategory(String categoryName,Integer parentId) {
		if(parentId==null||StringUtils.isBlank(categoryName)) {
			return ServiceResponse.createByErrorMessage("添加品类参数错误");
		}
		Category category = new Category();
		category.setName(categoryName);
		category.setParentId(parentId);
		category.setStatus(true);//这个分类可用
		int resultCount = categoryMapper.insert(category);
		if(resultCount>0) {
			return ServiceResponse.createBySuccess("添加品类成功");
			
		}else {
			return ServiceResponse.createByErrorMessage("添加品类失败");
		}
		
	}
	
	public ServiceResponse updateCategoryName(Integer categoryId,String categoryName) {
		if(categoryId==null||StringUtils.isBlank(categoryName)) {
			return ServiceResponse.createByErrorMessage("添加品类参数错误");
		}
		Category category = new Category();
		category.setId(categoryId);
		category.setName(categoryName);
		 
		int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
		if(rowCount>0) {
			return ServiceResponse.createBySuccess("修改品类名称成功");
		}else {
			return ServiceResponse.createByErrorMessage("修改品类名称失败");
		}
	}
	
	public ServiceResponse<List<Category>> getChilerenParallCategory(Integer categoryId){
		List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
		if(CollectionUtils.isEmpty(categoryList)) {
			logger.info("未找到当前分类的子分类");
		}
		return ServiceResponse.createBySuccess(categoryList);
	}
	
	/**
	 * d递归查询本节点的id以及孩子节点的id
	 * @param categoryId
	 * @return
	 */
	public ServiceResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId) {
		Set<Category> categorySet=Sets.newHashSet();
		findChildrenCategory(categorySet, categoryId);
		List<Integer> categoryIdList = Lists.newArrayList();
		if(categoryId!=null) {
			for (Category ctegoryItem : categorySet) {
				categoryIdList.add(ctegoryItem.getId());
			}
		}
		return ServiceResponse.createBySuccess(categoryIdList);
	}
	
	//递归算法算出子节点
	private Set<Category> findChildrenCategory(Set<Category> categorySet,Integer categoryId){
		Category category = categoryMapper.selectByPrimaryKey(categoryId);
		if(category!=null) {
			categorySet.add(category);
		}
		//查找子节点，递归算法有一个退出的条件
		List<Category> categoryList =categoryMapper.selectCategoryChildrenByParentId(categoryId);
		for (Category categoryItem : categoryList) {
			findChildrenCategory(categorySet, categoryItem.getId());
		}
		return categorySet;
		
	}
	
}
