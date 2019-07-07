package com.myself.CC.analyze.service;

import com.myself.CC.analyze.model.AuthorCount;
import com.myself.CC.analyze.model.WordCloud;

import java.util.List;


//分析
public interface AnalyzeService {
    //分析唐诗中作者以及对应的创作数量
    List<AuthorCount> analyzeAuthorCount();
    //词云分析
    List<WordCloud> analyzeWordCloud();

}
