package com.travel.statistics.emumes;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor

public enum ProductLevelEnum {
    PLATINUM("4","platinum"),
    MASONRY("5","masonry");
    private String code;
    private String remark;

    public static String getRemark(String code){
        for ( ProductLevelEnum v : ProductLevelEnum.values()){
            if (Objects.equals(v.getCode(), code)){
                return v.getRemark();
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }



    public String getRemark() {
        return remark;
    }

}
