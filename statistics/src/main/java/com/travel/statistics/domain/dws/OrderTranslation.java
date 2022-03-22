package com.travel.statistics.domain.dws;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderTranslation implements Serializable {

    private static final long serialVersionUID = -6109726102208504443L;

    private long timeStamp;
    private long travelMemberAdult;
    private long travelMemberYounger;
    private long travelMemberBaby;

    private String productId;
    private String productTraffic;
    private String productTrafficGrade;
    private String productTrafficType;


    private String userId;
    private long userMobile;
    private double disCount;


    private String orderId;

    private String pubId;
    private String pubName;
    private String pubStar;
    private String pubGrade;
    private String pubGradeDesc;
    private String pubAreaCode;
    private String pubAddress;
    private String isNational;

    //产品
    private String productTitle;
    private String productLevel;
    private String productType;
    private String travelDay;
    private double productPrice;
    private String departureCode;
    private String desCityCode;

    private double actualPayment;

    private long value = 1;

}
