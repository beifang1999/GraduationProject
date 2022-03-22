package com.travel.statistics.domain.dws;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionTranslation extends OrderTranslation{

    private String desCityName;
    private String desProvinceCode;
    private String desProvinceName;
    private String desISOCode;
    private String departureCityName;
    private String departureProvinceCode;
    private String departureProvinceName;
    private String departureISOCode;

}
