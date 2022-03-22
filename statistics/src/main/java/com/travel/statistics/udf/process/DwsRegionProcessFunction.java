package com.travel.statistics.udf.process;


import com.travel.statistics.domain.dws.OrderTranslation;
import com.travel.statistics.domain.dws.RegionResult;
import com.travel.statistics.domain.dws.RegionTranslation;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DwsRegionProcessFunction extends ProcessWindowFunction<RegionTranslation, RegionResult, String, TimeWindow> {
    private static final long serialVersionUID = 8287193986124142562L;

    @Override
    public void process(String key, Context context, Iterable<RegionTranslation> elements, Collector<RegionResult> out) {

        RegionResult result = new RegionResult();

        List<RegionTranslation> orderTranslationList = StreamSupport.stream(elements.spliterator(), false).collect(Collectors.toList());

        Optional<RegionTranslation> orderTranslationOptional = orderTranslationList.stream().findAny();
        if (orderTranslationOptional.isPresent()) {
            RegionTranslation orderTranslation = orderTranslationOptional.get();
            result.setTimeStamp(orderTranslation.getTimeStamp());

            result.setDepartureCode(orderTranslation.getDepartureCode());
            result.setDepartureCityName(orderTranslation.getDepartureCityName());
            result.setDepartureProvinceCode(orderTranslation.getDepartureProvinceCode());
            result.setDepartureProvinceName(orderTranslation.getDepartureProvinceName());
            result.setDepartureISOCode(orderTranslation.getDepartureISOCode());

            result.setDesCityCode(orderTranslation.getDesCityCode());
            result.setDesCityName(orderTranslation.getDesCityName());
            result.setDesProvinceCode(orderTranslation.getDesProvinceCode());
            result.setDesProvinceName(orderTranslation.getDesProvinceName());
            result.setDesISOCode(orderTranslation.getDesISOCode());

        }
        double totalProductPrice = orderTranslationList.stream().mapToDouble(OrderTranslation::getActualPayment).sum();
        result.setMoney(totalProductPrice);

        //成年人数
        long totalAdultNum = orderTranslationList.stream().mapToLong(OrderTranslation::getTravelMemberAdult).sum();
        result.setTravelMemberAdult(totalAdultNum);

        //青年人数
        long totalYoungNum = orderTranslationList.stream().mapToLong(OrderTranslation::getTravelMemberYounger).sum();
        result.setTravelMemberYounger(totalYoungNum);

        // baby 人数
        long totalBabyNum = orderTranslationList.stream().mapToLong(OrderTranslation::getTravelMemberBaby).sum();
        result.setTravelMemberBaby(totalBabyNum);

        //总人数
        result.setTotalPeopleNumber(totalAdultNum + totalYoungNum + totalBabyNum);

        //订单数量
        int orderNumber = orderTranslationList.stream().collect(Collectors.groupingBy(RegionTranslation::getOrderId)).keySet().size();
        result.setOrderNumber(orderNumber);

        //酒店数量
        int pubNum = orderTranslationList.stream().filter(v -> v.getPubId() != null).collect(Collectors.groupingBy(RegionTranslation::getPubId)).keySet().size();
        result.setPubNumber(pubNum);

        //产品数量
        int productNum = orderTranslationList.stream().collect(Collectors.groupingBy(RegionTranslation::getProductId)).keySet().size();
        result.setPubNumber(productNum);

        out.collect(result);
    }
}
