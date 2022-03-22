package com.travel.statistics;

import com.travel.statistics.config.JobParams;
import com.travel.statistics.config.JobParamsKafka;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class HandlerBase implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(HandlerBase.class);

    protected JobParamsKafka initJobConfig(JobParams params) {
        // 加载配置
        LOGGER.info("Config file path:{}", System.getProperty("user.dir"));
        JobParamsKafka paramsKafka = new JobParamsKafka();
        try {
            ParameterTool parameterTool = ParameterTool.fromPropertiesFile(JobParams.PROP_PATH);
            paramsKafka.setParallelism(parameterTool.getInt(params.getParallelism()));
            paramsKafka.setWindowSize(parameterTool.getLong(params.getWindowSize()));
            paramsKafka.setWaterMaker(parameterTool.getLong(params.getWaterMaker()));
            paramsKafka.setCheckpointInterval(parameterTool.getLong(params.getCheckpointInterval()));
            paramsKafka.setJobName(parameterTool.get(params.getJobName()));
            paramsKafka.setBootstrapServerIn(parameterTool.get(params.getBootstrapServerIn()));
            paramsKafka.setBootstrapServerOut(parameterTool.get(params.getBootstrapServerOut()));
            paramsKafka.setGroupId(parameterTool.get(params.getGroupId()));
            paramsKafka.setTopicIn(parameterTool.get(params.getTopicIn()));
            paramsKafka.setTopicOut(parameterTool.get(params.getTopicOut()));
            if (params instanceof JobParams.DiversionJobParams) {
                paramsKafka.setTopicLaunch(parameterTool.get(((JobParams.DiversionJobParams) params).getTopicLaunch()));
                paramsKafka.setTopicPageView(parameterTool.get(((JobParams.DiversionJobParams) params).getTopicPageView()));
                paramsKafka.setTopicClick(parameterTool.get(((JobParams.DiversionJobParams) params).getTopicClick()));
            }
            if (params instanceof JobParams.LogWideJobParams) {
                paramsKafka.setTopicClickWide(parameterTool.get(((JobParams.LogWideJobParams) params).getTopicClick()));
            }

            if (params instanceof JobParams.OrderStatsJobParams) {
                paramsKafka.setTopicTraffic(parameterTool.get(((JobParams.OrderStatsJobParams) params).getTopicTraffic()));
                paramsKafka.setTopicWord(parameterTool.get(((JobParams.OrderStatsJobParams) params).getTopicWord()));
                paramsKafka.setTopicPub(parameterTool.get(((JobParams.OrderStatsJobParams) params).getTopicPub()));
                paramsKafka.setTopicRegion(parameterTool.get(((JobParams.OrderStatsJobParams) params).getTopicRegion()));
            }

        } catch (IOException e) {
            LOGGER.error("Load config file failed, error:{}", e.getLocalizedMessage());
        }
        return paramsKafka;
    }

    protected StreamExecutionEnvironment getStreamENV(JobParamsKafka paramsKafka) {
        // 创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 设置时间语义为 事件时间

//        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        // 设定Watermark 生成周期为 每秒
//        env.getConfig().setAutoWatermarkInterval(1000L);

//        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);

        env.setParallelism(paramsKafka.getParallelism());
        env.enableCheckpointing(paramsKafka.getCheckpointInterval());
        return env;
    }

    protected Properties initKafkaProperties(String bootstrapServer, String groupId) {
        // Kafka 配置文件
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServer);
        props.put("group.id", groupId);
//        props.put("auto.offset.reset","earliest");
//        props.put("retries", 10);
        //key 反序列化
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return props;
    }

    public void handle() {

    }

    @Override
    public void run() {

    }
}
