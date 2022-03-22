package com.travel.statistics.emumes;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public enum  ProductTypeEnum {


    FOLLOW_TEAM("Y006", "follow"),
    PRIVATE("Y002", "privateTourism");
    private String code;
    private String remark;

    public  static String getRemark(String code) {
        for (ProductTypeEnum v : ProductTypeEnum.values()){
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
