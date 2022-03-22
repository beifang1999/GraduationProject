package com.travel.statistics.domain.dws;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegionResult {

    private long timeStamp;
    private String departureCode;
    private String desCityCode;

    private String desCityName;
    private String desProvinceCode;
    private String desProvinceName;
    private String desISOCode;
    private String departureCityName;
    private String departureProvinceCode;
    private String departureProvinceName;
    private String departureISOCode;


    private double money;

    private long orderNumber;
    private long productNumber;
    private long pubNumber;


    private long travelMemberAdult;
    private long travelMemberYounger;
    private long travelMemberBaby;
    private long totalPeopleNumber;

}
