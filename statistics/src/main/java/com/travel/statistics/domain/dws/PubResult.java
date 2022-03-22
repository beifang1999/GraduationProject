package com.travel.statistics.domain.dws;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PubResult implements Serializable {
    private static final long serialVersionUID = -3397816348578472424L;

    private long timeStamp;
    private String pubId;
    private String name;
    private String star;
    private String grade;
    private String address;

    private double money;

    private long orderNumber;

    private long totalPeopleNum;
    private long totalAdultNum;
    private long totalYoungNum;
    private long totalBabyNum;

}
