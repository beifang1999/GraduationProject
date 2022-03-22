package com.travel.statistics.handler.dwm;

import com.alibaba.fastjson.JSONObject;
import com.travel.statistics.HandlerBase;
import com.travel.statistics.config.JobParams;
import com.travel.statistics.config.JobParamsKafka;
import com.travel.statistics.domain.dwm.Order;
import com.travel.statistics.domain.schema.KafkaEventSchema;
import com.travel.statistics.udf.function.DimAsyncFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.TimeUnit;


/**
    合并订单宽表
 */
public class OrderWide extends HandlerBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderWide.class);

    private static final ObjectMapper OM = new ObjectMapper();

    @Override
    public void handle() {
        JobParams.OrderWideParams orderWideParams = new JobParams.OrderWideParams();
        JobParamsKafka jpk = initJobConfig(orderWideParams);
        StreamExecutionEnvironment env = getStreamENV(jpk);
        Properties propsIn = initKafkaProperties(jpk.getBootstrapServerIn(), jpk.getGroupId());
        DataStreamSource<Order> orderDataStreamSource = env.addSource(new FlinkKafkaConsumer<>(jpk.getTopicIn(), new KafkaEventSchema<>(Order.class), propsIn));

        //order 和酒店关联
        SingleOutputStreamOperator<Order> orderAndPubStream = AsyncDataStream.unorderedWait(orderDataStreamSource, new DimAsyncFunction<Order>("dim_pub", "pub_id") {
            private static final long serialVersionUID = -573829508809090904L;

            @Override
            public String getKey(Order order) {
                return order.getProductPub() + "";
            }

            @Override
            public void join(Order order, JSONObject dimPub) {

                order.setPubName(dimPub.getString("PUB_NAME") + "");
                order.setPubStar(dimPub.getString("PUB_STAR") + "");
                order.setPubGradeDesc(dimPub.getString("PUB_GRADE_DESC") + "");
                order.setPubGrade(dimPub.getString("PUB_GRADE") + "");
                order.setPubAreaCode(dimPub.getString("PUB_AREA_CODE") + "");
                order.setPubAddress(dimPub.getString("PUB_ADDRESS") + "");
                order.setIsNational(dimPub.getString("IS_NATIONAL") + "");
            }
        }, 60, TimeUnit.SECONDS);

        SingleOutputStreamOperator<Order> orderAndPubAndProductStream = AsyncDataStream.unorderedWait(orderAndPubStream, new DimAsyncFunction<Order>("dim_product", "product_id") {
            @Override
            public String getKey(Order order) {
                return order.getProductId() + "";
            }

            @Override
            public void join(Order order, JSONObject dimProduct) throws Exception {
                order.setProductTitle(dimProduct.getString("PRODUCT_TITLE") + "");
                order.setProductLevel(dimProduct.getString("PRODUCT_LEVEL") + "");
                order.setProductType(dimProduct.getString("PRODUCT_TYPE") + "");
                order.setTravelDay(dimProduct.getString("TRAVEL_DAY") + "");
                order.setProductPrice(dimProduct.getString("PRODUCT_PRICE")+"");
                order.setDepartureCode(dimProduct.getString("DEPARTURE_CODE") + "");
                order.setDesCityCode(dimProduct.getString("DES_CITY_CODE") + "");
            }
        }, 60, TimeUnit.SECONDS);

        Properties propsOut = initKafkaProperties(jpk.getBootstrapServerOut(), jpk.getGroupId());
        orderAndPubAndProductStream.map( v ->OM.writeValueAsString(v))
                .addSink(new FlinkKafkaProducer<>(jpk.getTopicOut(), new SimpleStringSchema(), propsOut))
                .name(jpk.getJobName());

        try {
            // 提交任务
            LOGGER.info("Start {} succeed", JobParams.JOB_ORDER_WIDE);
            env.execute(jpk.getJobName());
        } catch (Exception e) {
            LOGGER.error("Start {} failed, error:{}", JobParams.JOB_ORDER_WIDE, e.getMessage());
        }
    }
}
