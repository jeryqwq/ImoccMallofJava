package com.mall.vo;

import java.math.BigDecimal;

public class ProductListVo {
    private Integer id;
    private Integer categoryId;
    private String name;
    private String subtitle;
    private String mainImage;
    private BigDecimal price;
    private Integer status;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    public Integer getId() {
        return id;

    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getMainImage() {
        return mainImage;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getStatus() {
        return status;
    }

    public String getImageHost() {
        return imageHost;
    }

    private String imageHost;
}
