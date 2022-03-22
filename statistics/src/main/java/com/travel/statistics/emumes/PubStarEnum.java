package com.travel.statistics.emumes;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public enum PubStarEnum {


    FIVE_STAR("5", "五星级"),
    FOUR_STAR("4", "四星级"),
    THREE_STAR("3", "三星级"),
    TWO_STAR("2", "二星级");

    private String code;
    private String remark;


    public static String getRemark(String code) {
        for (PubStarEnum v : PubStarEnum.values()) {
            if (Objects.equals(v.getCode(), code)) {
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
