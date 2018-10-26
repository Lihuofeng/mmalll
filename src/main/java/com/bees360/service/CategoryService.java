package com.bees360.service;

import java.util.List;

import com.bees360.common.ServiceResponse;
import com.bees360.pojo.Category;

public interface CategoryService {
	ServiceResponse addCategory(String categoryName,Integer parentId);
	
	ServiceResponse updateCategoryName(Integer categoryId,String categoryName);
	
	ServiceResponse<List<Category>> getChilerenParallCategory(Integer categoryId);
	
	ServiceResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
