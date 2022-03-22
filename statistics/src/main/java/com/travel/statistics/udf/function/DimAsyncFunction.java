package com.travel.statistics.udf.function;


import com.alibaba.fastjson.JSONObject;

import com.travel.statistics.utils.DimUtil;
import com.travel.statistics.utils.ThreadPoolUtil;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;

/**
 * 自定义维度异步查询的函数
 * 模板方法设计模式
 * 在父类中只定义方法的声明，让整个流程跑通
 * 具体的实现延迟到子类中实现
 */
public abstract class DimAsyncFunction<T> extends RichAsyncFunction<T, T> implements DimJoinFunction<T> {

    //线程池对象的父接口生命（多态）
    private ExecutorService executorService;

    //维度的表名
    private String tableName;
    private String filed;

    public DimAsyncFunction(String tableName, String filed) {
        this.tableName = tableName;
        this.filed = filed;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        //初始化线程池对象
        System.out.println("初始化线程池对象");
        executorService = ThreadPoolUtil.getInstance();
    }

    /**
     * 发送异步请求的方法
     * @param obj          流中的事实数据
     * @param resultFuture 异步处理结束之后，返回结果
     * @throws Exception
     */
    @Override
    public void asyncInvoke(T obj, ResultFuture<T> resultFuture) {
        executorService.submit(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //发送异步请求
                            //从流中事实数据获取key
                            String key = getKey(obj);

                            //根据维度的主键到维度表中进行查询
                            JSONObject dimInfoJsonObj = DimUtil.getDimInfo(tableName, filed, key);
                            //System.out.println("维度数据Json格式：" + dimInfoJsonObj);

                            if (dimInfoJsonObj != null) {
                                //维度关联  流中的事实数据和查询出来的维度数据进行关联
                                join(obj, dimInfoJsonObj);
                            }
                            //将关联后的数据数据继续向下传递
                            resultFuture.complete(Arrays.asList(obj));
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new RuntimeException(tableName + "维度异步查询失败");
                        }
                    }
                }
        );
    }

    @Override
    public void close() throws Exception {
        super.close();
        executorService.shutdown();
    }
}

