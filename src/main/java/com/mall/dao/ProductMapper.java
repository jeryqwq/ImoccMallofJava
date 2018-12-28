package com.mall.dao;

import com.mall.pojo.Comment;
import com.mall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);
    List<Product> selectList();
    List<Product> selectByNameAndProductId(@Param("productName")String productName,@Param("productId")Integer productId);
    List<Product> selectByNameAndCategoryIds(@Param("productName")String productName,@Param("categoryIdList") List<Integer> categoryIdList);
    List<Comment> selectAllCommentByProductId(@Param("productId")Integer productId);
    int insertComment(@Param("comment")Comment comment);
}