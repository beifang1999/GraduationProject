package com.travel.producer.data.enumes;

import java.util.Arrays;
import java.util.List;

/**
*@Description 访问应用使用的网络类型枚举
**/
public enum NetworkTypeEnum {

    WIFI("WIFI", "无线"),
    D4G("4G", "4g"),
    D3G("5G", "5g"),
    OFFLINE("线下支付", "线下支付");



    private String code;
    private String desc;

    private NetworkTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static List<String> getNetworkTypes(){
        List<String> networkTypes = Arrays.asList(
                D4G.code,
                D3G.code,
                WIFI.code,
                OFFLINE.code
        );
        return networkTypes;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
