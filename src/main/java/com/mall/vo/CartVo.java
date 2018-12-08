package com.mall.vo;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.math.BigDecimal;
import java.util.List;

public class CartVo {
    private List<CartProductVo> cartProductVoList;
    private String imgHost;
    private BigDecimal productTotalPrice;
    private Boolean isAllChecked;
    public List<CartProductVo> getCartProductVoList() {
        return cartProductVoList;
    }
private BigDecimal cartTotalPrice;

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public void setCartProductVoList(List<CartProductVo> cartProductVoList) {
        this.cartProductVoList = cartProductVoList;
    }

    public String getImgHost() {
        return imgHost;
    }

    public void setImgHost(String imgHost) {
        this.imgHost = imgHost;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public Boolean getAllChecked() {
        return isAllChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        isAllChecked = allChecked;
    }


}
