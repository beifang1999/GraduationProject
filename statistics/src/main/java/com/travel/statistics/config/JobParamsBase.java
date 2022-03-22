package com.travel.statistics.config;

import java.io.Serializable;

public class JobParamsBase implements Serializable {

    private static final long serialVersionUID = -5560602278324176230L;

    /**
     * 并行度
     */
    private int parallelism;

    /**
     * 窗口大小
     */
    private long windowSize;

    /**
     * 水位线
     */
    private long waterMaker;

    /**
     *  CheckPoint 间隔
     */

    private long checkpointInterval;

    /**
     * Job 名称
     */
    private String jobName;



    public JobParamsBase() {
    }

    public JobParamsBase(int parallelism, long windowSize, long waterMaker, long checkInterval, String jobName) {
        this.parallelism = parallelism;
        this.windowSize = windowSize;
        this.waterMaker = waterMaker;
        this.checkpointInterval = checkInterval;
        this.jobName = jobName;
    }

    public int getParallelism() {
        return parallelism;
    }

    public void setParallelism(int parallelism) {
        this.parallelism = parallelism;
    }

    public long getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(long windowSize) {
        this.windowSize = windowSize;
    }

    public long getWaterMaker() {
        return waterMaker;
    }

    public void setWaterMaker(long waterMaker) {
        this.waterMaker = waterMaker;
    }

    public long getCheckpointInterval() {
        return checkpointInterval;
    }

    public void setCheckpointInterval(long checkpointInterval) {
        this.checkpointInterval = checkpointInterval;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }


    @Override
    public String toString() {
        return "JobParamsBase{" +
                "parallelism=" + parallelism +
                ", windowSize=" + windowSize +
                ", waterMaker=" + waterMaker +
                ", checkpointInterval=" + checkpointInterval +
                ", jobName='" + jobName + '\'' +
                '}';
    }
}
