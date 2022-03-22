package com.travel.statistics.udf.map.dwd;

import com.travel.statistics.domain.LogMsgBase;
import com.travel.statistics.domain.dwd.ProductListView;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class ProductListViewMapFunction implements MapFunction<LogMsgBase, ProductListView> , Serializable {


    private static final long serialVersionUID = 4425315163244030759L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductListViewMapFunction.class);

    private static final ObjectMapper AM_OM = new ObjectMapper();

    @Override
    public ProductListView map(LogMsgBase value) throws Exception {
        String exts = value.getExts();
        ProductListView result = AM_OM.readValue(exts, ProductListView.class);
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
        result.setHotTarget(value.getHotTarget());
        result.setNetworkType(value.getNetworkType());
        return result;
    }
}
