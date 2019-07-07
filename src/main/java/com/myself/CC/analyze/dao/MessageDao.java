package com.myself.CC.analyze.dao;

import com.myself.CC.analyze.entity.PoetryInfo;
import com.myself.CC.analyze.model.AuthorCount;

import java.util.List;

//把相关数据从数据库中查询、提取出来
public interface MessageDao {
    //分析唐诗中作者以及对应的创作数量
    List<AuthorCount> getAuthorCountMessage();

    //查询所有的诗文，提供给业务层service分析
    List<PoetryInfo> getAllPoetryMessage();
}
