package com.travel.statistics.udf.reduce;

import com.travel.statistics.domain.dws.WordResult;
import org.apache.flink.api.common.functions.ReduceFunction;

public class DwsWordReduceFunction implements ReduceFunction<WordResult> {
    private static final long serialVersionUID = 398113583071922108L;

    @Override
    public WordResult reduce(WordResult value1, WordResult value2) throws Exception {
        value1.setTitleValue(value1.getTitleValue() + value2.getTitleValue());
        return value1;
    }

}
