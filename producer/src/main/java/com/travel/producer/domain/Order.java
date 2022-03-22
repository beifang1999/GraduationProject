package com.travel.producer.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {
    @JsonAlias("travel_member_adult")
    private String travelMemberAdult;
    @JsonAlias("travel_member_baby")
    private String travelMemberBaby;
    @JsonAlias("user_region")
    private String userRegion;

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
}
