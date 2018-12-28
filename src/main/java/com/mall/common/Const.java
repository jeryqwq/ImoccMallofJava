package com.mall.common;

import com.google.common.collect.Sets;

import java.math.BigDecimal;
import java.util.Set;

public class Const {
    public  static  final String CURRENT_USER="currentUser";
    public  static  final String USERNAME="username";
    public  static  final String EMAIL="email";
    public  static final String FRONT_TAKEN="token_";
    public interface  productListOrderBy{
        Set<String> PRICE_ASC_DESC=Sets.newHashSet("price_desc","price_asc");
        Set<String> STOCK_ASC_DESC=Sets.newHashSet("stock_desc","stock_asc");
    }
    public  interface Cart{
        int CHECKED=1;
        int UN_CHECKED=0;
        String LIMIT_NUM_SUCCESS="LIMIT_NUM_SUCCESS";
        String LIMIT_NUM_FAIL="LIMIT_NUM_FAIL";
    }
    public  interface Role{
        int ROLE_ADMIN=0;
        int ROLE_CUSTOMER=1;
    }

    public  interface AliPayCallback{
String TRADE_STATUS_WAIT_BUYER_PAY="WAIT_BUYER_PAY";
String TRADE_STATUS_TRADE_CLOSED="TRADE_CLOSED";
String TRADE_STATUS_TRADE_SUCCESS="TRADE_SUCCESS";
String TRADE_STATUS_TRADE_FINISHED="TRADE_FINISHED";

    }
    public enum PayPlatFromEnum{
        //业务扩充更多支付方式
       ALiPay(1,"支付宝");
//        WeChatPay(2,"微信");

        private int code;
        private String status;

        PayPlatFromEnum(int code, String status) {
            this.code = code;
            this.status = status;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
    public enum OrderStatusEnum{
        CANCELED(0,"已取消"),
        NO_PAY(10,"未支付"),
        PAID(20,"已付款"),
        SHIPPED(30,"已发货"),
        SUCCESS(40,"订单已签收"),
        CLOSE(50,"订单已关闭");
        public  static  OrderStatusEnum codeOf(int code){
            for (OrderStatusEnum payType:values()) {
                if(payType.getCode()==code){
                    return payType;
                }
            }
            throw  new RuntimeException("没有找到对应的枚举");
        }
        private int code;
private String status;

        OrderStatusEnum(int code, String status) {
            this.code = code;
            this.status = status;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
public  enum  ProductStatusEnum{
    OFF_SALE(0,"下架"),
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


    public  enum  PaymentTypeEnum{
        ON_LINE_PAY(1,"在线当面付支付");
        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        PaymentTypeEnum(int code,String value){
            this.code=code;
            this.value=value;
        }
        public  static  PaymentTypeEnum codeOf(int code){
            for (PaymentTypeEnum payType:values()) {
                if(payType.getCode()==code){
                    return payType;
                }
            }
            throw  new RuntimeException("没有找到对应的枚举");
        }
    }
}
