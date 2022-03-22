package com.travel.statistics.udf.map.dws;


import com.travel.statistics.domain.dws.OrderTranslation;
import com.travel.statistics.domain.dws.RegionTranslation;
import org.apache.flink.api.common.functions.MapFunction;

public class OrderRegionMapFunction implements MapFunction<OrderTranslation, RegionTranslation> {
    private static final long serialVersionUID = 4901430982774580995L;

    @Override
    public RegionTranslation map(OrderTranslation value) {
        RegionTranslation result = new RegionTranslation();
        result.setTimeStamp(value.getTimeStamp());
        result.setTravelMemberAdult(value.getTravelMemberAdult());
        result.setTravelMemberYounger(value.getTravelMemberYounger());
        result.setTravelMemberBaby(value.getTravelMemberBaby());


        result.setProductId(value.getProductId());
        result.setProductTraffic(value.getProductTraffic());
        result.setProductTrafficGrade(value.getProductTrafficGrade());
        result.setProductTrafficType(value.getProductTrafficType());

        result.setUserId(value.getUserId());
        result.setUserMobile(value.getUserMobile());
        result.setDisCount(value.getDisCount());
        result.setOrderId(value.getOrderId());

        if (value.getPubId() != null) {
            result.setPubId(value.getPubId());
            result.setPubName(value.getPubName());
            result.setPubStar(value.getPubStar());
            result.setPubGrade(value.getPubGrade());
            result.setPubGradeDesc(value.getPubGradeDesc());
            result.setPubAreaCode(value.getPubAreaCode());
            result.setPubAddress(value.getPubAddress());
            result.setIsNational(value.getIsNational());
        }

        result.setProductTitle(value.getProductTitle());
        result.setProductLevel(value.getProductLevel());
        result.setProductType(value.getProductType());
        result.setTravelDay(value.getTravelDay());
        result.setProductPrice(value.getProductPrice());
        result.setDepartureCode(value.getDepartureCode());
        result.setDesCityCode(value.getDesCityCode());

        result.setActualPayment(value.getActualPayment());
        result.setValue(value.getValue());
        return result;
    }
}
