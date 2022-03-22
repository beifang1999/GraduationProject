package com.travel.producer.data.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Launch {
    private String os;
    private String longitude;
    private String latitude;
    private String userRegion;
    private String userID;
    private String sid;
    private String manufacturer;
    private String duration;
    private String ct;
    private String carrier;
    private String userRegionIp;
    private String userDeviceType;
    private String action;
    private String userDevice;
    private String networkType;
}
