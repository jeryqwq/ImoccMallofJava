package com.mall.common;

import java.io.Serializable;

public class ServerResponse<T> implements Serializable {
private  int status;
private String msg;
private T data;
private ServerResponse(int status){
    this.status=status;
}
private ServerResponse(int status,String msg){
this.status=status;
this.msg=msg;
}
private ServerResponse(int status,String msg,T data){
    this.status=status;
    this.msg=msg;
    this.data=data;
}
    private ServerResponse(int status,T data){
        this.status=status;
        this.data=data;
    }
    public  Boolean isSuccess(){
    return  this.status==ResponseCode.SUCCESS.getCode();
    }
    public  int getStatus(){
    return this.status;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }
    public  static  <T> ServerResponse createSuccess(){
    return  new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }
    public static  <T> ServerResponse createSuccess(String msg){
return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }
public  static <T> ServerResponse createSuccess(T data){
    return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
}
public static <T> ServerResponse createSuccess (String msg,T data){
    return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
}
}
