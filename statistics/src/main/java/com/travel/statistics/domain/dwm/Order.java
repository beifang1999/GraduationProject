package com.travel.statistics.domain.dwm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonAlias;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order implements Serializable {
    private static final long serialVersionUID = 717478043562111560L;
    @JsonAlias("travel_member_adult")
    private String travelMemberAdult;
    @JsonAlias("travel_member_baby")
    private String travelMemberBaby;
    @JsonAlias("product_traffic_grade")
    private String productTrafficGrade;
    @JsonAlias("travel_member_yonger")
    private String travelMemberYonger;
    @JsonAlias("product_traffic_type")
    private String productTrafficType;
    @JsonAlias("product_pub")
    private String productPub;
    @JsonAlias("order_ct")
    private String orderCt;
    @JsonAlias("user_id")
    private String userId;
    @JsonAlias("user_mobile")
    private String userMobile;
    @JsonAlias("has_activity")
    private String hasActivity;
    @JsonAlias("product_id")
    private String productId;
    @JsonAlias("product_traffic")
    private String productTraffic;
    @JsonAlias("order_id")
    private String orderId;



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
    private String productPrice;
    private String departureCode;
    private String desCityCode;


}
