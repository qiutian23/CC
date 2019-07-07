package com.myself.CC.crawler.pipeline;

import com.myself.CC.crawler.common.Page;

//通过管道传递给清洗
public interface Pipeline {
    void pipeline(Page page);
}
