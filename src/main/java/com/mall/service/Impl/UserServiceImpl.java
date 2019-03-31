package com.mall.service.Impl;
import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.common.TokenCache;
import com.mall.dao.UserMapper;
import com.mall.pojo.User;
import com.mall.service.IUserService;
import com.mall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private  UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        String md5Password=MD5Util.MD5EncodeUtf8(password);
int resultCount=userMapper.checkUserMame(username);

if(resultCount==0){
    return ServerResponse.createByErrorMessage("用户名不存在");
}
        User user=userMapper.selectLogin(username,md5Password);
if(user==null){
    return ServerResponse.createByErrorMessage("密码错误");
}
user.setPassword(StringUtils.EMPTY);
return ServerResponse.createBySuccess("登录成功!",user);

    }

public  ServerResponse<String> register(User user){
        ServerResponse ValidResponse=this.checkValid(user.getUsername(),Const.USERNAME);//使用固定值调用校验方法即可
if(!ValidResponse.isSuccess()){
    return ValidResponse;
}
ValidResponse=this.checkValid(user.getEmail(),Const.EMAIL);//使用固定值调用校验方法即可
    if(!ValidResponse.isSuccess()){
        return ValidResponse;
    }

user.setRole(Const.Role.ROLE_CUSTOMER);
user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
int resultCount=userMapper.insert(user);
if(resultCount==0){
    return ServerResponse.createBySuccessMessage("注册失败，请重试!");
}
    return ServerResponse.createBySuccess("注册成功");

}
    public ServerResponse<String> checkValid(String str,String type){
        if(StringUtils.isNotBlank(type)){
            //开始校验
            if(Const.USERNAME.equals(type)){
                int resultCount = userMapper.checkUserMame(str);
                if(resultCount > 0 ){
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            if(Const.EMAIL.equals(type)){
                int resultCount = userMapper.checkEmail(str);
                if(resultCount > 0 ){
                    return ServerResponse.createByErrorMessage("email已存在");
                }
            }
        }else{
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }


public ServerResponse<String> forgetAndQuestion(String username){
        ServerResponse validResponse=this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
return  ServerResponse.createByErrorMessage("用户不存在");
        }
        String question=userMapper.getQuestion(username);
        if(StringUtils.isNotBlank(question)){
            return  ServerResponse.createBySuccess(question);
        }
return  ServerResponse.createByErrorMessage("问题为空？");
}
    /*
    以键值对的形式存储已验证通过账户找回密码的用户，避免恶意用户直接调找回密码接口修改密码
     */
 public   ServerResponse<String> checkAnswer(String username,String question,String answer){
int resultCount=userMapper.checkAnswer(username,question,answer);
if(resultCount>0){
String forgetToken=UUID.randomUUID().toString();
    TokenCache.setKey(Const.FRONT_TAKEN+username,forgetToken);
    System.out.println(forgetToken);
    return ServerResponse.createBySuccess(forgetToken);
}
return  ServerResponse.createByErrorMessage("问题答案错误");
 }

 public ServerResponse<String> forgetResetPassword(String username,String newPassword,String userToken){

     if(StringUtils.isNotBlank(userToken)){
    ServerResponse.createByErrorMessage("Error Argument,The Token is not null");
}
     ServerResponse validResponse=this.checkValid(username,Const.USERNAME);
     if(validResponse.isSuccess()){
         return  ServerResponse.createByErrorMessage("用户不存在");
     }
     String token=TokenCache.getKey(Const.FRONT_TAKEN+username);

     if(StringUtils.isBlank(token)){
return ServerResponse.createByErrorMessage("无效Token,或者已过期！");
     }
     if(StringUtils.equals(token,userToken)){
String md5Password=MD5Util.MD5EncodeUtf8(newPassword);
int rowCount=userMapper.forgetResetPassword(username,md5Password);
if(rowCount>0){
    return ServerResponse.createBySuccessMessage("密码重置成功！");
}
     }else {
         return ServerResponse.createByErrorMessage("Token错误,请重新回答问题获取");
     }
     return ServerResponse.createByErrorMessage("密码修改失败!");
 }
 public ServerResponse<String> resetPassword(String newPassword,String oldPassword,User user){
int rowCount=userMapper.checkPassword(MD5Util.MD5EncodeUtf8(oldPassword),user.getId());
if(rowCount==0){
return  ServerResponse.createByErrorMessage("旧密码提交错误");
}

user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
int updateCount=userMapper.updateByPrimaryKeySelective(user);
if(updateCount>0){
    return ServerResponse.createBySuccessMessage("修改成功");
}
     return  ServerResponse.createByErrorMessage("密码修改失败!");
 }


public  ServerResponse<User> updateInfo(User user){
int resultCount=userMapper.checkEmailByUserId(user.getEmail(),user.getId());
if(resultCount>0){
    return ServerResponse.createByErrorMessage("该邮箱已被注册");
}
User updateUser=new User();
updateUser.setId(user.getId());
updateUser.setEmail(user.getEmail());
updateUser.setPhone(user.getPhone());
updateUser.setQuestion(user.getQuestion());
updateUser.setAnswer(user.getAnswer());
resultCount=userMapper.updateByPrimaryKeySelective(updateUser);
if(resultCount>0){
    return ServerResponse.createBySuccess("修改成功",updateUser);
}
return ServerResponse.createByErrorMessage("更新信息失败！");
}
   public ServerResponse<User> getInfo(int userId){
User user=userMapper.selectByPrimaryKey(userId);
if(user==null){
    return ServerResponse.createByErrorMessage("未找到该用户");
}
user.setPassword("");
return  ServerResponse.createBySuccess(user);
   }


/*
/Admin
 */
public  ServerResponse isAdmin (User user){
    if(user!=null&&user.getRole()==Const.Role.ROLE_ADMIN){
        return ServerResponse.createBySuccess();
    }
    return ServerResponse.createByError();

}
}
