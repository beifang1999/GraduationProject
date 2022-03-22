package com.travel.statistics.domain.dwm;

import com.travel.statistics.domain.dwd.Launch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LaunchWide implements Serializable {
    private static final long serialVersionUID = 2009563504052008896L;

    private Launch launch;


    private String cityName;
    private String provinceCode;
    private String provinceName;
    private String isoCode;

    private long value = 1L;

}
