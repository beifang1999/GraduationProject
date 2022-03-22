package com.travel.statistics.domain.dws;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductResult implements Serializable {
    private static final long serialVersionUID = 8143821496952191752L;

    private String productId;
    private String title;

    private String level;

    private String type;

    private String travelTime;

    private long orderNumber;

    private long timeStamp;


    private double money;

    private long totalPeopleNum;
    private long totalAdultNum;
    private long totalYoungNum;
    private long totalBabyNum;

}
