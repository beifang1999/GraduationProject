package com.travel.producer.data.util.data.travel.logs;

import com.alibaba.fastjson.JSON;


import com.travel.producer.data.constant.CommonConstant;
import com.travel.producer.data.dvo.GisDO;
import com.travel.producer.data.enumes.*;
import com.travel.producer.data.util.AmapGisUtil;
import com.travel.producer.data.util.CSVUtil;
import com.travel.producer.data.util.CommonUtil;
import com.travel.producer.data.util.HttpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TravelCurLogHelper implements Runnable {

    private int sleepTime;

    @Value("${travel_url}")
    public String url;

    private final static Logger log = LoggerFactory.getLogger(TravelCurLogHelper.class);

    public static final String PRODUCT_KEY_ID = "product_id";


    //旅游产品
    public static List<String> product = new ArrayList<>();

    public TravelCurLogHelper() {
    }

    public TravelCurLogHelper(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public static List<String> getProducts() {
        if (CollectionUtils.isEmpty(product)) {
            try {
                List<Map<String, String>> pubDatas = CSVUtil.readCSVFile(CSVUtil.PRODUCT_FILE, CSVUtil.QUOTE_COMMON);
                if (CollectionUtils.isNotEmpty(pubDatas)) {
                    for (Map<String, String> pub : pubDatas) {
                        String productID = pub.getOrDefault(PRODUCT_KEY_ID, "");
                        product.add(productID);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("TravelOrderHeler.product.error:", e);
            }
        }
        return product;
    }


    //=====基础信息==============================================================
    List<String> productIDs = getProducts();

    //请求ID
    public static final String KEY_SID = "sid";
    //用户ID
    public static final String KEY_USER_ID = "userID";
    //用户设备号
    public static final String KEY_USER_DEVICE = "userDevice";

    //用户设备类型
    public static final String KEY_USER_DEVICE_TYPE = "userDeviceType";
    //操作系统
    public static final String KEY_OS = "os";
    //手机制造商
    public static final String KEY_MANUFACTURER = "manufacturer";
    //电信运营商
    public static final String KEY_CARRIER = "carrier";
    //网络类型
    public static final String KEY_NETWORK_TYPE = "networkType";
    //用户所在地区
    public static final String KEY_USER_REGION = "userRegion";
    //用户所在地区IP
    public static final String KEY_USER_REGION_IP = "userRegionIP";
    //经度
    public static final String KEY_LONGITUDE = "longitude";
    //纬度
    public static final String KEY_LATITUDE = "latitude";

    //行为类型
    public static final String KEY_ACTION = "action";

    //事件类型
    public static final String KEY_EVENT_TYPE = "eventType";

    //事件目的
    public static final String KEY_EVENT_TARGET_TYPE = "eventTargetType";


    //创建时间
    public static final String KEY_CT = "ct";

    //=====扩展信息==============================================================

    public static final String KEY_EXTS = "exts";


    //目标信息
    public static final String KEY_EXTS_TARGET_ID = "targetID";



    //用户数量限制级别
    public static final Integer USER_COUNT_LEVEL = 5;


    //地区信息
    public static List<GisDO> giss;

    static {
        giss = AmapGisUtil.initDatas();
    }



    public static Map<String, Object> getTravelCommonData(String curTime, int countLevel, List<GisDO> giss) {

        Map<String, Object> result = new HashMap<>();

        //请求id
        String sid = CommonUtil.getRandomChar(USER_COUNT_LEVEL);
        String sidNum = curTime + sid;
        result.put(KEY_SID, sidNum);

        //用户ID
        String userId = CommonUtil.getRandomNumStr(countLevel);
        result.put(KEY_USER_ID, userId);

        //用户设备号
        String userDevice = CommonUtil.getRandomNumStr(countLevel);
        result.put(KEY_USER_DEVICE, userDevice);

        //用户设备类型
        String userDeviceType = CommonUtil.getRandomElementRange(DeviceTypeEnum.getDeviceTypes());
        result.put(KEY_USER_DEVICE_TYPE, userDeviceType);

        //操作系统
        String os = CommonUtil.getRandomElementRange(OSEnum.getOS());
        result.put(KEY_OS, os);

        String manufacturer = CommonUtil.getRandomElementRange(ManufacturerEnum.getManufacturers());
        //手机制造商
        if (OSEnum.IOS.getCode().equalsIgnoreCase(os)) {
            manufacturer = ManufacturerEnum.APPLE.getCode();
        }
        result.put(KEY_MANUFACTURER, manufacturer);

        //电信运营商
        String carrier = CommonUtil.getRandomElementRange(CarrierEnum.getCarriers());
        result.put(KEY_CARRIER, carrier);

        //网络类型
        String networkType = CommonUtil.getRandomElementRange(NetworkTypeEnum.getNetworkTypes());
        result.put(KEY_NETWORK_TYPE, networkType);

        //用户所在地区
        GisDO gisDO = CommonUtil.getRandomElementRange(giss);
        result.put(KEY_USER_REGION, gisDO.getAdcode());

        //经度|纬度
        result.put(KEY_LONGITUDE, gisDO.getLongitude());
        result.put(KEY_LATITUDE, gisDO.getLatitude());

        //用户所在地区IP
        result.put(KEY_USER_REGION_IP, CommonUtil.getRadomIP());

        //行为类型
        List<String> midActions = ActionEnum.getMidActions();
        String action = CommonUtil.getRandomElementRange(midActions);
        result.put(KEY_ACTION, action);

        //事件类型
        String eventType = "";
        if (ActionEnum.INTERACTIVE.getCode().equalsIgnoreCase(action)) {
            eventType = CommonUtil.getRandomElementRange(EventEnum.getInterActiveEvents());
        }
        result.put(KEY_EVENT_TYPE, eventType);

        //创建时间
        long ct = CommonUtil.getSelectTimestamp(curTime, CommonConstant.FORMATTER_YYYYMMDDHHMMDD);
        result.put(KEY_CT, ct);


        return result;
    }



    public static Map<String, Object> getTravelExtData(Map<String, Object> commonData, List<GisDO> giss, List<String> productIDs) {

        Map<String, Object> result = new HashMap<String, Object>();

        //行为类型
        String action = commonData.getOrDefault(KEY_ACTION, "").toString();
        //事件类型
        String eventType = commonData.getOrDefault(KEY_EVENT_TYPE, "").toString();

        //扩展信息
        Map<String, String> subDatas = new HashMap<String, String>();
        if (ActionEnum.LAUNCH.getCode().equalsIgnoreCase(action)) {
//            无具体扩展信息
        }
        else if (ActionEnum.INTERACTIVE.getCode().equalsIgnoreCase(action)) {
            //点击事件
            if (EventEnum.CLICK.getCode().equalsIgnoreCase(eventType)) {
                //点击事件
                //产品ID页面
                String productID = CommonUtil.getRandomElementRange(productIDs);
                subDatas.put(KEY_EXTS_TARGET_ID, productID);

                //事件类型
                String eventAction = CommonUtil.getRandomElementRange(EventActionEnum.getEventActions());
                subDatas.put(KEY_EVENT_TARGET_TYPE, eventAction);
            }
        }
        result.put(KEY_EXTS, JSON.toJSONString(subDatas));

        return result;
    }


    /**
     * 模拟旅游行业用户行为日志
     *
     * @return
     */
    public String getTravelODSData(String curTime, int countLevel, List<GisDO> giss, List<String> productIDs) {

        Map<String, Object> result = new HashMap<String, Object>();

        //基础数据
        Map<String, Object> commonData = getTravelCommonData(curTime, countLevel, giss);
        result.putAll(commonData);

        Map<String, Object> extsData = getTravelExtData(commonData, giss, productIDs);

        result.putAll(extsData);
        String dataJson = JSON.toJSONString(result);
        return dataJson;

    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    @Override
    public void run() {

        String curTime = CommonUtil.formatDate(new Date(), "yyyyMMddHHmmss");

        String travelODSData = getTravelODSData(curTime, USER_COUNT_LEVEL, giss, productIDs);
        System.out.println(Thread.currentThread().getId()+travelODSData);

        try {
            Thread.sleep(this.getSleepTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpUtil.get(travelODSData, url);
    }
}
