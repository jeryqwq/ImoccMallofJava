package com.mall.controller.backend;

import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.ICategoryService;
import com.mall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;
@RequestMapping(value = "/addCategory.do",method = RequestMethod.POST)
@ResponseBody
    public ServerResponse  addCategory(HttpSession session,String categoryName, @RequestParam(value="parentId", defaultValue="0")int parentId){
    User user=(User)session.getAttribute(Const.CURRENT_USER);
if(user==null){
    return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请登录后重试！");
}
if(iUserService.isAdmin(user).isSuccess()){
return iCategoryService.addCategory(categoryName,parentId);
}else {
    return ServerResponse.createByErrorMessage("您不是管理员");
}
}

@RequestMapping(value = "/updateCategory.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateCategory(HttpSession session,String categoryName,Integer parentId){
    User user=(User)session.getAttribute(Const.CURRENT_USER);
    if(user==null){
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请登录后重试！");
    }
    if(iUserService.isAdmin(user).isSuccess()){
return iCategoryService.updateCategory(categoryName,parentId);
    }else {
        return ServerResponse.createByErrorMessage("您不是管理员");
    }
}
@RequestMapping(value = "/getChildrenCategory.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getChildrenCategory(HttpSession session,@RequestParam(value="parentId", defaultValue="0")int parentId){
return iCategoryService.getChildrenCategory(parentId);
}

    @RequestMapping(value = "/getDeepCategory.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getDeepCategory(HttpSession session,@RequestParam(value="categoryId", defaultValue="0")int categoryId){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请登录后重试！");
        }
        if(iUserService.isAdmin(user).isSuccess()){
return  iCategoryService.selectCategoryAndChildById(categoryId);
        }else {
            return ServerResponse.createByErrorMessage("您不是管理员");
        }
    }
}
