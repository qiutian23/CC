package com.myself.CC.analyze.model;

import lombok.Data;

//词云：每个词以及对应的词频
@Data
public class WordCloud {
    private String word;
    private Integer count;
}