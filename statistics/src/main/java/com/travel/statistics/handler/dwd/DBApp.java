package com.travel.statistics.handler.dwd;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.travel.statistics.HandlerBase;
import com.travel.statistics.config.JobParams;
import com.travel.statistics.config.JobParamsKafka;
import com.travel.statistics.domain.TableProcess;
import com.travel.statistics.udf.process.TableProcessFunction;
import com.travel.statistics.udf.sink.DimSink;
import com.travel.statistics.utils.MyKafkaUtil;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.flink.util.OutputTag;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class DBApp extends HandlerBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBApp.class);

    @Override
    public void handle() {
        JobParams.DBAppJobParams dbJobParams = new JobParams.DBAppJobParams();
        JobParamsKafka jpk = initJobConfig(dbJobParams);
        StreamExecutionEnvironment env = getStreamENV(jpk);
        Properties propsIn = initKafkaProperties(jpk.getBootstrapServerIn(), jpk.getGroupId());

        DataStreamSource<String> dataStreamSource = env.addSource(new FlinkKafkaConsumer<>(jpk.getTopicIn(), new SimpleStringSchema(), propsIn));

        SingleOutputStreamOperator<JSONObject> jsonObjectStream = dataStreamSource.map(JSON::parseObject);
        SingleOutputStreamOperator<JSONObject> filteredDS = jsonObjectStream.filter(v -> v.getString("table") != null
                && v.getJSONObject("data") != null
                && v.getString("data").length() > 3);

        //动态分流  事实表放到主流，写回到kafka的DWD层；如果维度表，通过侧输出流，写入到Hbase
        //定义输出到Hbase的侧输出流标签
        OutputTag<JSONObject> hbaseTag = new OutputTag<JSONObject>(TableProcess.SINK_TYPE_HBASE){
            private static final long serialVersionUID = 3905243098319393443L;
        };

        SingleOutputStreamOperator<JSONObject> kafkaDS = filteredDS.process(new TableProcessFunction(hbaseTag));
        //获取侧输出流    写到Hbase的数据
        DataStream<JSONObject> hbaseDS = kafkaDS.getSideOutput(hbaseTag);

        //将维度数据保存到Phoenix对应的维度表中
        hbaseDS.addSink(new DimSink()).name("dimension");
        FlinkKafkaProducer<JSONObject> kafkaSink = MyKafkaUtil.getKafkaSinkBySchema(
                new KafkaSerializationSchema<JSONObject>() {
                    private static final long serialVersionUID = -976292345937129650L;
                    @Override
                    public void open(SerializationSchema.InitializationContext context) {
                        System.out.println("kafka序列化");
                    }
                    @Override
                    public ProducerRecord<byte[], byte[]> serialize(JSONObject jsonObj, @Nullable Long timestamp) {
                        String sinkTopic = jsonObj.getString("sink_table");
                        JSONObject dataJsonObj = jsonObj.getJSONObject("data");
                        return new ProducerRecord<>(sinkTopic,dataJsonObj.toString().getBytes());
                    }
                }
        );
        kafkaDS.addSink(kafkaSink).name("fact");
        try {
            // 提交任务
            LOGGER.info("Start {} succeed", JobParams.JOB_DB);
            env.execute(jpk.getJobName());
        } catch (Exception e) {
            LOGGER.error("Start {} failed, error:{}", JobParams.JOB_DB, e.getMessage());
        }
    }
}
