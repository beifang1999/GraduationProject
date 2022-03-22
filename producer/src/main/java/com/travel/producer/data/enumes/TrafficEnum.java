package com.travel.producer.data.enumes;

import java.util.Arrays;
import java.util.List;

/**
*@Description 旅游选择的交通工具类型枚举
**/
public enum TrafficEnum {
    GTRAIN("high_speed_rail", "train","高铁"),
    CTRAIN("train", "train","火车"),
    AIRPLAN("plane", "plane","飞机"),
    STEAMSHIP("steamship", "steamship","轮船");


    private String code;
    private String desc;
    private String remark;

    private TrafficEnum(String code, String remark, String desc) {
        this.code = code;
        this.remark = remark;
        this.desc = desc;
    }


    public static List<String> getAllTraffics(){
        List<String> traffics = Arrays.asList(
                GTRAIN.code,
                CTRAIN.code,
                AIRPLAN.code,
                STEAMSHIP.code
        );
        return traffics;
    }

    public static List<String> getCommonTraffics(){
        List<String> traffics = Arrays.asList(
                GTRAIN.code,
                CTRAIN.code,
                AIRPLAN.code
        );
        return traffics;
    }

    public static List<String> getGoodTraffics(){
        List<String> traffics = Arrays.asList(
                GTRAIN.code,
                AIRPLAN.code
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
