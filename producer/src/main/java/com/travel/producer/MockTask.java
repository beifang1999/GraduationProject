package com.travel.producer;


import com.travel.producer.data.util.data.travel.logs.TravelCurLogHelper;
import com.travel.producer.data.util.data.travel.orders.TravelOrderHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;


@Component
public class MockTask {

    @Autowired
    ThreadPoolTaskExecutor poolExecutor;

    @Autowired
    TravelCurLogHelper travelCurLogHelper;

    @Autowired
    TravelOrderHelper travelOrderHelper;

    public void mainTask(String flag, int count, int sleepTime, int core) {


        poolExecutor.setCorePoolSize(core);

        if (flag.equals("log")) {
            travelCurLogHelper.setSleepTime(sleepTime);
            for (int i = 0; i < count; i++) {
                poolExecutor.submit(travelCurLogHelper);
            }
        } else if (flag.equals("db")) {
            travelOrderHelper.setSleepTime(sleepTime);
            for (int i = 0; i < count; i++) {
                poolExecutor.submit(travelOrderHelper);
            }
        }else {
            try {
                throw new Exception("无效参数，flag 必须为 db 或log");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        while (true) {
            try {
                Thread.sleep(1000);
                if (poolExecutor.getActiveCount() == 0) {
                    poolExecutor.destroy();
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

