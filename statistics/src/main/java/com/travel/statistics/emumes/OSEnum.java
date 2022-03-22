package com.travel.statistics.emumes;

import java.util.Arrays;
import java.util.List;


public enum OSEnum {

    ANDROID("Android", "安卓"),
    IOS("IOS", "苹果");


    private String code;
    private String desc;

    private OSEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static List<String> getOS(){
        List<String> oss = Arrays.asList(
                ANDROID.code,
                IOS.code
        );
        return oss;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
