package com.travel.statistics.udf.reduce;

import com.travel.statistics.domain.dws.WordResult;
import org.apache.flink.api.common.functions.ReduceFunction;

public class DwsPubReduceFunction implements ReduceFunction<WordResult> {


    private static final long serialVersionUID = 5736702661556286520L;

    @Override
    public WordResult reduce(WordResult value1, WordResult value2) {
        value1.setPubValue(value1.getPubValue() + value2.getPubValue());
        return value1;
    }
}
