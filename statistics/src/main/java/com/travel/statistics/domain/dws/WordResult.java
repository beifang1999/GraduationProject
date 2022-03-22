package com.travel.statistics.domain.dws;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WordResult implements Serializable {
    private static final long serialVersionUID = -8997317779504776200L;
    public long timeStamp;
    public String  pubWord;
    public String  titleWord;
    public long titleValue;
    public long pubValue;
}
