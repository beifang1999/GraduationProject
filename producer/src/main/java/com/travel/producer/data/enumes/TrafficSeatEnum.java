package com.travel.producer.data.enumes;

import java.util.Arrays;
import java.util.List;

/**
*@Description 交通工具的席别和舱位
**/
public enum TrafficSeatEnum {
    //座席类别(商务座、一等、二等、软卧、硬卧、硬座)
    //舱位(头等舱、经济舱)

    TRAIN_GD_BUSY("business_seat", "tr_busyseat","商务座"),
    TRAIN_GD_FIRST("first_class_seat", "tr_firstseat","一等"),
    TRAIN_GD_SECOND("second_class_seat", "tr_secondseat","二等"),

    TRAIN_SOFT_BERTH("soft_sleeper", "tr_secondseat","软卧"),
    TRAIN_HAND_BERTH("tourist_car", "tr_secondseat","硬卧"),

    AIRPLAT_BUSY("first_class", "ap_busyseat","头等舱"),
    AIRPLAT_COMMON("economy_class", "ap_common","经济舱");


    private String code;
    private String desc;
    private String remark;

    private TrafficSeatEnum(String code, String remark, String desc) {
        this.code = code;
        this.remark = remark;
        this.desc = desc;
    }


    public static List<String> getAllTrafficSeats(){
        List<String> traffics = Arrays.asList(
                TRAIN_GD_BUSY.code,
                TRAIN_GD_FIRST.code,
                TRAIN_GD_SECOND.code,
                TRAIN_SOFT_BERTH.code,
                TRAIN_HAND_BERTH.code,

                AIRPLAT_BUSY.code,
                AIRPLAT_COMMON.code
        );
        return traffics;
    }

    public static List<String> getTrainTrafficSeats(){
        List<String> traffics = Arrays.asList(
                TRAIN_GD_BUSY.code,
                TRAIN_GD_FIRST.code,
                TRAIN_GD_SECOND.code

        );
        return traffics;
    }
    public static List<String> getTrainSeats(){
        List<String> traffics = Arrays.asList(
                TRAIN_SOFT_BERTH.code,
                TRAIN_HAND_BERTH.code

        );
        return traffics;
    }
    public static List<String> getAirTrafficSeats(){
        List<String> traffics = Arrays.asList(
                AIRPLAT_BUSY.code,
                AIRPLAT_COMMON.code
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
