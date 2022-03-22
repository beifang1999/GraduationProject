package com.travel.statistics.udf.window;


import com.travel.statistics.domain.dws.WordResult;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class WordWindowFunction extends ProcessWindowFunction<WordResult, WordResult, String, TimeWindow> {
    @Override
    public void process(String s, Context context, Iterable<WordResult> elements, Collector<WordResult> out) throws Exception {
        long end = context.window().getEnd();
        // 5 min 滚动窗口时间区间的划分:如 19:23:21  19:24:59 的日志都划分到 [20,25) 时间区间,
        // 并以窗口的结束时间（19:24:59，近似为19:25:00）作为汇总后的时间
        elements.forEach(e ->{
            e.setTimeStamp(end);
            out.collect(e);
        });
    }
}
