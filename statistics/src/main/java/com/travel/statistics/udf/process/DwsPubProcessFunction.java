package com.travel.statistics.udf.process;

import com.travel.statistics.domain.dws.OrderTranslation;
import com.travel.statistics.domain.dws.PubResult;
import com.travel.statistics.emumes.PubStarEnum;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DwsPubProcessFunction extends ProcessWindowFunction<OrderTranslation, PubResult, String, TimeWindow> {
    private static final long serialVersionUID = 9031651874141169784L;

    @Override
    public void process(String id, Context context, Iterable<OrderTranslation> elements, Collector<PubResult> out) {
        PubResult result = new PubResult();
        result.setPubId(id);

        List<OrderTranslation> orderList = StreamSupport.stream(elements.spliterator(), false).collect(Collectors.toList());

        Optional<OrderTranslation> orderStream = orderList.stream().findAny();
        if (orderStream.isPresent()){
            OrderTranslation orderTranslation = orderStream.get();
            result.setTimeStamp(orderTranslation.getTimeStamp());
            result.setName(orderTranslation.getPubName());
            result.setStar(PubStarEnum.getRemark(orderTranslation.getPubStar()));
            result.setGrade(orderTranslation.getPubGrade());
            result.setAddress(orderTranslation.getPubAddress());
        }

        // 订单总价格
        double totalPubPrice = orderList.stream().mapToDouble(OrderTranslation::getActualPayment).sum();
        result.setMoney(totalPubPrice);

        //成年人数
        long totalAdultNum = orderList.stream().mapToLong(OrderTranslation::getTravelMemberAdult).sum();
        result.setTotalAdultNum(totalAdultNum);

        //青年人数
        long totalYoungNum = orderList.stream().mapToLong(OrderTranslation::getTravelMemberYounger).sum();
        result.setTotalYoungNum(totalYoungNum);

        // baby 人数
        long totalBabyNum = orderList.stream().mapToLong(OrderTranslation::getTravelMemberBaby).sum();
        result.setTotalBabyNum(totalBabyNum);

        //总人数
        result.setTotalPeopleNum(totalAdultNum + totalYoungNum + totalBabyNum);

        //订单数量
        int orderNumber = orderList.stream().collect(Collectors.groupingBy(OrderTranslation::getOrderId)).keySet().size();
        result.setOrderNumber(orderNumber);

        out.collect(result);
    }
}
