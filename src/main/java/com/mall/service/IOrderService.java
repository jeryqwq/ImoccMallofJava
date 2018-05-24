package com.mall.service;


import com.mall.common.ServerResponse;
import org.springframework.stereotype.Service;

@Service("iOrderService")
public interface IOrderService {
    ServerResponse pay(Integer userId, Long orderNo, String path);

}
