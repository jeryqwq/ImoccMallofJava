package com.mall.pojo;

import org.joda.time.DateTime;

public class Comment {
    private Integer cId;
    private Integer productId;
    private Integer userId;
    private String username;
    private int cStarts;
    private String cContent;
    private DateTime cTime;

    public Integer getcId() {
        return cId;
    }

    public void setcId(Integer cId) {
        this.cId = cId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getcStarts() {
        return cStarts;
    }

    public void setcStarts(int cStarts) {
        this.cStarts = cStarts;
    }

    public String getcContent() {
        return cContent;
    }

    public void setcContent(String cContent) {
        this.cContent = cContent;
    }

    public DateTime getcTime() {
        return cTime;
    }

    public void setcTime(DateTime cTime) {
        this.cTime = cTime;
    }
}
