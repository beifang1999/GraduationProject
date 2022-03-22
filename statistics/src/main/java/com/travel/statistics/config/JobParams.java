package com.travel.statistics.config;

import java.io.Serializable;

public class JobParams implements Serializable {

    private static final long serialVersionUID = -3327028833254802044L;

    public static final String PROP_PATH = "config/application.properties";
    /**
     * 需要启动的Job类型
     */
    public static final String JOB_TYPE = "job.type";

    /**
     * 初始 JOB
     */
    public static final String JOB_DIVERSION = "log_diversion_job";

    public static final String JOB_LOG_WIDE="log_wide_job";

    public static final String JOB_LOG_STATS="log_stats_job";


    public static final String JOB_DB = "db";

    public static final String JOB_ORDER_WIDE = "order_wide_job";

    public static final String JOB_ORDER_STATS_JOB = "order_stats_job";

    private String parallelism;

    private String windowSize;

    private String waterMaker;

    private String checkpointInterval;

    private String jobName;

    private String bootstrapServerIn;

    private String bootstrapServerOut;

    private String groupId;

    private String topicIn;

    private String topicOut;

    public static class DiversionJobParams extends JobParams {

        private String topicLaunch = "kafka.topic.out_log_launch";
        private String topicPageView = "kafka.topic.out_log_page_view";
        private String topicClick = "kafka.topic.out_log_click";

        public DiversionJobParams() {
            super.parallelism = "parallelism.diversion";
            super.windowSize = "window.size_diversion";
            super.waterMaker = "water.maker_diversion";
            super.checkpointInterval = "checkpoint.interval_diversion";
            super.jobName = "job.name_diversion";
            super.bootstrapServerIn = "bootstrap.servers_input_diversion";
            super.bootstrapServerOut = "bootstrap.servers_output_diversion";
            super.topicIn = "kafka.topic.input_diversion";
            super.topicOut = "kafka.topic.output_diversion";
            super.groupId = "group.id_diversion";
        }

        public String getTopicLaunch() {
            return topicLaunch;
        }

        public void setTopicLaunch(String topicLaunch) {
            this.topicLaunch = topicLaunch;
        }

        public String getTopicPageView() {
            return topicPageView;
        }

        public void setTopicPageView(String topicPageView) {
            this.topicPageView = topicPageView;
        }

        public String getTopicClick() {
            return topicClick;
        }

        public void setTopicClick(String topicClick) {
            this.topicClick = topicClick;
        }


    }

    public static class LogStatsJobParams extends JobParams {

        public LogStatsJobParams() {
            super.parallelism = "parallelism.log_stats";
            super.windowSize = "window.size_log_stats";
            super.waterMaker = "water.maker_log_stats";
            super.checkpointInterval = "checkpoint.interval_log_stats";
            super.jobName = "job.name_log_stats";
            super.bootstrapServerIn = "bootstrap.servers_input_log_stats";
            super.bootstrapServerOut = "bootstrap.servers_output_log_stats";
            super.topicIn = "kafka.topic.input_log_stats";
            super.topicOut = "kafka.topic.output_log_stats";
            super.groupId = "group.id_log_stats";
        }


    }



    public static class LogWideJobParams extends JobParams {

        private String topicClick = "kafka.topic.out_log_wide_click";


        public LogWideJobParams() {
            super.parallelism = "parallelism.log_wide";
            super.windowSize = "window.size_log_wide";
            super.waterMaker = "water.maker_log_wide";
            super.checkpointInterval = "checkpoint.interval_log_wide";
            super.jobName = "job.name_log_wide";
            super.bootstrapServerIn = "bootstrap.servers_input_log_wide";
            super.bootstrapServerOut = "bootstrap.servers_output_log_wide";
            super.topicIn = "kafka.topic.input_log_wide";
            super.topicOut = "kafka.topic.output_log_wide";
            super.groupId = "group.id_log_wide";
        }

        public String getTopicClick() {
            return topicClick;
        }

        public void setTopicClick(String topicClick) {
            this.topicClick = topicClick;
        }
    }




    public static class DBAppJobParams extends JobParams {
        public DBAppJobParams() {
            super.parallelism = "parallelism.db";
            super.windowSize = "window.size_db";
            super.waterMaker = "water.maker_db";
            super.checkpointInterval = "checkpoint.interval_db";
            super.jobName = "job.name_db";
            super.bootstrapServerIn = "bootstrap.servers_input_db";
            super.bootstrapServerOut = "bootstrap.servers_output_db";
            super.topicIn = "kafka.topic.input_db";
            super.topicOut = "kafka.topic.output_db";
            super.groupId = "group.id_db";
        }
    }

    public static class OrderWideParams extends JobParams {
        public OrderWideParams() {
            super.parallelism = "parallelism.orderWide";
            super.windowSize = "window.size_orderWide";
            super.waterMaker = "water.maker_orderWide";
            super.checkpointInterval = "checkpoint.interval_orderWide";
            super.jobName = "job.name_orderWide";
            super.bootstrapServerIn = "bootstrap.servers_input_orderWide";
            super.bootstrapServerOut = "bootstrap.servers_output_orderWide";
            super.topicIn = "kafka.topic.input_orderWide";
            super.topicOut = "kafka.topic.output_orderWide";
            super.groupId = "group.id_orderWide";
        }
    }

    public static class OrderStatsJobParams extends JobParams {

        private String topicTraffic = "kafka.topic.output_traffic_stats";
        private String topicWord = "kafka.topic.output_word_stats";
        private String topicPub = "kafka.topic.output_pub_stats";
        private String topicRegion = "kafka.topic.output_region_stats";

        public OrderStatsJobParams() {
            super.parallelism = "parallelism.order_stats";
            super.windowSize = "window.size_order_stats";
            super.waterMaker = "water.maker_order_stats";
            super.checkpointInterval = "checkpoint.interval_order_stats";
            super.jobName = "job.name_order_stats";
            super.bootstrapServerIn = "bootstrap.servers_input_order_stats";
            super.bootstrapServerOut = "bootstrap.servers_output_order_stats";
            super.topicIn = "kafka.topic.input_order_stats";
            super.topicOut = "kafka.topic.output_order_stats";
            super.groupId = "group.id_order_stats";
        }

        public String getTopicTraffic() {
            return topicTraffic;
        }

        public void setTopicTraffic(String topicTraffic) {
            this.topicTraffic = topicTraffic;
        }

        public String getTopicWord() {
            return topicWord;
        }

        public void setTopicWord(String topicWord) {
            this.topicWord = topicWord;
        }

        public String getTopicPub() {
            return topicPub;
        }

        public void setTopicPub(String topicPub) {
            this.topicPub = topicPub;
        }

        public String getTopicRegion() {
            return topicRegion;
        }

        public void setTopicRegion(String topicRegion) {
            this.topicRegion = topicRegion;
        }
    }


    public String getParallelism() {
        return parallelism;
    }

    public void setParallelism(String parallelism) {
        this.parallelism = parallelism;
    }

    public String getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(String windowSize) {
        this.windowSize = windowSize;
    }

    public String getWaterMaker() {
        return waterMaker;
    }

    public void setWaterMaker(String waterMaker) {
        this.waterMaker = waterMaker;
    }

    public String getCheckpointInterval() {
        return checkpointInterval;
    }

    public void setCheckpointInterval(String checkpointInterval) {
        this.checkpointInterval = checkpointInterval;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getBootstrapServerIn() {
        return bootstrapServerIn;
    }

    public void setBootstrapServerIn(String bootstrapServerIn) {
        this.bootstrapServerIn = bootstrapServerIn;
    }

    public String getBootstrapServerOut() {
        return bootstrapServerOut;
    }

    public void setBootstrapServerOut(String bootstrapServerOut) {
        this.bootstrapServerOut = bootstrapServerOut;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTopicIn() {
        return topicIn;
    }

    public void setTopicIn(String topicIn) {
        this.topicIn = topicIn;
    }

    public String getTopicOut() {
        return topicOut;
    }

    public void setTopicOut(String topicOut) {
        this.topicOut = topicOut;
    }


}
