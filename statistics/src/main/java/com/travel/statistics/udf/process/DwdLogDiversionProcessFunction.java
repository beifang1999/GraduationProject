package com.travel.statistics.udf.process;

import com.travel.statistics.domain.LogMsgBase;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

public class DwdLogDiversionProcessFunction extends ProcessFunction<LogMsgBase, LogMsgBase> {


    private static final long serialVersionUID = -6290826623997086872L;
    OutputTag<LogMsgBase> launch;
    OutputTag<LogMsgBase>   pageView ;
    OutputTag<LogMsgBase> click ;


    public DwdLogDiversionProcessFunction(OutputTag<LogMsgBase> launch, OutputTag<LogMsgBase> pageView, OutputTag<LogMsgBase> click) {
        this.launch = launch;
        this.pageView = pageView;
        this.click = click;
    }

    @Override
    public void processElement(LogMsgBase value, Context ctx, Collector<LogMsgBase> out)  {
        if (value.getAction().equals("launch"))
            ctx.output(launch, value);
        else if (value.getAction().equals("page_enter_native"))
            ctx.output(pageView, value);
        else if (value.getAction().equals("interactive") && value.getEventType().equals("click"))
            ctx.output(click, value);
        else out.collect(value);
    }
}
