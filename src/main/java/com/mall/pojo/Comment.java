package com.mall.pojo;

import java.util.Date;

public class Comment {
    private Integer cId;
    private Integer productId;
    private Integer userId;
    private Integer toId;
    private String username;
    private Integer cStarts;
    private String cContent;
    private Date cTime;
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

    public Integer getToId() {
        return toId;
    }

    public void setToId(Integer toId) {
        this.toId = toId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getcStarts() {
        return cStarts;
    }

    public void setcStarts(Integer cStarts) {
        this.cStarts = cStarts;
    }

    public String getcContent() {
        return cContent;
    }

    public void setcContent(String cContent) {
        this.cContent = cContent;
    }

    public Date getcTime() {
        return cTime;
    }

    public void setcTime(Date cTime) {
        this.cTime = cTime;
    }
    public Comment() {
        super();
    }
    public Comment(Integer cId, Integer productId, Integer userId, Integer toId, String username, Integer cStarts, String cContent, Date cTime) {
        this.cId = cId;
        this.productId = productId;
        this.userId = userId;
        this.toId = toId;
        this.username = username;
        this.cStarts = cStarts;
        this.cContent = cContent;
        this.cTime = cTime;
    }
}
