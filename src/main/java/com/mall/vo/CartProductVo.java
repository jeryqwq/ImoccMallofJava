package com.mall.vo;

import java.math.BigDecimal;

public class CartProductVo {
    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer quantity;
private  String productName;
private String productSubtitle;
private  String productMainImg;
private BigDecimal productPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSubtitle() {
        return productSubtitle;
    }

    public void setProductSubtitle(String productSubtitle) {
        this.productSubtitle = productSubtitle;
    }

    public String getProductMainImg() {
        return productMainImg;
    }

    public void setProductMainImg(String prodcutMainImg) {
        this.productMainImg = prodcutMainImg;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getProdcutStatus() {
        return prodcutStatus;
    }

    public void setProdcutStatus(Integer prodcutStatus) {
        this.prodcutStatus = prodcutStatus;
    }

    public BigDecimal getProducTotailPrice() {
        return producTotailPrice;
    }

    public void setProducTotailPrice(BigDecimal producTotailPrice) {
        this.producTotailPrice = producTotailPrice;
    }

    public Integer getProductStock() {
        return productStock;
    }

    public void setProductStock(Integer productStock) {
        this.productStock = productStock;
    }

    public Integer getProductChecked() {
        return productChecked;
    }

    public void setProductChecked(Integer productChecked) {
        this.productChecked = productChecked;
    }

    public String getLimitQuantity() {
        return limitQuantity;
    }

    public void setLimitQuantity(String limitQuantity) {
        this.limitQuantity = limitQuantity;
    }

    private Integer prodcutStatus;
private BigDecimal producTotailPrice;
private Integer productStock;
private Integer productChecked;
private  String limitQuantity;


}
