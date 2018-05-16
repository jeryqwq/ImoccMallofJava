package com.mall.dao;
import com.mall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    int checkUserMame(String username);
    User selectLogin(@Param("username")String username,@Param("password")String password);
    int checkEmail(String email);
    String getQuestion(String username);
    int checkAnswer(@Param("username")String username,@Param("question")String question,@Param("answer")String answer);
    int forgetResetPassword(@Param("username")String username,@Param("newPassword")String newPassword);
    int checkPassword(@Param("userPassword")String username,@Param("userId")int userId);
    int checkEmailByUserId(@Param("email") String email,@Param("userId") int userId);
}