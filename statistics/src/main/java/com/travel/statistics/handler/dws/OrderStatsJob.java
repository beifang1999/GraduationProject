package com.travel.statistics.handler.dws;

import com.alibaba.fastjson.JSONObject;
import com.travel.statistics.HandlerBase;
import com.travel.statistics.config.JobParams;
import com.travel.statistics.config.JobParamsKafka;
import com.travel.statistics.domain.dwm.Order;
import com.travel.statistics.domain.dws.*;
import com.travel.statistics.domain.schema.KafkaEventSchema;
import com.travel.statistics.udf.flatmap.DwsWordFlatMapFunction;
import com.travel.statistics.udf.function.DimAsyncFunction;
import com.travel.statistics.udf.map.OrderMapFunction;
import com.travel.statistics.udf.map.dws.OrderRegionMapFunction;
import com.travel.statistics.udf.process.DwsProductProcessFunction;
import com.travel.statistics.udf.process.DwsPubProcessFunction;
import com.travel.statistics.udf.process.DwsRegionProcessFunction;
import com.travel.statistics.udf.process.DwsTrafficProcessWindowFunction;
import com.travel.statistics.udf.reduce.DwsPubReduceFunction;
import com.travel.statistics.udf.reduce.DwsWordReduceFunction;
import org.apache.flink.api.common.eventtime.BoundedOutOfOrdernessWatermarks;
import org.apache.flink.api.common.eventtime.WatermarkGenerator;
import org.apache.flink.api.common.eventtime.WatermarkGeneratorSupplier;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.file.sink.FileSink;
import org.apache.flink.core.fs.Path;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.Duration;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class OrderStatsJob extends HandlerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderStatsJob.class);

    private static final ObjectMapper OM = new ObjectMapper();

    @Override
    public void handle() {

        JobParams.OrderStatsJobParams orderStatsJobParams = new JobParams.OrderStatsJobParams();
        JobParamsKafka jpk = initJobConfig(orderStatsJobParams);
        StreamExecutionEnvironment env = getStreamENV(jpk);
        Properties propsIn = initKafkaProperties(jpk.getBootstrapServerIn(), jpk.getGroupId());
        DataStreamSource<Order> orderDataStreamSource = env.addSource(new FlinkKafkaConsumer<>(jpk.getTopicIn(), new KafkaEventSchema<>(Order.class), propsIn));
        SingleOutputStreamOperator<Order> orderStream = orderDataStreamSource.assignTimestampsAndWatermarks(new WatermarkStrategy<Order>() {
            private static final long serialVersionUID = 2316703260017784690L;

            @Override
            public WatermarkGenerator<Order> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
                return new BoundedOutOfOrdernessWatermarks<>(Duration.ofMillis(jpk.getWaterMaker()));
            }
        }.withTimestampAssigner((element, timestamp) -> Long.parseLong(element.getOrderCt()))
                .withIdleness(Duration.ofMillis(jpk.getWindowSize())));

        SingleOutputStreamOperator<OrderTranslation> orderTranslationStream = orderStream.map(new OrderMapFunction());


        Properties propsOut = initKafkaProperties(jpk.getBootstrapServerOut(), jpk.getGroupId());


        //出行方式
        orderTranslationStream.keyBy(v -> v.getProductTraffic() + v.getProductTrafficGrade() + v.getProductTrafficType())
                .window(TumblingEventTimeWindows.of(Time.milliseconds(jpk.getWindowSize())))
                .process(new DwsTrafficProcessWindowFunction())
                .returns(TypeInformation.of(TrafficResult.class))
                .map(v -> OM.writeValueAsString(v))
                .addSink(new FlinkKafkaProducer<>(jpk.getTopicTraffic(), new SimpleStringSchema(), propsOut))
                .name(jpk.getJobName());

        //词云
        orderTranslationStream
                .flatMap(new DwsWordFlatMapFunction())
                .keyBy(WordResult::getTitleWord)
                .reduce(new DwsWordReduceFunction())
                .returns(TypeInformation.of(WordResult.class))
                .filter(v -> v.getPubWord() != null)
                .keyBy(WordResult::getPubWord)
                .reduce(new DwsPubReduceFunction())
                .returns(TypeInformation.of(WordResult.class))
                .map(v -> OM.writeValueAsString(v))
                .addSink(new FlinkKafkaProducer<>(jpk.getTopicWord(), new SimpleStringSchema(), propsOut))
                .name(jpk.getJobName());

        //产品维度
        orderTranslationStream
                .keyBy(OrderTranslation::getProductId)
                .window(TumblingEventTimeWindows.of(Time.milliseconds(jpk.getWindowSize())))
                .process(new DwsProductProcessFunction())
                .returns(TypeInformation.of(ProductResult.class))
                .map(v -> OM.writeValueAsString(v))
                .addSink(new FlinkKafkaProducer<>(jpk.getTopicOut(), new SimpleStringSchema(), propsOut))
                .name(jpk.getJobName());



//        final FileSink<String> sink = FileSink
//                .forRowFormat(new Path("E:\\GraduationProject\\data\\"), new SimpleStringEncoder<String>("UTF-8"))
//                .withRollingPolicy(
//                        DefaultRollingPolicy.builder()
//                                .withRolloverInterval(TimeUnit.MINUTES.toMillis(15))
//                                .withInactivityInterval(TimeUnit.MINUTES.toMillis(5))
//                                .withMaxPartSize(1024 * 1024 * 1024)
//                                .build())
//                .build();
//        orderTranslationStream
//                .keyBy(OrderTranslation::getProductId)
//                .window(TumblingEventTimeWindows.of(Time.milliseconds(jpk.getWindowSize())))
//                .process(new DwsProductProcessFunction())
//                .returns(TypeInformation.of(ProductResult.class))
//                .map(v -> OM.writeValueAsString(v))
//                .sinkTo(sink);

//        //酒店维度
        orderTranslationStream
                .filter(v -> v.getPubId() != null && !Objects.equals(v.getPubId(), ""))
                .keyBy(OrderTranslation::getPubId)
                .window(TumblingEventTimeWindows.of(Time.milliseconds(jpk.getWindowSize())))
                .process(new DwsPubProcessFunction())
                .returns(TypeInformation.of(PubResult.class))
                .map(v -> OM.writeValueAsString(v))
                .addSink(new FlinkKafkaProducer<>(jpk.getTopicPub(), new SimpleStringSchema(), propsOut))
                .name(jpk.getJobName());

        //地区维度
        SingleOutputStreamOperator<RegionTranslation> regionTranslationSingleOutputStreamOperator = orderTranslationStream.map(new OrderRegionMapFunction()).returns(TypeInformation.of(RegionTranslation.class));

        //关联离开城市 的 name
        SingleOutputStreamOperator<RegionTranslation> regionDepartureStream = AsyncDataStream.unorderedWait(regionTranslationSingleOutputStreamOperator, new DimAsyncFunction<RegionTranslation>("dim_region", "region_city_code") {
            @Override
            public String getKey(RegionTranslation regionTranslation) {
                return regionTranslation.getDepartureCode() + "";
            }

            @Override
            public void join(RegionTranslation regionTranslation, JSONObject dimRegion) {
                regionTranslation.setDepartureCityName(dimRegion.getString("REGION_CITY_NAME") + "");
                regionTranslation.setDepartureProvinceCode(dimRegion.getString("REGION_PROVINCE_NAME") + "");

                regionTranslation.setDepartureProvinceName(dimRegion.getString("REGION_PROVINCE_NAME")+"");
                regionTranslation.setDepartureISOCode(dimRegion.getString("ISO_CODE")+"");
            }
        }, 60, TimeUnit.SECONDS);

        //关联 目的地  城市 name
        SingleOutputStreamOperator<RegionTranslation> regionStream = AsyncDataStream.unorderedWait(regionDepartureStream, new DimAsyncFunction<RegionTranslation>("dim_region", "region_city_code") {
            @Override
            public String getKey(RegionTranslation regionTranslation) {
                return regionTranslation.getDesCityCode() + "";
            }

            @Override
            public void join(RegionTranslation regionTranslation, JSONObject dimRegion) {
                regionTranslation.setDesCityName(dimRegion.getString("REGION_CITY_NAME") + "");
                regionTranslation.setDesProvinceCode(dimRegion.getString("REGION_PROVINCE_NAME") + "");
                regionTranslation.setDesProvinceName(dimRegion.getString("REGION_PROVINCE_NAME")+"");
                regionTranslation.setDesISOCode(dimRegion.getString("ISO_CODE")+"");
            }
        }, 60, TimeUnit.SECONDS);

        regionStream.
                keyBy(v -> v.getDesCityCode() +v.getDepartureCode())
                .window(TumblingEventTimeWindows.of(Time.milliseconds(jpk.getWindowSize())))
                .process(new DwsRegionProcessFunction())
                .returns(TypeInformation.of(RegionResult.class))
                .map(v -> OM.writeValueAsString(v))
                .addSink(new FlinkKafkaProducer<>(jpk.getTopicRegion(), new SimpleStringSchema(), propsOut))
                .name(jpk.getJobName());

        try {
            // 提交任务
            LOGGER.info("Start {} succeed", JobParams.JOB_ORDER_STATS_JOB);
            env.execute(jpk.getJobName());
        } catch (Exception e) {
            LOGGER.error("Start {} failed, error:{}", JobParams.JOB_ORDER_STATS_JOB, e.getMessage());
        }

    }
}
