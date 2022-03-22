package com.travel.statistics.udf.map;

import com.travel.statistics.domain.dwm.Order;
import com.travel.statistics.domain.dws.OrderTranslation;
import org.apache.flink.api.common.functions.MapFunction;

import java.math.BigDecimal;

public class OrderMapFunction implements MapFunction<Order, OrderTranslation> {

    private OrderTranslation result = new OrderTranslation();

    @Override
    public OrderTranslation map(Order order) {
        result.setOrderId(order.getOrderId());
        result.setTravelMemberAdult(Long.parseLong(order.getTravelMemberAdult()));
        result.setTravelMemberYounger(Long.parseLong(order.getTravelMemberYonger()));
        result.setTravelMemberBaby(Long.parseLong(order.getTravelMemberBaby()));
        result.setProductTraffic(order.getProductTraffic());
        result.setProductTrafficGrade(order.getProductTrafficGrade());
        result.setProductTrafficType(order.getProductTrafficType());

        result.setTimeStamp(Long.parseLong(order.getOrderCt()));
        result.setUserId(order.getUserId());
        result.setUserMobile(Long.parseLong(order.getUserMobile()));
        double discount = BigDecimal.valueOf(Double.parseDouble(order.getHasActivity())).divide(BigDecimal.valueOf(10)).doubleValue();
        result.setDisCount(discount);
        if (order.getProductPub() != null) {
            result.setPubId(order.getProductPub());
            result.setPubName(order.getPubName());
            result.setPubStar(order.getPubStar());
            result.setPubGrade(order.getPubGrade());
            result.setPubGradeDesc(order.getPubGradeDesc());
            result.setPubAreaCode(order.getPubAreaCode());
            result.setPubAddress(order.getPubAddress());
            result.setIsNational(order.getIsNational());
        }
        result.setProductId(order.getProductId());
        result.setProductTitle(order.getProductTitle());
        result.setProductLevel(order.getProductLevel());
        result.setProductType(order.getProductType());
        result.setTravelDay(order.getTravelDay());
        result.setProductPrice(Double.parseDouble(order.getProductPrice()));
        result.setDepartureCode(order.getDepartureCode());
        result.setDesCityCode(order.getDesCityCode());

        if (discount == 0){
            discount = 1 ;
        }
        //实际金额 = 折扣*价格
        result.setActualPayment(BigDecimal.valueOf(discount).multiply(BigDecimal.valueOf(Double.parseDouble(order.getProductPrice()))).doubleValue());
        return result;
    }
}
