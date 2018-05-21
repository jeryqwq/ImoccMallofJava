package com.mall.service.Impl;
import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.dao.CartMapper;
import com.mall.pojo.Cart;
import com.mall.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;

public class CartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;
    public ServerResponse addCart(Integer userId,Integer productId,Integer count) {
        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        if (cart == null) {
Cart itemCart=new Cart();
itemCart.setQuantity(count);
itemCart.setChecked(Const.Cart.CHECKED);
itemCart.setProductId(productId);
itemCart.setUserId(userId);
cartMapper.insert(itemCart);
        }else {
            count=cart.getQuantity()+count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
return  null;
    }

}
