package com.travel.statistics.udf.process;

import com.travel.statistics.domain.dws.OrderTranslation;
import com.travel.statistics.domain.dws.ProductResult;
import com.travel.statistics.emumes.ProductLevelEnum;
import com.travel.statistics.emumes.ProductTypeEnum;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DwsProductProcessFunction extends ProcessWindowFunction<OrderTranslation, ProductResult, String, TimeWindow> {

    private static final long serialVersionUID = -3327167688152598564L;

    @Override
    public void process(String id, Context context, Iterable<OrderTranslation> elements, Collector<ProductResult> out) {
        ProductResult result = new ProductResult();

        result.setProductId(id);

        List<OrderTranslation> orderList = StreamSupport.stream(elements.spliterator(), false).collect(Collectors.toList());

        Optional<OrderTranslation> orderStream = orderList.stream().findAny();
        if (orderStream.isPresent()) {
            OrderTranslation orderTranslation = orderStream.get();
            result.setTitle(orderTranslation.getProductTitle());
            result.setType(ProductTypeEnum.getRemark(orderTranslation.getProductType()));
            result.setLevel(ProductLevelEnum.getRemark(orderTranslation.getProductLevel()));
            result.setTravelTime(orderTranslation.getTravelDay());
            result.setTimeStamp(orderTranslation.getTimeStamp());
        }
        // 订单总价格
        double totalProductPrice = orderList.stream().mapToDouble(OrderTranslation::getActualPayment).sum();
        result.setMoney(totalProductPrice);

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

    @Test
    public void  myTest(){
        long ts = 1637055628000L;
        System.out.println(ts -ts % 60000 + 60000);
    }
}
