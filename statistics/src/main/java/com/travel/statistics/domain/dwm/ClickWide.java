package com.travel.statistics.domain.dwm;

import com.travel.statistics.domain.dwd.Click;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClickWide implements Serializable {
    private static final long serialVersionUID = -3259896412077325772L;
    //产品
    private Click click;

    //产品
    private String productTitle;
    private String productLevel;
    private String productType;
    private String travelDay;
    private String productPrice;
    private String departureCode;
    private String desCityCode;

    private long value = 1l;
}
