package com.travel.statistics.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.java.tuple.Tuple2;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * 用于维度查询的工具类  底层调用的是PhoenixUtil
 */
public class DimUtil {

    //从Phoenix中查询数据，没有使用缓存
    public static JSONObject getDimInfoNoCache(String tableName, Tuple2<String, String>... cloNameAndValue) {
        //拼接查询条件
        String whereSql = " where ";
        for (int i = 0; i < cloNameAndValue.length; i++) {
            Tuple2<String, String> tuple2 = cloNameAndValue[i];
            String filedName = tuple2.f0;
            String fieldValue = tuple2.f1;
            if (i > 0) {
                whereSql += " and ";
            }
            whereSql += filedName + "='" + fieldValue + "'";
        }

        String sql = "select * from " + tableName + whereSql;
        System.out.println("查询维度的SQL:" + sql);
        List<JSONObject> dimList = PhoenixUtil.queryList(sql, JSONObject.class);
        JSONObject dimJsonObj = null;
        //对于维度查询来讲，一般都是根据主键进行查询，不可能返回多条记录，只会有一条
        if (dimList != null && dimList.size() > 0) {
            dimJsonObj = dimList.get(0);
        } else {
            System.out.println("维度数据没有找到:" + sql);
            System.out.println();
        }
        return dimJsonObj;
    }


    public static JSONObject getDimInfo(String tableName, String filed, String id) {
        return getDimInfo(tableName, Tuple2.of(filed, id));
    }
    public static JSONObject getDimInfoNoCache(String tableName, String filed, String id) {
        return getDimInfoNoCache(tableName, Tuple2.of(filed, id));
    }

    public static JSONObject getDimInfo(String tableName, Tuple2<String, String>... cloNameAndValue) {
        //拼接查询条件
        String whereSql = " where ";
        String redisKey = "dim:" + tableName.toLowerCase() + ":";
        for (int i = 0; i < cloNameAndValue.length; i++) {
            Tuple2<String, String> tuple2 = cloNameAndValue[i];
            String filedName = tuple2.f0;
            String fieldValue = tuple2.f1;
            if (i > 0) {
                whereSql += " and ";
                redisKey += "_";
            }
            if (fieldValue.equals(""))
                return null;
            whereSql += filedName + "='" + fieldValue + "'";
            redisKey += fieldValue;
        }

        //从Redis中获取数据
        Jedis jedis;
        //维度数据的json字符串形式
        String dimJsonStr;
        //维度数据的json对象形式
        JSONObject dimJsonObj = null;
        try {
            //获取jedis客户端
            jedis = RedisUtil.getJedis();
            //根据key到Redis中查询
            dimJsonStr = jedis.get(redisKey);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("从redis中查询维度失败");
        }

        //判断是否从Redis中查询到了数据
        if (dimJsonStr != null && dimJsonStr.length() > 0) {
            dimJsonObj = JSON.parseObject(dimJsonStr);
        } else {
            //如果在Redis中没有查到数据，需要到Phoenix中查询
            String sql = "select * from " + tableName + whereSql;
            System.out.println("查询维度的SQL:" + sql);
            List<JSONObject> dimList = PhoenixUtil.queryList(sql, JSONObject.class);
            //对于维度查询来讲，一般都是根据主键进行查询，不可能返回多条记录，只会有一条
            if (dimList != null && dimList.size() > 0) {
                dimJsonObj = dimList.get(0);
                //将查询出来的数据放到Redis中缓存起来
                if (jedis != null) {
                    jedis.setex(redisKey, 3600 * 24, dimJsonObj.toJSONString());
                }
            } else {
                System.out.println("维度数据没有找到:" + sql);
            }
        }

        //关闭Jedis
        if (jedis != null) {
            jedis.close();
        }

        return dimJsonObj;
    }

    //根据key让Redis中的缓存失效
    public static void deleteCached(String tableName, String id) {
        String key = "dim:" + tableName.toLowerCase() + ":" + id;
        try {
            Jedis jedis = RedisUtil.getJedis();
            jedis.del(key);
            jedis.close();
        } catch (Exception e) {
            System.out.println("缓存异常！");
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        JSONObject dimInfo = DimUtil.getDimInfo("dim_region", "region_city_code", "110000");
        System.out.println(dimInfo);

//        JSONObject dimInfoNoCache = DimUtil.getDimInfoNoCache("dim_region", "region_city_code", "110000");
//        System.out.println(dimInfoNoCache);

    }
}
