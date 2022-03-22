package com.travel.statistics.config;

public class CommonConfig {
    //Hbase的命名空间
    public static final String HABSE_SCHEMA = "TRAVEL";

    //Phonenix连接的服务器地址
    public static final String PHOENIX_SERVER="jdbc:phoenix:hadoop01,hadoop02,hadoop03:2181";

    //ClickHouse的URL连接地址
    public static final String CLICKHOUSE_URL="jdbc:clickhouse://192.168.193.100:8123/default";
}
