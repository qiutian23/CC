package com.myself.CC.crawler.common;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

//所抓取的网页页面
@Data
public class Page {
    /*
     * 数据网站的根地址
     * 比如：https://so.gushiwen.org
     * */
    private final String base;

    /*
    * 具体网页的路径
    * 比如：/shiwenv_ce802de625e5.aspx
    * */
    private final String path;

    //标识网页是否是详情页
    private final boolean detail;

    //网页DOM（文档对象模型）对象
    private HtmlPage htmlPage;

    //子页面对象
    private Set<Page> subPage = new HashSet<>();

    //数据对象
    private DataSet dataSet = new DataSet();

    public String getUrl() {
        return this.base + this.path;
    }
}

