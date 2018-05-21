package com.mall.service.Impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mall.common.ServerResponse;
import com.mall.dao.CategoryMapper;
import com.mall.pojo.Category;
import com.mall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;


@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
  private  CategoryMapper categoryMapper;
    public ServerResponse addCategory( String categoryName,Integer parentId ){
        if(parentId==null||StringUtils.isNotBlank(categoryName)){
            return  ServerResponse.createByErrorMessage("添加品类错误");
        }
        Category category=new Category();
        category.setName(categoryName);
        category.setId(parentId);
        category.setStatus(true);
        int rowCount=categoryMapper.insert(category);
        if(rowCount>0){
            return ServerResponse.createByErrorMessage("添加成功!");
        }
        return null;
    }
    public  ServerResponse updateCategory(String categoryName,Integer parentId){
        if(parentId==null||StringUtils.isNotBlank(categoryName)){
            return  ServerResponse.createByErrorMessage("参数错误");
        }
        Category category=new Category();
        category.setName(categoryName);
        category.setId(parentId);
        int rowCount=categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount>0){
            return ServerResponse.createByErrorMessage("更新成功!");
        }
        return ServerResponse.createByErrorMessage("更新失败！！！");
    }
    public ServerResponse<List<Category>>  getChildrenCategory(Integer CategoryId){
        List<Category> categoryList = (List<Category>) categoryMapper.selectCategoryByParent_id(CategoryId);
        if(CollectionUtils.isEmpty(categoryList)){
            return  ServerResponse.createByErrorMessage("未找到该子类");
        }
return  ServerResponse.createBySuccess(categoryList);
    }

    public  ServerResponse<List<Integer>> selectCategoryAndChildById(Integer categoryId){
Set<Category> categorySet=Sets.newHashSet();
findChildCategory(categorySet,categoryId);
List<Integer> categoryIdList=Lists.newArrayList();
if(categoryId!=null){
    for(Category categoryItem:categorySet){
        categoryIdList.add(categoryItem.getId());
    }
}
        return  ServerResponse.createBySuccess(categoryIdList);
    }

    //递归调用自身事件并将值放入setCategory中返回
    private Set<Category> findChildCategory(Set<Category> setCategory,Integer categoryId){
        Category category=categoryMapper.selectByPrimaryKey(categoryId);
if(category!=null){
    setCategory.add(category);
}
List<Category> categoryList= (List<Category>) categoryMapper.selectCategoryByParent_id(categoryId);
        for (Category categoryItem: categoryList) {
            findChildCategory(setCategory,categoryItem.getId());
        }
        return  setCategory;
    }
}
