package com.travel.statistics.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonAlias;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogMsgBase implements Serializable {
    private static final long serialVersionUID = -8919902067002051146L;
    //操作系统
    @JsonAlias({"os"})
    private String os;

    //经度
    @JsonAlias({"longitude"})
    private String longitude;
    //维度
    @JsonAlias({"latitude"})
    private String latitude;
//用户区域
    @JsonAlias({"userRegion"})
    private String userRegion;
//事件类型
    @JsonAlias({"eventType"})
    private String eventType;
//用户ID
    @JsonAlias({"userID"})
    private String userID;
//请求ID
    @JsonAlias({"sid"})
    private String sid;

    @JsonAlias({"manufacturer"})
    private String manufacturer;

    @JsonAlias({"duration"})
    private String duration;

    @JsonAlias({"ct"})
    private long timeStamp;

    @JsonAlias({"carrier"})
    private String carrier;

    @JsonAlias({"userRegionIP"})
    private String userRegionIP;

    @JsonAlias({"userDeviceType"})
    private String userDeviceType;

    @JsonAlias({"action"})
    private String action;

    @JsonAlias({"userDevice"})
    private String userDevice;

    @JsonAlias({"hotTarget"})
    private String hotTarget;

    @JsonAlias({"networkType"})
    private String networkType;

    @JsonAlias({"exts"})
    private String exts;


}
