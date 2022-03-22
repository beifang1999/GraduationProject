package com.travel.statistics.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JobParamsKafka extends JobParamsBase implements Serializable {
    private static final long serialVersionUID = -6122892767334474346L;

    private String bootstrapServerIn;

    private String bootstrapServerOut;

    private String groupId;

    private String topicIn;

    private String topicOut;

    //log 分流topic
    private String topicLaunch;
    private String topicPageView;
    private String topicClick;

    //log dwm层
    private String topicClickWide;

    //dws层 topic
    private String topicTraffic;
    private String topicWord;
    private String topicPub;
    private String topicRegion;

    private String topicMetroStation;

    private String topicMetro;


}
