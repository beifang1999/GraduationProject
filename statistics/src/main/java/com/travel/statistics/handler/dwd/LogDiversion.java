package com.travel.statistics.handler.dwd;

import com.travel.statistics.HandlerBase;
import com.travel.statistics.config.JobParams;
import com.travel.statistics.config.JobParamsKafka;
import com.travel.statistics.domain.schema.KafkaEventSchema;
import com.travel.statistics.udf.map.dwd.ClickMapFunction;
import com.travel.statistics.udf.map.dwd.LaunchMapFunction;
import com.travel.statistics.udf.map.dwd.PageViewMapFunction;
import com.travel.statistics.udf.map.dwd.ProductListViewMapFunction;
import com.travel.statistics.udf.process.DwdLogDiversionProcessFunction;
import com.travel.statistics.domain.LogMsgBase;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.util.OutputTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class LogDiversion extends HandlerBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogDiversion.class);

    private static final ObjectMapper OM = new ObjectMapper();

    @Override
    public void handle() {
        JobParams.DiversionJobParams diversionJobParams = new JobParams.DiversionJobParams();
        JobParamsKafka jpk = initJobConfig(diversionJobParams);
        StreamExecutionEnvironment env = getStreamENV(jpk);
        Properties propsIn = initKafkaProperties(jpk.getBootstrapServerIn(), jpk.getGroupId());
        DataStreamSource<LogMsgBase> logMsgBaseDataStreamSource = env.addSource(new FlinkKafkaConsumer<>(jpk.getTopicIn(), new KafkaEventSchema<>(LogMsgBase.class), propsIn));
        OutputTag<LogMsgBase> launch = new OutputTag<>("launch", TypeInformation.of(LogMsgBase.class));
        OutputTag<LogMsgBase> pageView = new OutputTag<>("page_view", TypeInformation.of(LogMsgBase.class));
        OutputTag<LogMsgBase> click = new OutputTag<>("click", TypeInformation.of(LogMsgBase.class));
        SingleOutputStreamOperator<LogMsgBase> productListViewStream =
                logMsgBaseDataStreamSource.process(new DwdLogDiversionProcessFunction(launch, pageView, click) {
        });

        Properties propsOut = initKafkaProperties(jpk.getBootstrapServerOut(), jpk.getGroupId());

//        productListViewStream.map(new ProductListViewMapFunction()).map(v -> OM.writeValueAsString(v))
//                .addSink(new FlinkKafkaProducer<>(jpk.getTopicOut(), new SimpleStringSchema(), propsOut))
//                .name(jpk.getJobName());

        productListViewStream.getSideOutput(launch)
                .map(new LaunchMapFunction())

                .map(v -> OM.writeValueAsString(v))
                .addSink(new FlinkKafkaProducer<String>(jpk.getTopicLaunch(), new SimpleStringSchema(), propsOut))
                .name(jpk.getJobName());

        productListViewStream.getSideOutput(click)
                .map(new ClickMapFunction())
                .map(v -> OM.writeValueAsString(v))
                .addSink(new FlinkKafkaProducer<String>(jpk.getTopicClick(), new SimpleStringSchema(), propsOut))
                .name(jpk.getJobName());

//        productListViewStream.getSideOutput(pageView)
//                .map(new PageViewMapFunction())
//                .map(v -> OM.writeValueAsString(v))
//                .addSink(new FlinkKafkaProducer<String>(jpk.getTopicPageView(), new SimpleStringSchema(), propsOut))
//                .name(jpk.getJobName());
        
        try {
            // 提交任务
            LOGGER.info("Start {} succeed", JobParams.JOB_DIVERSION);
            env.execute(jpk.getJobName());
        } catch (Exception e) {
            LOGGER.error("Start {} failed, error:{}", JobParams.JOB_DIVERSION, e.getMessage());
        }
    }
}
