package com.mall.controller.portal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {
    @RequestMapping(value = "/login.do",method = RequestMethod.GET)
    @ResponseBody
    public String userLogin(String username,String password){


        return username+password;
    }
    @RequestMapping(value = "/logout.do",method = RequestMethod.POST)
    public void  userLogout(){

    }
    @RequestMapping("checkstatus")
    @ResponseBody
    public void checkLogin(){

    }
}


