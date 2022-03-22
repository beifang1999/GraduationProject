package com.travel.statistics.domain.dws;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrafficResult implements Serializable {
    private static final long serialVersionUID = 415624675905079786L;

    private long timeStamp;
    private String productTraffic;
    private String productTrafficGrade;
    private String productTrafficType;
    private long value;
}
