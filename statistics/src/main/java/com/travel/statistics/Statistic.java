package com.travel.statistics;


import com.travel.statistics.handler.dwd.DBApp;
import com.travel.statistics.config.JobParams;
import com.travel.statistics.handler.dwd.LogDiversion;
import com.travel.statistics.handler.dwm.LogWide;
import com.travel.statistics.handler.dwm.OrderWide;
import com.travel.statistics.handler.dws.LogStatsJob;
import com.travel.statistics.handler.dws.OrderStatsJob;
import org.apache.flink.api.java.utils.ParameterTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Statistic {

    private static final Logger LOGGER = LoggerFactory.getLogger(Statistic.class);

    public static void main(String [] args) throws Exception{
        ParameterTool param = ParameterTool.fromPropertiesFile(JobParams.PROP_PATH);
        String job = param.get(JobParams.JOB_TYPE);
        switch (job){
            case JobParams.JOB_DIVERSION:{
                LOGGER.info("Start diversion log handle........");
                LogDiversion diversion = new LogDiversion();
                diversion.handle();
                break;
            }
            case JobParams.JOB_DB:{
                LOGGER.info("start db handle");
                DBApp dbApp = new DBApp();
                dbApp.handle();
                break;
            }
            case JobParams.JOB_ORDER_WIDE:{
                LOGGER.info("start order wide job");
                OrderWide orderWide = new OrderWide();
                orderWide.handle();
                break;
            }
            case JobParams.JOB_ORDER_STATS_JOB:{
                LOGGER.info("start order stats job");
                OrderStatsJob orderStatsJob = new OrderStatsJob();
                orderStatsJob.handle();
                break;
            }
            case JobParams.JOB_LOG_WIDE:{
                LOGGER.info("start log_wide job");
                LogWide logWide = new LogWide();
                logWide.handle();
                break;
            }
            case JobParams.JOB_LOG_STATS: {
                LOGGER.info("start log_stats job");
                LogStatsJob logStatsJob = new LogStatsJob();
                logStatsJob.handle();
                break;
            }

            default:{
                LOGGER.warn("Can not match any job, job name:{}", job);
            }
        }
    }

}
