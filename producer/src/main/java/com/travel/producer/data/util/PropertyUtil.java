package com.travel.producer.data.util;


import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

/**
*@Description property配置文件工具类
**/
public class PropertyUtil implements Serializable{

    private static Logger log = LoggerFactory.getLogger(PropertyUtil.class);

    public static final String PROPERTY_FILTER = ".properties";

    /**
     * 读取资源文件
     * @param proPath
     * @return
     */
    public static Properties readProperties(String proPath){
        Validate.notEmpty(proPath, "properties is empty");

        Properties properties = null;
        InputStream is = null;
        try{
            is = PropertyUtil.class.getClassLoader().getResourceAsStream(proPath);
            properties = new Properties();
            properties.load(is);
        }catch(IOException ioe){
            log.error("loadProperties4Redis:" + ioe.getMessage());
        }finally {
            try{
                if(null != is){
                    is.close();
                }}catch (Exception e){
                e.printStackTrace();
            }
        }
        return properties;
    }


    public static boolean getProperty(Properties props, String key) {
        Boolean result = null;
        try{
            result =  Boolean.parseBoolean(props.getProperty(key).trim());
        }catch(Exception e){
            log.error("PropertiesUtil.getProperty4Int:" + e.getMessage());
        }
        return result;
    }



}
