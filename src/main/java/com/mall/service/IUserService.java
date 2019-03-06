package com.mall.service;

import com.mall.common.ServerResponse;
import com.mall.pojo.User;

public interface IUserService {
    ServerResponse<User> login(String username, String password);//
    ServerResponse<String> register(User user);//检查注册信息是否重复并注册
    ServerResponse<String> checkValid(String str,String type);//表单填写时动态检查用户名和邮箱
    ServerResponse<String> forgetAndQuestion(String username);
    ServerResponse<String> checkAnswer(String username,String question,String answer);
    ServerResponse<String> forgetResetPassword(String username,String newPassword,String userToken);
    ServerResponse<String> resetPassword(String newPassword,String oldPassword,User user);
    ServerResponse<User> updateInfo(User user);
    ServerResponse<User> getInfo(int userId);
    ServerResponse isAdmin (User user);
}
