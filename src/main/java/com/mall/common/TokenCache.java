package com.mall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeUnit;

public class TokenCache {
    private static Logger logger=LoggerFactory.getLogger(TokenCache.class);
    //创建10000可存放键值对的cache，初始化1000个，有效期12小时
    private  static LoadingCache<String,String> localCache=CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12,TimeUnit.HOURS)
            .build(new CacheLoader<String, String>(){
                @Override
                public String load(String s){
return  "null";
                }
            });
    public  static void setKey(String key,String value){
localCache.put(key,value);
    }
    public static String getKey(String key){
        String value=null;
        try{
            value=localCache.get(key);
if("null".equals(value)){
return  null;
}
return  value;
        }catch (Exception e){
            logger.error("LocalCache get Error",e);
        }
        return  null;
    }
}
