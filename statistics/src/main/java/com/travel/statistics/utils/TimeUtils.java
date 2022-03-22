package com.travel.statistics.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    public static long dateToStamp(String s) throws Exception {
        String res;//设置时间格式，将该时间格式的时间转换为时间戳
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long time = date.getTime();
        return time;
    }
    //将时间戳转换为时间
    public static String stampToTime(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);//将时间戳转换为时间
        Date date = new Date(lt);//将时间调整为yyyy-MM-dd HH:mm:ss时间样式
        res = simpleDateFormat.format(date);
        return res;
    }

}
