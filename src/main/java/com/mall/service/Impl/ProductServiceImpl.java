package com.mall.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.dao.CategoryMapper;
import com.mall.pojo.Comment;
import com.mall.dao.ProductMapper;
import com.mall.pojo.Category;
import com.mall.pojo.Product;
import com.mall.service.ICategoryService;
import com.mall.service.IProductService;
import com.mall.util.DateTimeUtil;
import com.mall.util.PropertiesUtil;
import com.mall.vo.ProductDetailVo;
import com.mall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("iProductServiceImpl")
public class ProductServiceImpl implements IProductService {
@Autowired
  private  ProductMapper productMapper;
@Autowired
private CategoryMapper categoryMapper;
@Autowired
private ICategoryService iCategoryService;
    public ServerResponse saveOrUpdateProduce(Product product,String upLoadType){
if(product!=null){
if(StringUtils.isNotBlank(product.getSubImages())){
 String [] subImageArray=product.getSubImages().split(",");
 if(subImageArray.length>0){
     product.setMainImage(subImageArray[0]);
 }
}
if(upLoadType.equals("update")){
int rowCount= productMapper.updateByPrimaryKey(product);
if(rowCount>0){
    return ServerResponse.createBySuccessMessage("产品更新成功");
}else {
    return ServerResponse.createByErrorMessage("产品更新失败");
}
}else if(upLoadType.equals("save")){
  int  rowCount= productMapper.insert(product);
   if(rowCount>0){
       return ServerResponse.createBySuccessMessage("产品新增成功");
   }else {
       return  ServerResponse.createByErrorMessage("新增失败");
   }
}
}
return ServerResponse.createByErrorMessage("更新参数不正确");
    }


    public ServerResponse<String> setSalStatus(Integer productId,Integer status){
if(productId==null||status==null){
    return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_GRGUMENT.getCode(),ResponseCode.ILLEGAL_GRGUMENT.getDesc());
}
Product product=new Product();
product.setId(productId);
product.setStatus(status);
int rowCount= productMapper.updateByPrimaryKeySelective(product);
if(rowCount>0){
    return ServerResponse.createBySuccessMessage("状态修改成功");
}
return ServerResponse.createByErrorMessage("状态修改失败");
    }


    public  ServerResponse<ProductDetailVo> manageProduct(Integer productId){
if(productId==null){
    return  ServerResponse.createByErrorMessage("无效的参数");
}
Product product=productMapper.selectByPrimaryKey(productId);
if(product==null){
    return  ServerResponse.createByErrorMessage("无效的ID,产品已下架或删除");
}
        ProductDetailVo productDetailVo=assembleProductDetailVo(product);
return ServerResponse.createBySuccess(productDetailVo);
    }

private ProductDetailVo assembleProductDetailVo(Product product){
ProductDetailVo productDetailVo=new ProductDetailVo();
productDetailVo.setId(product.getId());
productDetailVo.setSubImages(product.getSubImages());
productDetailVo.setSubtitle(product.getSubtitle());
productDetailVo.setStatus(product.getStatus());
productDetailVo.setCreateTime(String.valueOf(product.getCreateTime()));
    productDetailVo.setCategoryId(product.getCategoryId());
    productDetailVo.setDetail(product.getDetail());
    productDetailVo.setMainImage(product.getMainImage());
    productDetailVo.setName(product.getName());
    productDetailVo.setPrice(product.getPrice());
    productDetailVo.setDetail(product.getDetail());
    productDetailVo.setStock(product.getStock());
    productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","ftp://127.0.0.1:8088"));
//    Category category=categoryMapper.selectByPrimaryKey(product.getId());
//    if(category!=null){
//        productDetailVo.setCategoryId(0);//根节点
//    }
productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
    productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
    return productDetailVo;
}

public  ServerResponse<PageInfo> getProductList(int pageNum,int pageSize){
    PageHelper.startPage(pageNum,pageSize);
    List<Product> productList=productMapper.selectList();
    List<ProductListVo> productListVos=new ArrayList();
    for (Product product:productList) {
       ProductListVo productListVo= assembleProductListVo(product);
        productListVos.add(productListVo);
    }
    PageInfo pageResult=new PageInfo(productList);
    pageResult.setList(productListVos);
    return  ServerResponse.createBySuccess(pageResult);
}
private ProductListVo assembleProductListVo(Product product){
    ProductListVo productListVo=new ProductListVo();
    productListVo.setId(product.getId());
    productListVo.setCategoryId(product.getCategoryId());
    productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","ftp://127.0.0.1:8088"));
    productListVo.setMainImage(product.getMainImage());
    productListVo.setName(product.getName());
    productListVo.setPrice(product.getPrice());
    productListVo.setStatus(product.getStatus());
    productListVo.setSubtitle(product.getSubtitle());
    return  productListVo;
}
public    ServerResponse <PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);

        if(StringUtils.isNotBlank(productName)){
            productName=new StringBuilder().append("%").append(productName).append("%").toString();
        }
    List<ProductListVo> productListVos=Lists.newArrayList();
        List<Product> productList=productMapper.selectByNameAndProductId(productName,productId);
    for (Product product:productList) {
       productListVos.add( assembleProductListVo(product));
    }
    PageInfo pageResult=new PageInfo(productList);
    pageResult.setList(productListVos);
    return ServerResponse.createBySuccess( pageResult);
}

public  ServerResponse<ProductDetailVo> getProductDetail(Integer productId){
    if(productId==null){
        return  ServerResponse.createByErrorMessage("无效的参数");
    }
    Product product=productMapper.selectByPrimaryKey(productId);
    if(product==null){
        return  ServerResponse.createByErrorMessage("无效的ID,产品已下架或删除");
    }
    if(product.getStatus()!=Const.ProductStatusEnum.ON_SALE.getCode()){
        return  ServerResponse.createByErrorMessage("无效的ID,产品已下架或删除");
    }
    ProductDetailVo productDetailVo=assembleProductDetailVo(product);
    return ServerResponse.createBySuccess(productDetailVo);
}

public  ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy){
//if(StringUtils.isBlank(keyword)&&categoryId==null){
//    return  ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_GRGUMENT.getCode(),ResponseCode.ILLEGAL_GRGUMENT.getDesc());
//}
List<Integer> categoryList=new ArrayList();

if(categoryId!=null){
    Category category=categoryMapper.selectByPrimaryKey(categoryId);
    if(category==null&&StringUtils.isBlank(keyword)){
        PageHelper.startPage(pageNum,pageSize);
        List<ProductListVo> productListVoList= Lists.newArrayList();
        PageInfo pageInfo=new PageInfo(productListVoList);
         return  ServerResponse.createBySuccess(pageInfo);
    }
categoryList=  iCategoryService.selectCategoryAndChildById(category .getId()).getData();
}
if(StringUtils.isNotBlank(keyword)){
    keyword=new StringBuilder().append("%").append(keyword).append("%").toString();
}
PageHelper.startPage(pageNum,pageSize);
if(StringUtils.isNotBlank(orderBy)){
if(Const.productListOrderBy.PRICE_ASC_DESC.contains(orderBy)||Const.productListOrderBy.STOCK_ASC_DESC.contains(orderBy)){
    String[]  orderByArray=orderBy.split("_");
PageHelper.orderBy(orderByArray[0]+" "+orderByArray[1]);
}
}
List<Product> productList=productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword,categoryList.size()==0?null:categoryList);
 List<ProductListVo> productListVoList=Lists.newArrayList();
    for (Product product:productList) {
ProductListVo productListVo=assembleProductListVo(product);
        productListVoList.add(productListVo);
    }
    PageInfo pageInfo=new PageInfo(productList);
    pageInfo.setList(productListVoList);
    return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse<List<Comment>> getAllCommentByProductId(Integer productId) {
        List<Comment> comments=productMapper.selectAllCommentByProductId(productId);
        if(comments.size()<=0){
            return ServerResponse.createByErrorMessage("无返回户数据");
        }
        return  ServerResponse.createBySuccess(comments);
}

    @Override
    public ServerResponse insertComment(Comment comment) {
       int result=productMapper.insertComment(comment);
       if(result>0){
return ServerResponse.createByErrorMessage("评论成功");
       }
       return ServerResponse.createByErrorMessage("评论失败，请稍后重试！");
    }




}



