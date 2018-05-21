package com.mall.common;

import com.google.common.collect.Sets;

import java.util.Set;

public class Const {
    public  static  final String CURRENT_USER="currentUser";
    public  static  final String USERNAME="username";
    public  static  final String EMAIL="email";
    public  static final String FRONT_TAKEN="token_";
    public interface  productListOrderBy{
        Set<String> PRICE_ASC_DESC=Sets.newHashSet("price_desc","price_asc");
    }
    public  interface Cart{
        int CHECKED=1;
        int UN_CHECKED=0;
    }
    public  interface Role{
        int ROLE_ADMIN=0;
        int ROLE_CUSTOMER=1;
    }
public  enum  ProductStatusEnum{
    ON_SALE(1,"在线");
    private String value;
        private int code;

    public String getValue() {
        return value;
    }

    public int getCode() {
        return code;
    }

    ProductStatusEnum(int code,String value){
        this.code=code;
        this.value=value;
    }
}
}
