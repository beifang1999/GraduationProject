package com.travel.producer.data.enumes;

import java.util.Arrays;
import java.util.List;

/**
*@Description 事件枚举，，，主要是针对产品的各种页面
**/
public enum EventEnum {

//    VIEW("view", "view","浏览"),
    CLICK("click", "click","点击");
//    INPUT("input", "input","输入"),
//    SLIDE("slide", "slide","滑动");


    private String code;
    private String desc;
    private String remark;

    private EventEnum(String code, String remark, String desc) {
        this.code = code;
        this.remark = remark;
        this.desc = desc;
    }

    public static List<String> getEvents(){
        List<String> events = Arrays.asList(
                CLICK.code
//                INPUT.code,
//                VIEW.code
        );
        return events;
    }

    public static List<String> getInterActiveEvents(){
        List<String> events = Arrays.asList(
                CLICK.code
//                VIEW.code,
//                SLIDE.code
        );
        return events;
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
