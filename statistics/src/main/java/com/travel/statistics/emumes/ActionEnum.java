package com.travel.statistics.emumes;

import java.util.Arrays;
import java.util.List;

public enum ActionEnum {

    INSTALL("install", "install","安装"),
    LAUNCH("launch", "launch","加载"),
    LOGIN("login", "login","登录"),
    REGISTER("register", "register","注册"),
    INTERACTIVE("interactive", "interactive","交互行为"),
    EXIT("exit", "exit","退出"),
    PAGE_ENTER_H5("page_enter_h5", "page_enter_h5","页面进入"),
    PAGE_ENTER_NATIVE("page_enter_native", "page_enter_native","页面进入");
    //PAGE_EXIT("page_exit","页面退出");


    private String code;
    private String desc;
    private String remark;

    private ActionEnum(String code, String remark, String desc) {
        this.code = code;
        this.remark = remark;
        this.desc = desc;
    }


    public static List<String> getActions(){
        List<String> actions = Arrays.asList(
                INSTALL.code,
                LAUNCH.code,
                //LOGIN.code,
                //REGISTER.code,
                INTERACTIVE.code,
                EXIT.code,
                PAGE_ENTER_H5.code,
                PAGE_ENTER_NATIVE.code
        );
        return actions;
    }

    public static List<String> getMidActions(){
        List<String> actions = Arrays.asList(
                LAUNCH.code,
                INTERACTIVE.code,
                //PAGE_ENTER_H5.code,
                PAGE_ENTER_NATIVE.code
        );
        return actions;
    }

    public static List<String> getMidActionRemarks(){
        List<String> actions = Arrays.asList(
                LAUNCH.remark,
                INTERACTIVE.remark,
                //PAGE_ENTER_H5.remark,
                PAGE_ENTER_NATIVE.remark
        );
        return actions;
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
