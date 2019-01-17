package com.mall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.pojo.Comment;
import com.mall.pojo.User;
import com.mall.service.IProductService;
import com.mall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/product/")
public class ProductController {
@Autowired
    private IProductService iProductService;
@RequestMapping(value = "detail.do",method = RequestMethod.POST)
@ResponseBody
public ServerResponse<ProductDetailVo> detail(Integer productId){
return  iProductService.getProductDetail(productId);
}
    @RequestMapping(value = "list.do",method = RequestMethod.POST)
    @ResponseBody
public  ServerResponse<PageInfo> List (@RequestParam(value="keyword",required = false)String keyword,
                                       @RequestParam(value="categoryId",required = false)Integer categoryId,
                                       @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                       @RequestParam(value = "pageSize",defaultValue = "8") int pageSize,
                                       @RequestParam(value = "orderBy",defaultValue = "") String  orderBy
                                       ){
return  iProductService.getProductByKeywordCategory(keyword,categoryId,pageNum,pageSize,orderBy);
        }
@RequestMapping(value="comment.do",method = RequestMethod.POST)
@ResponseBody
    public ServerResponse comment (HttpSession session, String cContent,int cStarts,Integer productId,Integer toId,Long orderNo){
    User user=(User)session.getAttribute(Const.CURRENT_USER);
    if(user==null){
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请登录后重试！");
    }else{
        Comment comment=new Comment();
        comment.setUserId(user.getId());
        comment.setUsername(user.getUsername());
        comment.setProductId(productId);
        comment.setcContent(cContent);
        comment.setcStarts(cStarts);
        comment.setToId(toId);
        return iProductService.insertComment(comment,orderNo);
    }
}

@RequestMapping(value="getComment.do",method = RequestMethod.POST)
@ResponseBody
    public ServerResponse getComment(Integer productId ,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "8") int pageSize){
    return iProductService.getAllCommentByProductId(productId,pageNum,pageSize);
}
}