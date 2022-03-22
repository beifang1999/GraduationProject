package com.travel.statistics.emumes;

import java.util.Arrays;
import java.util.List;

/**
*@Author 东哥
*@Company 千锋好程序员大数据
*@Date 2020/3/26 0026
*@Description 手机生产商枚举 ---使用什么样类型的手机
**/
public enum ManufacturerEnum {

    HUAWEI("华为", "华为"),
    OPPO("oppo", "oppo"),
    VIVO("ViVo", "vivo"),
    XIAOMI("小米", "小米"),
    SAMSUNG("三星", "三星"),
    APPLE("苹果", "苹果"),
    LENOVO("联想", "联想"),
    SONY("索尼", "索尼"),
    NOKIA("诺基亚", "诺基亚"),
    ZTE("中信", "中信");


    private String code;
    private String desc;

    private ManufacturerEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static List<String> getManufacturers(){
        List<String> manufacturers = Arrays.asList(
                HUAWEI.code,
                OPPO.code,
                VIVO.code,
                XIAOMI.code,
                SAMSUNG.code,
                LENOVO.code,
                SONY.code,
                NOKIA.code,
                ZTE.code
        );
        return manufacturers;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
