package com.travel.statistics.domain.dwd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Launch implements Serializable {
    private static final long serialVersionUID = -8208290777536502682L;

    private String os;
    private String longitude;
    private String latitude;
    private String userRegion;
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
}
