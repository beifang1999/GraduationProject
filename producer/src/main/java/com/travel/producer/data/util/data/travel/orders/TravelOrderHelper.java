package com.travel.producer.data.util.data.travel.orders;

import com.alibaba.fastjson.JSON;


import com.travel.producer.data.constant.CommonConstant;
import com.travel.producer.data.dvo.RegionDO;
import com.travel.producer.data.enumes.TrafficEnum;
import com.travel.producer.data.enumes.TrafficGoBackEnum;
import com.travel.producer.data.enumes.TrafficSeatEnum;
import com.travel.producer.data.util.CSVUtil;
import com.travel.producer.data.util.CommonUtil;
import com.travel.producer.domain.Order;
import com.travel.producer.util.MyBatisUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

@Component
public class TravelOrderHelper implements Runnable, Serializable {

    private long sleepTime;

    public long getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    @Autowired
    ThreadPoolTaskExecutor poolExecutor;

    private final static Logger log = LoggerFactory.getLogger(TravelOrderHelper.class);


    //订单编号ID: 时间戳+产品ID+用户ID
    public static final String KEY_ORDER_ID = "order_id";

    //用户ID：(在一些场景下，平台会为用户构造的唯一编号)
    public static final String KEY_USER_ID = "user_id";

    //预留手机号
    public static final String KEY_USER_MOBILE = "user_mobile";

    //旅游产品编号：
    public static final String KEY_PRODUCT_ID = "product_id";

    //旅游产品交通资源：旅游交通选择:飞机|高铁|火车 (出行方式)
    public static final String KEY_PRODUCT_TRAFFIC = "product_traffic";


    //旅游产品交通资源：旅游交通选择:商务|一等|软卧... (座席)
    public static final String KEY_PRODUCT_TRAFFIC_GRADE = "product_traffic_grade";


    //单程|往返
    public static final String KEY_PRODUCT_TRAFFIC_TYPE = "product_traffic_type";

    //旅游产品住宿资源：
    public static final String KEY_PRODUCT_PUB = "product_pub";

    //所在区域：
    public static final String KEY_USER_REGION = "user_region";

    //人员构成_成人人数：
    public static final String KEY_TRAVEL_MEMBER_ADULT = "travel_member_adult";

    //人员构成_儿童人数：
    public static final String KEY_TRAVEL_MEMBER_YONGER = "travel_member_yonger";

    //人员构成_婴儿人数：
    public static final String KEY_TRAVEL_MEMBER_BABY = "travel_member_baby";


    //活动特价：0无活动特价|其他为折扣率如0.8
    public static final String KEY_HAS_ACTIVITY = "has_activity";

    //下单时间：
    public static final String KEY_ORDER_CT = "order_ct";


    //====================================================
    //地区
    public static final String REGION_KEY_CODE = "region_code";
    public static final String REGION_KEY_CODE_DESC = "region_code_desc";
    public static final String REGION_KEY_CITY = "region_city";
    public static final String REGION_KEY_CITY_DESC = "region_city_desc";
    public static final String REGION_KEY_PROVINCE = "region_province";
    public static final String REGION_KEY_PROVINCE_DESC = "region_province_desc";

    //酒店
    public static final String PUB_KEY_ID = "pub_id";

    //产品
    public static final String PRODUCT_KEY_ID = "product_id";

    private static final long serialVersionUID = -498669910806367514L;


    //地区信息
    public static List<RegionDO> regions = new ArrayList<>();

    public static List<RegionDO> getRegions() {
        if (CollectionUtils.isEmpty(regions)) {
            try {
                List<Map<String, String>> regionDatas = CSVUtil.readCSVFile(CSVUtil.REGION_FILE, CSVUtil.QUOTE_COMMON);
                if (CollectionUtils.isNotEmpty(regionDatas)) {
                    for (Map<String, String> region : regionDatas) {
                        RegionDO regionDO = new RegionDO();
                        regionDO.setRegionCity(region.getOrDefault(REGION_KEY_CITY, ""));
                        regionDO.setRegionCityDesc(region.getOrDefault(REGION_KEY_CITY_DESC, ""));
                        regionDO.setRegionCode(region.getOrDefault(REGION_KEY_CODE, ""));
                        regionDO.setRegionCodeDesc(region.getOrDefault(REGION_KEY_CODE_DESC, ""));
                        regionDO.setRegionProvince(region.getOrDefault(REGION_KEY_PROVINCE, ""));
                        regionDO.setRegionProvinceDesc(region.getOrDefault(REGION_KEY_PROVINCE_DESC, ""));
                        regions.add(regionDO);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("TravelOrderHeler.region.error:", e);
            }
        }
        return regions;
    }

    /**
     * 旅游产品与酒店映射
     */
    public static Map<String, String> proMappingPub = new HashMap<>();

    public static Map<String, String> getPubMappingPro() {
        if (MapUtils.isEmpty(proMappingPub)) {
            try {
                List<Map<String, String>> pubDatas = CSVUtil.readCSVFile(CSVUtil.PUB_FILE, CSVUtil.QUOTE_COMMON);
                if (CollectionUtils.isNotEmpty(pubDatas)) {
                    for (Map<String, String> pub : pubDatas) {
                        String pubID = pub.getOrDefault(PUB_KEY_ID, "");
                        String[] pps = pubID.split("\\|");
                        proMappingPub.put(pps[0], pubID);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("TravelOrderHeler.pub2pro.error:", e);
            }
        }
        return proMappingPub;
    }


    //旅游产品
    public static List<String> product = new ArrayList<>();

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

    /**
     * 模拟旅游产品订单
     *
     * @return 旅游订单
     */
    public static Map<String, Object> getTravelOrderData(String curTime, int countLevel, List<RegionDO> regions, List<String> productIDs, Map<String, String> pubs) {
        Map<String, Object> result = new HashMap<>();

        //用户ID
        String userId = CommonUtil.getRandomNumStr(countLevel);
        result.put(KEY_USER_ID, userId);

        //预留手机号
        String userMobile = CommonUtil.getMobile();
        result.put(KEY_USER_MOBILE, userMobile);

        //旅游产品编号：
        String productID = CommonUtil.getRandomElementRange(productIDs);
        result.put(KEY_PRODUCT_ID, productID);

        //旅游产品住宿资源：
        String pubID = pubs.getOrDefault(productID, "");
        result.put(KEY_PRODUCT_PUB, pubID);

        //用户所在地区
        RegionDO regionDO = CommonUtil.getRandomElementRange(regions);
        String userRegion = regionDO.getRegionCode();
        result.put(KEY_USER_REGION, userRegion);

        //旅游产品交通资源：旅游交通选择:飞机|高铁|火车 (出行方式+座席)
        List<String> traffics = TrafficEnum.getAllTraffics();
        String traffic = CommonUtil.getRandomElementRange(traffics);
        result.put(KEY_PRODUCT_TRAFFIC, traffic);

        //席座
        List<String> trafficSeas;
        if (TrafficEnum.AIRPLAN.getCode().equalsIgnoreCase(traffic)) {
            trafficSeas = TrafficSeatEnum.getAirTrafficSeats();
        } else if (TrafficEnum.GTRAIN.getCode().equalsIgnoreCase(traffic)) {
            trafficSeas = TrafficSeatEnum.getTrainTrafficSeats();
        } else if (TrafficEnum.CTRAIN.getCode().equalsIgnoreCase(traffic)) {
            trafficSeas = TrafficSeatEnum.getTrainSeats();
        } else {
            trafficSeas = TrafficSeatEnum.getAirTrafficSeats();
        }
        String trafficSea = CommonUtil.getRandomElementRange(trafficSeas);
        result.put(KEY_PRODUCT_TRAFFIC_GRADE, trafficSea);


        //单程|往返
        List<String> trafficTrips = TrafficGoBackEnum.getAllTrafficGoBacks();
        String trafficTrip = CommonUtil.getRandomElementRange(trafficTrips);
        result.put(KEY_PRODUCT_TRAFFIC_TYPE, trafficTrip);

        //人员构成_成人人数：
        int begin = CommonConstant.DEF_NUMBER_ONE;
        int end = CommonConstant.DEF_NUMBER_DUL;
        int sep = CommonConstant.DEF_NUMBER_ONE;
        List<Integer> adults = CommonUtil.getRangeNumeric(begin, end, sep);
        Integer adult = CommonUtil.getRandomElementRange(adults);
        result.put(KEY_TRAVEL_MEMBER_ADULT, String.valueOf(adult));

        //儿童及婴儿数量
        int yonger = 0;
        int baby = 0;
        if (CommonConstant.DEF_NUMBER_DUL == adult) {
            int randomNumber = CommonUtil.getRandomNum(2) % 3;
            if (randomNumber == 1) {
                yonger = 1;
            } else if (randomNumber == 2) {
                baby = 1;
            }
        }

        //人员构成_儿童人数：
        result.put(KEY_TRAVEL_MEMBER_YONGER, String.valueOf(yonger));

        //人员构成_婴儿人数：
        result.put(KEY_TRAVEL_MEMBER_BABY, String.valueOf(baby));


        //活动特价：0无活动特价|其他为折扣率如0.8
        int feeRatioRandom = CommonUtil.getRandomNum(1);
        int feeRatio = CommonConstant.DEF_NUMBER_ZERO;
        if (feeRatioRandom > 5) {
            feeRatio = feeRatioRandom;
        }
        result.put(KEY_HAS_ACTIVITY, String.valueOf(feeRatio));


        //创建时间
        long ct = CommonUtil.getSelectTimestamp(curTime, CommonConstant.FORMATTER_YYYYMMDDHHMMDD);
        result.put(KEY_ORDER_CT, String.valueOf(ct));

        //订单编号ID: 时间戳+产品ID+用户ID
        String orderID = ct + productID + userId;
        result.put(KEY_ORDER_ID, orderID);
        return result;
    }


//    public void start(int count,int sleepTime,int core) {
//
//
//        //辅助数据
//        //地区信息
//        List<RegionDO> regions = getRegions();
//        Map<String, String> pubs = getPubMappingPro();
//        List<String> productIDs = getProducts();
//
//        poolExecutor.setCorePoolSize(core);
//
//        for (int i = 0; i < count; i++) {
//            poolExecutor.submit(() -> {
//                SqlSession session = MyBatisUtils.openSession();
//                String curTime = CommonUtil.formatDate(new Date(), "yyyyMMddHHmmss");
//
//                Order order = JSON.parseObject(JSON.toJSONString(getTravelOrderData(curTime, CommonConstant.USER_COUNT_LEVEL, regions, productIDs, pubs)), Order.class);
//                session.insert("order.insert", order);
//                session.commit();
//                System.out.println("ods.data send =" + order);
//                MyBatisUtils.closeSession(session);
//                try {
//                    Thread.sleep(sleepTime);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//        }
//
//        while (true) {
//            try {
//                Thread.sleep(1000);
//                if (poolExecutor.getActiveCount() == 0) {
//                    poolExecutor.destroy();
//                    break;
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }


    Map<String, String> pubs = getPubMappingPro();
    List<String> productIDs = getProducts();
    List<RegionDO> region = getRegions();
    @Override
    public void run() {


        SqlSession session = MyBatisUtils.openSession();
        String curTime = CommonUtil.formatDate(new Date(), "yyyyMMddHHmmss");

        Order order = JSON.parseObject(JSON.toJSONString(getTravelOrderData(curTime, CommonConstant.USER_COUNT_LEVEL, region, productIDs, pubs)), Order.class);
        session.insert("order.insert", order);
        session.commit();
        System.out.println("ods.data send =" + order);
        MyBatisUtils.closeSession(session);
        try {
            Thread.sleep(this.getSleepTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

