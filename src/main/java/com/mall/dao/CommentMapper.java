package com.mall.dao;
import com.mall.pojo.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentMapper {
    List<Comment> selectAllCommentByProductId(@Param("productId")Integer productId);
    int insertComment(@Param("comment")Comment comment);
}
