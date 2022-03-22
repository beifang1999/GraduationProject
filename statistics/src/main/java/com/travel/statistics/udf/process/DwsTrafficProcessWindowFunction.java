package com.travel.statistics.udf.process;

import com.travel.statistics.domain.dws.OrderTranslation;

import com.travel.statistics.domain.dws.TrafficResult;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DwsTrafficProcessWindowFunction extends ProcessWindowFunction<OrderTranslation, TrafficResult, String, TimeWindow> {
    private static final long serialVersionUID = -5915209343917363614L;





    @Override
    public void process(String key, Context context, Iterable<OrderTranslation> elements, Collector<TrafficResult> out) throws Exception {

        TrafficResult result = new TrafficResult();

        List<OrderTranslation> translations = StreamSupport.stream(elements.spliterator(), false).collect(Collectors.toList());

        Optional<OrderTranslation> optionalOrderTranslation = translations.stream().findAny();
        if (optionalOrderTranslation.isPresent()){
            OrderTranslation orderTranslation = optionalOrderTranslation.get();

            result.setTimeStamp(orderTranslation.getTimeStamp());
            result.setProductTraffic(orderTranslation.getProductTraffic());
            result.setProductTrafficGrade(orderTranslation.getProductTrafficGrade());
            result.setProductTrafficType(orderTranslation.getProductTrafficType());
        }
        long value = translations.stream().mapToLong(OrderTranslation::getValue).sum();
        result.setValue(value);
        out.collect(result);
    }
}
