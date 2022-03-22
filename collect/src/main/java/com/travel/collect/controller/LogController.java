package com.travel.collect.controller;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LogController {

    @Autowired
    ThreadPoolTaskExecutor poolExecutor;

    //KafkaTemplate是Spring提供对kafka操作的类
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    //    同步
    @RequestMapping("/log0")
    public String logger(@RequestParam("param") String jsonLog) {



        //System.out.println(jsonLog);
        //  借助记录日志的第三方框架 log4j [logback]
        log.info(jsonLog);
        //3.将生成的日主发送到kafka对应的主题中
        kafkaTemplate.send("ods_base_log", jsonLog);

        return "success";
    }


    //多线程异步
    @RequestMapping("/log")
    public String sendSyn(@RequestParam("param") String jsonLog) {

        poolExecutor.setCorePoolSize(2);

        poolExecutor.submit(() -> {

            ListenableFuture<SendResult<String, String>> cancan = kafkaTemplate.send("ods_base_log", jsonLog);

            cancan.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                @Override
                public void onFailure(@NonNull Throwable throwable) {
                    log.info("结果失败");
                }

                @Override
                public void onSuccess(SendResult<String, String> result) {

                    ProducerRecord<String, String> producerRecord = result.getProducerRecord();
                    System.out.println(producerRecord.toString());

                }
            });
        });
        return "succeed";
    }
}

