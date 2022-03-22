package com.travel.statistics.udf.map.dwd;

import com.travel.statistics.domain.LogMsgBase;
import com.travel.statistics.domain.dwd.Launch;
import org.apache.flink.api.common.functions.MapFunction;

public class LaunchMapFunction implements MapFunction<LogMsgBase, Launch> {
    @Override
    public Launch map(LogMsgBase value) {
        Launch result = new Launch();
        result.setLongitude(value.getLongitude());
        result.setOs(value.getOs());
        result.setLatitude(value.getLatitude());
        result.setUserRegion(String.valueOf((Integer.parseInt(value.getUserRegion()) / 100) * 100));
        result.setUserID(value.getUserID());
        result.setSid(value.getSid());
        result.setManufacturer(value.getManufacturer());
        result.setDuration(value.getDuration());
        result.setTimeStamp(value.getTimeStamp() );
        result.setCarrier(value.getCarrier());
        result.setUserDeviceType(value.getUserDeviceType());
        result.setAction(value.getAction());
        result.setUserDevice(value.getUserDevice());
        result.setNetworkType(value.getNetworkType());
        return result;
    }
}
