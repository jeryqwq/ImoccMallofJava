package com.mall.controller.backend;

import com.google.common.collect.Maps;
import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.pojo.Product;
import com.mall.pojo.User;
import com.mall.service.IFileService;
import com.mall.service.IProductService;
import com.mall.service.IUserService;
import com.mall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {
@Autowired
private IFileService iFileService;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product){
User user= (User) session.getAttribute(Const.CURRENT_USER);
if(user==null){
    return  ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户需要登陆");
}
if(iUserService.isAdmin(user).isSuccess()){
return  iProductService.saveOrUpdateProduce(product);
}else {
    return ServerResponse.createByErrorMessage("您没有权限");
}
    }

    @RequestMapping("setSalStatus.do")
    @ResponseBody
    public ServerResponse setSalStatus(HttpSession session, Integer productId,Integer status){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户需要登陆");
        }
        if(iUserService.isAdmin(user).isSuccess()){
            return iProductService.setSalStatus(productId,status);
        }else {
            return ServerResponse.createByErrorMessage("您没有权限");
        }
    }


    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse setDetail(HttpSession session, Integer productId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户需要登陆");
        }
        if(iUserService.isAdmin(user).isSuccess()){
return  iProductService.manageProduct(productId);
        }else {
            return ServerResponse.createByErrorMessage("您没有权限");
        }
    }


    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "8") int pageSize){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户需要登陆");
        }
        if(iUserService.isAdmin(user).isSuccess()){
            return  iProductService.getProductList(pageNum,pageSize);
        }else {
            return ServerResponse.createByErrorMessage("您没有权限");
        }
    }

    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse getSearch(HttpSession session,String productName,Integer productId, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "8") int pageSize){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户需要登陆");
        }
        if(iUserService.isAdmin(user).isSuccess()){
            return  iProductService.searchProduct(productName,productId,pageNum,pageSize);
        }else {
            return ServerResponse.createByErrorMessage("您没有权限");
        }
    }
    @RequestMapping("upload.do")
    @ResponseBody
    public  ServerResponse upload(HttpSession session,@RequestParam(value = "upload_file",required = false)MultipartFile file, HttpServletRequest request){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户需要登陆");
        }
        if(iUserService.isAdmin(user).isSuccess()){

            String path=request.getSession().getServletContext().getRealPath("upload");
            String targetFileName=iFileService.upload(file,path);
            String url=PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            Map fileMap=Maps.newHashMap();
            fileMap.put("uri",targetFileName);
            fileMap.put("url",url);
            return  ServerResponse.createBySuccess(fileMap);
        }else {
            return ServerResponse.createByErrorMessage("您没有权限");
        }

    }

    @RequestMapping("richTextImgUpload.do")
    @ResponseBody
    public  Map richTextImgUpload(HttpSession session, @RequestParam(value = "upload_file",required = false)MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        Map resultMap=Maps.newHashMap();
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            resultMap.put("success","false");
            resultMap.put("msg","请登录");
            return  resultMap;
//            return  ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户需要登陆");
        }
        if(iUserService.isAdmin(user).isSuccess()){
            String path=request.getSession().getServletContext().getRealPath("upload");
            String targetFileName=iFileService.upload(file,path);
            if(StringUtils.isBlank(targetFileName)){
                resultMap.put("success",false);
                resultMap.put("msg","上传失败");
                return  resultMap;
            }
            String url=PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            resultMap.put("success",true);
            resultMap.put("msg","上海窜成功");
            resultMap.put("file_path",url);
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return  resultMap;
        }else {
            resultMap.put("success",false);
            resultMap.put("msg","您没有权限");
            return  resultMap;
//            return ServerResponse.createByErrorMessage("您没有权限");
        }

    }


}
