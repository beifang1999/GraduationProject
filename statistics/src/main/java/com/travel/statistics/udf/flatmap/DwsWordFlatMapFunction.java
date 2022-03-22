package com.travel.statistics.udf.flatmap;

import com.travel.statistics.domain.dws.OrderTranslation;
import com.travel.statistics.domain.dws.WordResult;
import com.travel.statistics.utils.KeywordUtil;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DwsWordFlatMapFunction implements FlatMapFunction<OrderTranslation, WordResult> {
    private static final long serialVersionUID = -7551280514487149495L;

    @Override
    public void flatMap(OrderTranslation value, Collector<WordResult> out) {
        WordResult result = new WordResult();
        result.setTimeStamp(value.getTimeStamp());
        String title = value.getProductTitle();
        String pubName = value.getPubName();
        List<String> pubNameWord =new ArrayList<>();

        if (pubName != null) {
            pubNameWord = KeywordUtil.analyze(pubName);
        }

        List<String> titleWord = KeywordUtil.analyze(title);

        long size = Math.max(pubNameWord.size(), titleWord.size());
        for (int i = 0; i < size; i++) {
            if (titleWord.size() > i) {
                result.setTitleWord(titleWord.get(i));
                result.setTitleValue(1);
            }
            if (pubNameWord.size() > i) {
                result.setPubWord(pubNameWord.get(i));
                result.setPubValue(1);
            }

            out.collect(result);
        }
    }
}
