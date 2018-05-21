package com.mall.controller.portal;


import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.dao.CartMapper;
import com.mall.pojo.Cart;
import com.mall.pojo.User;
import com.mall.service.ICartService;
import com.mall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private  ICartService iCartService;
    @RequestMapping(value = "/addCart",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Cart> addCart(HttpSession session,Integer productId,Integer count){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
if(user==null){
    return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
}
return null;
    }

    private CartVo getCartVoLimit(Integer userId){
        CartVo cartVo =new CartVo();
List<Cart> cartVoList =cartMapper.selectCartByUserId(userId);
return  null;
    }
}
