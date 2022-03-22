package com.travel.statistics.udf.map.dwd;

import com.travel.statistics.domain.LogMsgBase;
import com.travel.statistics.domain.dwd.PageView;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

public class PageViewMapFunction implements MapFunction<LogMsgBase, PageView> {

    private static final long serialVersionUID = 5731055959333988086L;

    private static final ObjectMapper AM_OM = new ObjectMapper();


    @Override
    public PageView map(LogMsgBase value) throws Exception {
        PageView result = AM_OM.readValue(value.getExts(), PageView.class);
        result.setLongitude(value.getLongitude());
        result.setOs(value.getOs());
        result.setLatitude(value.getLatitude());
        result.setUserRegion(String.valueOf(Integer.getInteger(value.getUserRegion()) / 100 * 100));
        result.setEventType(value.getEventType());
        result.setUserID(value.getUserID());
        result.setSid(value.getSid());
        result.setManufacturer(value.getManufacturer());
        result.setDuration(value.getDuration());
        result.setTimeStamp(value.getTimeStamp());
        result.setCarrier(value.getCarrier());

        result.setUserDeviceType(value.getUserDeviceType());
        result.setAction(value.getAction());
        result.setUserDevice(value.getUserDevice());
        result.setNetworkType(value.getNetworkType());
        return result;
    }
}
