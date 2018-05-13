package com.mall.controller.portal;

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
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService iUserService;
    @RequestMapping(value = "/login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> userLogin(String username, String password,HttpSession session){
ServerResponse<User> response=  iUserService.login(username,password);
if(response.isSuccess()){
    session.setAttribute(Const.CURRENT_USER,response.getData());
}
        return response;
    }
    @RequestMapping(value = "/logout.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String>  userLogout(HttpSession session){
session.removeAttribute(Const.CURRENT_USER);
return ServerResponse.createBySuccess();
    }
 @RequestMapping(value = "/register.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
return iUserService.register(user);
 }
    @RequestMapping(value = "/check_valid.do",method = RequestMethod.POST)
    @ResponseBody
public ServerResponse<String> checkValid(String str,String type){
        return iUserService.checkValid(str,type);
    }

    @RequestMapping(value = "/get_user_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        if(user!=null){
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("无法获取用户信息!您当前未登录!");
    }
    @RequestMapping(value = "/forget_and_question.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetAndQuestion(String username){
return  iUserService.forgetAndQuestion(username);
    }
@RequestMapping(value = "/forget_check_answer.do",method = RequestMethod.POST)
    @ResponseBody
    public  ServerResponse<String> checkAnswer(String username,String question,String answer){
return iUserService.checkAnswer(username,question,answer);
}
    @RequestMapping(value = "/forget_reset_password.do",method = RequestMethod.POST)
    @ResponseBody
public ServerResponse<String> forgetResetPassword(String username,String newPassword,String userToken){
return iUserService.forgetResetPassword(username,newPassword,userToken);
}
    @RequestMapping(value = "/reset_password.do",method = RequestMethod.POST)
    @ResponseBody
public  ServerResponse<String> resetPassword(HttpSession session,String newPassword,String oldPassword){
if(session==null){
    return ServerResponse.createBySuccessMessage("不存在的用户登录信息");
}
return  null;
}

}


