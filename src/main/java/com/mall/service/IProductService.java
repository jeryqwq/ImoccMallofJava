package com.mall.service;

import com.github.pagehelper.PageInfo;
import com.mall.common.ServerResponse;
import com.mall.pojo.Comment;
import com.mall.pojo.Product;
import com.mall.vo.ProductDetailVo;

import java.util.List;

public interface IProductService {
    ServerResponse saveOrUpdateProduce(Product product,String upLoadType);
    ServerResponse<String> setSalStatus(Integer productId,Integer status);
    ServerResponse<ProductDetailVo> manageProduct(Integer productId);
    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);
    ServerResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize);
    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);
    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);
    ServerResponse<PageInfo> getAllCommentByProductId(Integer productId,int pageNum,int pageSize);
    ServerResponse<Integer>  insertComment(Comment comment,Long orderNo);
}
