package com.mall.service.Impl;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.dao.CartMapper;
import com.mall.dao.ProductMapper;
import com.mall.pojo.Cart;
import com.mall.pojo.Product;
import com.mall.service.ICartService;
import com.mall.util.BigDecimalUtil;
import com.mall.util.PropertiesUtil;
import com.mall.vo.CartProductVo;
import com.mall.vo.CartVo;
import net.sf.jsqlparser.schema.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
@Service("iCartService")
public class CartServiceImpl implements ICartService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CartMapper cartMapper;
    public ServerResponse<CartVo> addCart(Integer userId,Integer productId,Integer count) {
        if(productId==null||count==null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_GRGUMENT.getCode(),ResponseCode.ILLEGAL_GRGUMENT.getDesc());
        }
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
        CartVo cartVo=getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

public ServerResponse<CartVo> update(Integer userId,Integer productId,Integer count){
    if(productId==null||count==null){
        return  ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_GRGUMENT.getCode(),ResponseCode.ILLEGAL_GRGUMENT.getDesc());
    }
    Cart cart=cartMapper.selectCartByUserIdProductId(userId,productId);

    if(cart!=null){
        cart.setQuantity(count);
    }
    cartMapper.updateByPrimaryKeySelective(cart);
   CartVo cartVo=this.getCartVoLimit(userId);
    return  ServerResponse.createBySuccess(cartVo);

}
public ServerResponse<CartVo> list(Integer userId){
    CartVo cartVo=this.getCartVoLimit(userId);
    return  ServerResponse.createBySuccess(cartVo);
}

public ServerResponse<CartVo> deleteProduct(Integer userId,String productIds){
    List<String> productList = Splitter.on(",").splitToList(productIds);
    if(org.apache.commons.collections.CollectionUtils.isEmpty(productList)){
        return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_GRGUMENT.getCode(),ResponseCode.ILLEGAL_GRGUMENT.getDesc());
    }

    cartMapper.delByUserIdProductId(userId,productList);
    return this.list(userId);
}
    //Count For TotalPrice
    private CartVo getCartVoLimit(Integer userId){
        CartVo cartVo = new CartVo();
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        List<CartProductVo> cartProductVoList = Lists.newArrayList();

        BigDecimal cartTotalPrice = new BigDecimal("0");

        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(cartList)){
            for(Cart cartItem : cartList){
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(userId);
                cartProductVo.setProductId(cartItem.getProductId());

                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if(product != null){
                    cartProductVo.setProductMainImg(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProdcutStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());
                    //判断库存
                    int buyLimitCount = 0;
                    if(product.getStock() >= cartItem.getQuantity()){
                        //库存充足的时候
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    }else{
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        //购物车中更新有效库存
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }
                    cartProductVo.setQuantity(buyLimitCount);
                    //计算总价
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartProductVo.getQuantity()));
                    cartProductVo.setProductChecked(cartItem.getChecked());
                }

                if(cartItem.getChecked() == Const.Cart.CHECKED){
                    //如果已经勾选,增加到整个的购物车总价中
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductVo.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
            }
        }
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setAllChecked(this.getAllCheckedStatus(userId));
        cartVo.setImgHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));

        return cartVo;
        }

    public ServerResponse<Integer> getCartProductCount(Integer userId){
        if(userId == null){
            return ServerResponse.createBySuccess(0);
        }
        return ServerResponse.createBySuccess(cartMapper.selectCartProductCount(userId));
    }
    private boolean getAllCheckedStatus(Integer userId){
        if(userId == null){
            return false;
        }
        return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;

    }
    public ServerResponse<CartVo> selectOrUnSelect (Integer userId,Integer productId,Integer checked){
        cartMapper.checkedOrUncheckedProduct(userId,productId,checked);
        return this.list(userId);
    }
}
