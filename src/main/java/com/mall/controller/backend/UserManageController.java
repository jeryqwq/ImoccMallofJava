package com.mall.controller.backend;

import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/user")
public class UserManageController {
@Autowired
private IUserService iUserService;
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> userManage(String username, String password, HttpSession session){
ServerResponse<User> serverResponse=iUserService.login(username,password);
if(serverResponse.isSuccess()){
    User user=serverResponse.getData();
    if(user.getRole()==Const.Role.ROLE_ADMIN){
        session.setAttribute(Const.CURRENT_USER,user);
        return  serverResponse;
    }else {
        return  ServerResponse.createByErrorMessage("你不是管理员");
    }
}
        return  serverResponse;
    }
}
