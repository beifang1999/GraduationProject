package com.travel.statistics.emumes;

import java.util.Arrays;
import java.util.List;

/**
*@Author 东哥
*@Company 千锋好程序员大数据
*@Date 2020/3/26 0026
*@Description 通常是交通工具选择是类型
**/
public enum TrafficGoBackEnum {
    SINGLE("single", "single","单程"),
    GOBACK("return", "goback","往返");


    private String code;
    private String desc;
    private String remark;

    private TrafficGoBackEnum(String code, String remark, String desc) {
        this.code = code;
        this.remark = remark;
        this.desc = desc;
    }


    public static List<String> getAllTrafficGoBacks(){
        List<String> traffics = Arrays.asList(
                SINGLE.code,
                GOBACK.code
        );
        return traffics;
    }


    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getRemark() {
        return remark;
    }
}
