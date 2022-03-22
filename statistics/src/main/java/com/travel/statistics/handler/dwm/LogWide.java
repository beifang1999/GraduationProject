package com.travel.statistics.handler.dwm;

import com.alibaba.fastjson.JSONObject;
import com.travel.statistics.HandlerBase;
import com.travel.statistics.config.JobParams;
import com.travel.statistics.config.JobParamsKafka;
import com.travel.statistics.domain.dwd.Click;
import com.travel.statistics.domain.dwd.Launch;
import com.travel.statistics.domain.dwm.ClickWide;
import com.travel.statistics.domain.dwm.LaunchWide;
import com.travel.statistics.domain.schema.KafkaEventSchema;
import com.travel.statistics.udf.function.DimAsyncFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.TimeUnit;


public class LogWide extends HandlerBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogWide.class);
    private static final ObjectMapper OM = new ObjectMapper();

    @Override
    public void handle() {

        JobParams.DiversionJobParams diversionJobParams = new JobParams.DiversionJobParams();
        JobParamsKafka diversionJpk = initJobConfig(diversionJobParams);

        JobParams.LogWideJobParams logWideJobParams = new JobParams.LogWideJobParams();
        JobParamsKafka jpk = initJobConfig(logWideJobParams);

        StreamExecutionEnvironment env = getStreamENV(jpk);

        Properties propsIn = initKafkaProperties(jpk.getBootstrapServerIn(), jpk.getGroupId());

        DataStreamSource<Click> clickDataStreamSource =
                env.addSource(new FlinkKafkaConsumer<>(diversionJpk.getTopicClick(), new KafkaEventSchema<>(Click.class), propsIn));

        DataStreamSource<Launch> launchDataStreamSource =
                env.addSource(new FlinkKafkaConsumer<>(diversionJpk.getTopicLaunch(), new KafkaEventSchema<>(Launch.class), propsIn));

        DataStream<ClickWide> clickWideSingleOutputStreamOperator = clickDataStreamSource.map((MapFunction<Click, ClickWide>) value -> {
            ClickWide clickWide = new ClickWide();
            clickWide.setClick(value);
            return clickWide;
        });

        DataStream<LaunchWide> launchWideSingleOutputStream = launchDataStreamSource.map((MapFunction<Launch, LaunchWide>) value -> {
            LaunchWide launchWide = new LaunchWide();
            launchWide.setLaunch(value);
            return launchWide;
        });

        DataStream<ClickWide> clickWideStream = AsyncDataStream.unorderedWait(clickWideSingleOutputStreamOperator, new DimAsyncFunction<ClickWide>("dim_product", "product_id") {
            private static final long serialVersionUID = 1521575893578494531L;

            @Override
            public String getKey(ClickWide clickWide) {
                return clickWide.getClick().getTargetID() + "";
            }

            @Override
            public void join(ClickWide order, JSONObject dimProduct) {
                order.setProductTitle(dimProduct.getString("PRODUCT_TITLE") + "");
                order.setProductLevel(dimProduct.getString("PRODUCT_LEVEL") + "");
                order.setProductType(dimProduct.getString("PRODUCT_TYPE") + "");
                order.setTravelDay(dimProduct.getString("TRAVEL_DAY") + "");
                order.setProductPrice(dimProduct.getString("PRODUCT_PRICE") + "");
                order.setDepartureCode(dimProduct.getString("DEPARTURE_CODE") + "");
                order.setDesCityCode(dimProduct.getString("DES_CITY_CODE") + "");
            }
        }, 60, TimeUnit.SECONDS);


        DataStream<LaunchWide> launchWideStream = AsyncDataStream.unorderedWait(launchWideSingleOutputStream, new DimAsyncFunction<LaunchWide>("dim_region", "region_city_code") {
            private static final long serialVersionUID = -2697430006686399802L;

            @Override
            public String getKey(LaunchWide value) {
                return value.getLaunch().getUserRegion() + "";
            }

            @Override
            public void join(LaunchWide launchWide, JSONObject dimRegion) {
                launchWide.setCityName(dimRegion.getString("REGION_CITY_NAME") + "");
                launchWide.setProvinceName(dimRegion.getString("REGION_PROVINCE_NAME") + "");
                launchWide.setProvinceCode(dimRegion.getString("REGION_PROVINCE_CODE") + "");
                launchWide.setIsoCode(dimRegion.getString("ISO_CODE") + "");
            }
        }, 60, TimeUnit.SECONDS);

        Properties propsOut = initKafkaProperties(jpk.getBootstrapServerOut(), jpk.getGroupId());
        launchWideStream.filter(v -> v.getProvinceCode() != null)
                .map(v -> OM.writeValueAsString(v))
                .addSink(new FlinkKafkaProducer<>(jpk.getTopicOut(), new SimpleStringSchema(), propsOut))
                .name(jpk.getJobName());


        clickWideStream.filter(v -> v.getProductLevel() != null)
                .map(v -> OM.writeValueAsString(v))
                .addSink(new FlinkKafkaProducer<>(jpk.getTopicClickWide(), new SimpleStringSchema(), propsOut))
                .name(jpk.getJobName());


        try {
            // 提交任务
            LOGGER.info("Start {} succeed", JobParams.JOB_LOG_WIDE);
            env.execute(jpk.getJobName());
        } catch (Exception e) {
            LOGGER.error("Start {} failed, error:{}", JobParams.JOB_LOG_WIDE, e.getMessage());
        }
    }
    
}
