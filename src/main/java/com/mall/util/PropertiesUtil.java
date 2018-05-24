package com.mall.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesUtil {
    private static Logger logger=LoggerFactory.getLogger(PropertiesUtil.class);
    private static Properties props;
    static {
        props=new Properties();
        try {
            props.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream("mmall.properties"),"UTF-8"));
        } catch (IOException e) {
            logger.error("配置读取异常",e);
        }
    }

    public  static String getProperty(String key){
        String value=props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            return  value;
        }
        return value.trim();
    }
    public  static String getProperty(String key,String defaultValue){
        String value=props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
          value=defaultValue;
        }
        return value.trim();
    }
}
