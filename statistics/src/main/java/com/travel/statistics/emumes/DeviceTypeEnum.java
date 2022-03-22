package com.travel.statistics.emumes;

import java.util.Arrays;
import java.util.List;


public enum DeviceTypeEnum {

    PHONE("phone","phone"),
    PC("PC", "other");  //可以将其看着是pc


    private String code;
    private String desc;

    private DeviceTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public static List<String> getDeviceTypes(){
        List<String> actions = Arrays.asList(
                PHONE.code,
                PC.code
        );
        return actions;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
