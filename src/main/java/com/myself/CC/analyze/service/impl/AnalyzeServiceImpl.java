package com.myself.CC.analyze.service.impl;

import com.myself.CC.analyze.dao.MessageDao;
import com.myself.CC.analyze.entity.PoetryInfo;
import com.myself.CC.analyze.model.AuthorCount;
import com.myself.CC.analyze.model.WordCloud;
import com.myself.CC.analyze.service.AnalyzeService;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import java.util.*;

public class AnalyzeServiceImpl implements AnalyzeService {
    private final MessageDao messageDao;

    public AnalyzeServiceImpl(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    @Override
    public List<AuthorCount> analyzeAuthorCount() {
        //未排序的结果
        return messageDao.getAuthorCountMessage();
    }

    //分析词云
    @Override
    public List<WordCloud> analyzeWordCloud() {
        //1. 查询出所有数据
        //2. 取出title content
        //3. 分词   过滤到 /w null 空 len<2的
        //4. 统计k-v：key【词】 、value【次数、词频】
        List<PoetryInfo> poetryInfos = messageDao.getAllPoetryMessage();
        Map<String, Integer> map = new HashMap<>();
        for (PoetryInfo poetryInfo : poetryInfos) {
            String title = poetryInfo.getTitle();
            String content = poetryInfo.getContent();

            List<Term> terms = new LinkedList<>();
            terms.addAll(NlpAnalysis.parse(title).getTerms());   //对题目进行分词并存入terms中
            terms.addAll(NlpAnalysis.parse(content).getTerms());  //对正文进行分词并存入terms中

            Iterator<Term> iterator = terms.iterator();
            while (iterator.hasNext()) {
                Term term = iterator.next();
                //词性的过滤
                if (term.getNatureStr() == null || term.getNatureStr().equals("w")) {
                    iterator.remove();
                    continue;
                }
                //词长度的过滤
                if (term.getRealName().length() < 2) {
                    iterator.remove();
                    continue;
                }
                String realName = term.getRealName();
                Integer count = 0;
                if (map.containsKey(realName)) {
                    count = map.get(realName) + 1;
                } else {
                    count = 1;
                }
                map.put(realName, count);
            }
        }
        List<WordCloud> list = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            WordCloud wordCloud = new WordCloud();
            wordCloud.setWord(entry.getKey());
            wordCloud.setCount(entry.getValue());
            list.add(wordCloud);
        }
        return list;
    }

}
