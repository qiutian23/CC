package com.myself.CC.crawler.parse.impl;

import com.gargoylesoftware.htmlunit.html.*;
import com.myself.CC.crawler.common.Page;
import com.myself.CC.crawler.parse.Parse;


//详情页面解析
public class DataPageParse implements Parse {
    @Override
    public void parse(Page page) {
        if(!page.isDetail()){ //如果是文档页面则不处理
            return;
        }

        HtmlPage htmlPage=page.getHtmlPage();
        HtmlElement body=htmlPage.getBody();

        //标题
        String titlePath="//div[@class='sons']//div[@class='cont']/h1/text()";
        DomText titleDom= (DomText) body.getByXPath(titlePath).get(0);
        String title=titleDom.asText();

        //朝代
        String dynastyPath="//div[@class='sons']//div[@class='cont']/p[@class='source']/a[1]";
        HtmlAnchor dynastyDom= (HtmlAnchor) body.getByXPath(dynastyPath).get(0);
        String dynasty=dynastyDom.asText();

        //作者
        String authorPath="//div[@class='sons']//div[@class='cont']/p[@class='source']/a[2]";
        HtmlAnchor authorDom= (HtmlAnchor) body.getByXPath(authorPath).get(0);
        String author=authorDom.asText();

        //正文
        String contentPath="//div[@class='sons']//div[@class='cont']/div[@class='contson']";
        HtmlDivision contentDiv= (HtmlDivision) body.getByXPath(contentPath).get(0);
        String content=contentDiv.asText();

        //可以在dataSet中放更多数据，而不仅限于PoetryInfo类中的属性数据
        page.getDataSet().putData("title",title);
        page.getDataSet().putData("author",author);
        page.getDataSet().putData("dynasty",dynasty);

        page.getDataSet().putData("content",content);

        page.getDataSet().putData("url",page.getUrl());
    }
}
