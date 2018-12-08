package com.mall.service;


import com.github.pagehelper.PageInfo;
import com.mall.common.ServerResponse;
import com.mall.vo.OrderVo;
import org.springframework.stereotype.Service;

import java.util.Map;


public interface IOrderService {
    ServerResponse pay(Integer userId, Long orderNo, String path);
    ServerResponse aLiPayCallback(Map<String,String> params);
    ServerResponse queryPayStatus(Integer userId,Long orderNo);
    ServerResponse createOrder(Integer userId,Integer shippingId);
    ServerResponse cancel(Integer userId,Long orderNo);
    ServerResponse getOrderCartProduct(Integer userId);
    ServerResponse orderDetail(Integer userId,Long orderNo);
    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);
    ServerResponse<OrderVo> manageDetail(Long orderNo);
    ServerResponse manageList(int pageNum,int pageSize);
    ServerResponse<PageInfo> manageSearch(Long orderNo,int pageNum, int pageSize);
    ServerResponse<String> sendGoods (Long orderNo);
}
