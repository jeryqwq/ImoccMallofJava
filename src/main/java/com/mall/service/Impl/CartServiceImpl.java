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
List<String> productList=Splitter.on(",").splitToList(productIds);
if(CollectionUtils.isEmpty(productList)){
    return  ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_GRGUMENT.getCode(),ResponseCode.ILLEGAL_GRGUMENT.getDesc());
}
cartMapper.delByUserIdProductId(userId,productList);
    CartVo cartVo=this.getCartVoLimit(userId);
    return  ServerResponse.createBySuccess(cartVo);
}
    //Count For TotalPrice
    private CartVo getCartVoLimit(Integer userId){
        CartVo cartVo =new CartVo();
        List<Cart> cartList =cartMapper.selectCartByUserId(userId);
        List<CartProductVo> cartProductVoList=Lists.newArrayList();
        BigDecimal cartTotalPrice=new BigDecimal("0");
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(cartList)){
            for (Cart cartItem:cartList) {
                CartProductVo cartProductVo=new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setProductId(cartItem.getProductId());
                cartProductVo.setUserId(cartItem.getUserId());
                Product product=productMapper.selectByPrimaryKey(cartItem.getProductId());
                if(product!=null){
                    cartProductVo.setProductMainImg(product.getMainImage());
                    cartProductVo.setProdcutStatus(product.getStatus());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStock(product.getStock());
                    cartProductVo.setProductPrice(product.getPrice());
                    int buyLimitCount=0;
                    if(product.getStock()>cartProductVo.getProductStock()){
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                        buyLimitCount=product.getStock();
                    }else {
                        buyLimitCount=product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        Cart cartForUpdate=new Cart();
                        cartForUpdate.setId(product.getId());
                        cartForUpdate.setQuantity(buyLimitCount);
                        int rowCount=cartMapper.updateByPrimaryKeySelective(cartForUpdate);
                        if(rowCount>0){

                        }
                    }
                    cartProductVo.setQuantity(buyLimitCount);
                    cartProductVo.setProducTotailPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartProductVo.getQuantity()));
                    cartProductVo.setProductChecked(cartItem.getChecked());
                }
                if(cartItem.getChecked()==Const.Cart.CHECKED){
                    cartTotalPrice=BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductVo.getProducTotailPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
            }
        }
        cartVo.setProductTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setAllChecked(isAllChoose(userId));
        cartVo.setImgHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return cartVo ;
    }




    private boolean isAllChoose(Integer userId){
        return cartMapper.selectCartProductCheckedStatusByUserId(userId)==0;
    }
}
