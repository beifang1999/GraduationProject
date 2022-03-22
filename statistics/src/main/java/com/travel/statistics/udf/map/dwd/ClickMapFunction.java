package com.travel.statistics.udf.map.dwd;

import com.travel.statistics.domain.LogMsgBase;
import com.travel.statistics.domain.dwd.Click;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

public class ClickMapFunction implements MapFunction<LogMsgBase, Click> {


    private static final ObjectMapper AM_OM = new ObjectMapper();
    private static final long serialVersionUID = -5562962614493954395L;


    @Override
    public Click map(LogMsgBase value) throws Exception {
        Click result = AM_OM.readValue(value.getExts(), Click.class);
        result.setLongitude(value.getLongitude());
        result.setOs(value.getOs());
        result.setLatitude(value.getLatitude());

        result.setUserRegion(String.valueOf((Integer.parseInt(value.getUserRegion()) / 100) * 100));
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
