package com.myself.CC.crawler.pipeline.impl;

import com.myself.CC.crawler.common.Page;
import com.myself.CC.crawler.pipeline.Pipeline;

import java.util.Map;

//输出控制台
public class ConsolePipeline implements Pipeline {
    @Override
    public void pipeline(Page page) {
        Map<String,Object> map=page.getDataSet().getAllData();
        System.out.println("==> " + map);
    }
}
