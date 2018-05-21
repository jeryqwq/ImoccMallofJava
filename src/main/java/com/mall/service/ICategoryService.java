package com.mall.service;

import com.mall.common.ServerResponse;
import com.mall.pojo.Category;

import java.util.List;

public interface  ICategoryService {

    ServerResponse addCategory(String categoryName, Integer parentId );
    ServerResponse updateCategory(String categoryName,Integer parentId);
    ServerResponse<List<Category>>  getChildrenCategory(Integer CategoryId);
    ServerResponse<List<Integer>>  selectCategoryAndChildById(Integer categoryId);
}
