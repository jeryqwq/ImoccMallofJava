package com.mall.dao;

import com.mall.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order selectByUserIdOrderNo(@Param("userId")Integer userId,@Param("orderNo")Long orderNo);

    Order selectOrderByOrderNo(Long orderNo);

List<Order> selectAllOrder();
List<Order> getOrderListByUserId(Integer userId);
String sendGoodByOrderNo(@Param("orderNo")Long orderNo,@Param("stats") int stats);
}