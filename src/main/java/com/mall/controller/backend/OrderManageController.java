package com.mall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.IOrderService;
import com.mall.service.IUserService;
import com.mall.vo.OrderVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("manage/order")
public class OrderManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IOrderService iOrderService;
    @RequestMapping(value="list.do",method = RequestMethod.POST)
    @ResponseBody
public ServerResponse<PageInfo> orderList(HttpSession session, @Param("pageSize")int pageSize,@Param("pageNum")int pageNum){
    User user=(User)session.getAttribute(Const.CURRENT_USER);
    if(user==null){
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请登录后重试！");
    }
    if(iUserService.isAdmin(user).isSuccess()){
        return iOrderService.manageList(pageNum,pageSize);
    }else {
        return ServerResponse.createByErrorMessage("您不是管理员");
    }

}
@RequestMapping("detail.do")
@ResponseBody
public ServerResponse<OrderVo> orderDetail(HttpSession session,Long orderNo){
    User user=(User)session.getAttribute(Const.CURRENT_USER);
    if(user==null){
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请登录后重试！");
    }
    if(iUserService.isAdmin(user).isSuccess()){
        return iOrderService.manageDetail(orderNo);
    }else {
        return ServerResponse.createByErrorMessage("您不是管理员");
    }
}
@RequestMapping(value = "setShipped.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setShipped(HttpSession session){
    User user=(User)session.getAttribute(Const.CURRENT_USER);
    if(user==null){
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请登录后重试！");
    }
    if(iUserService.isAdmin(user).isSuccess()){
        return null;
    }else {
        return ServerResponse.createByErrorMessage("您不是管理员");
    }
}
    @RequestMapping(value="search.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> search(HttpSession session,Long orderNo, @Param("pageSize")int pageSize,@Param("pageNum")int pageNum){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请登录后重试！");
        }
        if(iUserService.isAdmin(user).isSuccess()){
            return iOrderService.manageSearch(orderNo,pageNum,pageSize);
        }else {
            return ServerResponse.createByErrorMessage("您不是管理员");
        }
    }
    @RequestMapping(value="sendGoods.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> sendGoods(HttpSession session,Long orderNo){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请登录后重试！");
        }
        if(iUserService.isAdmin(user).isSuccess()){
            return iOrderService.sendGoods(orderNo);
        }else {
            return ServerResponse.createByErrorMessage("您不是管理员");
        }
    }
}
