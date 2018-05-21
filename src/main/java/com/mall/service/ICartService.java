package com.mall.service;

import com.mall.common.ServerResponse;

public interface ICartService {
    ServerResponse addCart(Integer userId, Integer productId, Integer count);
}
