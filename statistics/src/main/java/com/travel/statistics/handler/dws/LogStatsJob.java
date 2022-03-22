package com.travel.statistics.handler.dws;

import com.travel.statistics.HandlerBase;
import com.travel.statistics.config.JobParams;
import com.travel.statistics.config.JobParamsKafka;
import com.travel.statistics.domain.dwd.Click;
import com.travel.statistics.domain.dwd.Launch;
import com.travel.statistics.domain.dwm.ClickWide;
import com.travel.statistics.domain.dwm.LaunchWide;
import com.travel.statistics.domain.dwm.Order;
import com.travel.statistics.domain.schema.KafkaEventSchema;
import org.apache.flink.api.common.eventtime.BoundedOutOfOrdernessWatermarks;
import org.apache.flink.api.common.eventtime.WatermarkGenerator;
import org.apache.flink.api.common.eventtime.WatermarkGeneratorSupplier;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Properties;


public class LogStatsJob extends HandlerBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogStatsJob.class);

    private static final ObjectMapper OM = new ObjectMapper();

    @Override
    public void handle() {
        JobParams.LogWideJobParams logWideJobParams = new JobParams.LogWideJobParams();
        JobParamsKafka logWideJpk = initJobConfig(logWideJobParams);


        JobParams.LogStatsJobParams logStatsJobParams = new JobParams.LogStatsJobParams();
        JobParamsKafka jpk = initJobConfig(logStatsJobParams);
        StreamExecutionEnvironment env = getStreamENV(jpk);

        Properties propsIn = initKafkaProperties(jpk.getBootstrapServerIn(), jpk.getGroupId());

        DataStreamSource<ClickWide> clickWideDataStreamSource =
                env.addSource(new FlinkKafkaConsumer<>(logWideJpk.getTopicClickWide(), new KafkaEventSchema<>(ClickWide.class), propsIn));

        DataStream<ClickWide> clickWideStream = clickWideDataStreamSource.assignTimestampsAndWatermarks(new WatermarkStrategy<ClickWide>() {
            private static final long serialVersionUID = 2316703260017784690L;

            @Override
            public WatermarkGenerator<ClickWide> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
                return new BoundedOutOfOrdernessWatermarks<>(Duration.ofMillis(jpk.getWaterMaker()));
            }
        }.withTimestampAssigner((element, timestamp) -> element.getClick().getTimeStamp())
                .withIdleness(Duration.ofMillis(jpk.getWindowSize())));


        DataStreamSource<LaunchWide> launchWideDataStreamSource =
                env.addSource(new FlinkKafkaConsumer<>(logWideJpk.getTopicOut(), new KafkaEventSchema<>(LaunchWide.class), propsIn));

        SingleOutputStreamOperator<LaunchWide> launchWideStream = launchWideDataStreamSource.assignTimestampsAndWatermarks(new WatermarkStrategy<LaunchWide>() {
            private static final long serialVersionUID = 2316703260017784690L;

            @Override
            public WatermarkGenerator<LaunchWide> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
                return new BoundedOutOfOrdernessWatermarks<>(Duration.ofMillis(jpk.getWaterMaker()));
            }
        }.withTimestampAssigner((element, timestamp) -> element.getLaunch().getTimeStamp())
                .withIdleness(Duration.ofMillis(jpk.getWindowSize())));


        Properties propsOut = initKafkaProperties(jpk.getBootstrapServerOut(), jpk.getGroupId());

        launchWideStream.keyBy(v -> v.getLaunch().getOs() + v.getLaunch().getUserRegion() + v.getLaunch().getManufacturer()
                + v.getLaunch().getCarrier() + v.getLaunch().getUserDeviceType() + v.getLaunch().getNetworkType() +
                v.getLaunch().getUserID())
                .window(TumblingEventTimeWindows.of(Time.milliseconds(jpk.getWindowSize())))
                .reduce(new ReduceFunction<LaunchWide>() {
                    @Override
                    public LaunchWide reduce(LaunchWide value1, LaunchWide value2) {
                        value1.setValue(value1.getValue() + value2.getValue());
                        return value1;
                    }
                }).map(new MapFunction<LaunchWide, LaunchWide>() {
            @Override
            public LaunchWide map(LaunchWide value) throws Exception {
                Launch launch = value.getLaunch();
                launch.setTimeStamp(launch.getTimeStamp() - launch.getTimeStamp() % 60000);
                value.setLaunch(launch);
                
                return value;
            }
        }).map(v -> OM.writeValueAsString(v))
                .addSink(new FlinkKafkaProducer<>("dws_launch_stat", new SimpleStringSchema(), propsOut))
                .name(jpk.getJobName());

        clickWideStream.keyBy(v -> v.getClick().getEventTargetType() + v.getClick().getTargetID())
                .window(TumblingEventTimeWindows.of(Time.milliseconds(jpk.getWindowSize())))
                .reduce(new ReduceFunction<ClickWide>() {
                    @Override
                    public ClickWide reduce(ClickWide value1, ClickWide value2) throws Exception {
                        value1.setValue(value1.getValue() + value2.getValue());
                        return value1;
                    }
                }).map(new MapFunction<ClickWide, ClickWide>() {

            @Override
            public ClickWide map(ClickWide value) throws Exception {
                Click click = value.getClick();
                click.setTimeStamp(click.getTimeStamp() - click.getTimeStamp() % 60000);
                value.setClick(click);
                return value;
            }
        }).map(v -> OM.writeValueAsString(v))
                .addSink(new FlinkKafkaProducer<>("dws_click_stat", new SimpleStringSchema(), propsOut))
                .name(jpk.getJobName());


        try {
            // 提交任务
            LOGGER.info("Start {} succeed", JobParams.JOB_LOG_STATS);
            env.execute(jpk.getJobName());
        } catch (Exception e) {
            LOGGER.error("Start {} failed, error:{}", JobParams.JOB_LOG_STATS, e.getMessage());
        }


    }
}
