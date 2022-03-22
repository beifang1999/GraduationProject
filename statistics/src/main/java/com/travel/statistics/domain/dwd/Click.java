package com.travel.statistics.domain.dwd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonAlias;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Click {
    //操作系统
    private String os;
    private String longitude;
    private String latitude;
    private String userRegion;
    private String eventType;
    private String userID;
    private String sid;
    private String manufacturer;
    private String duration;
    private long timeStamp;
    private String carrier;

    private String userDeviceType;
    private String action;
    private String userDevice;
    private String networkType;

    @JsonAlias("eventTargetType")
    private String eventTargetType;

    @JsonAlias("targetID")
    private String targetID;

}
