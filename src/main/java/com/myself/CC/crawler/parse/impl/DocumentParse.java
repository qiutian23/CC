package com.myself.CC.crawler.parse.impl;

import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.myself.CC.crawler.common.Page;
import com.myself.CC.crawler.parse.Parse;

import java.util.function.Consumer;

/*
 * 处理文档页面
 * 链接解析
 * */
//文档页面解析
public class DocumentParse implements Parse {
    @Override
    public void parse(Page page) {
        if (page.isDetail()) {//如果是详情页面则不处理
            return;
        }
        HtmlPage htmlPage = page.getHtmlPage(); //获取一个单纯的网页页面
        htmlPage.getBody().getElementsByAttribute("div", "class", "typecont")
                .forEach(new Consumer<HtmlElement>() {//找到body体中div元素下class属性为typecont值
                    @Override
                    public void accept(HtmlElement htmlElement) {
                        DomNodeList<HtmlElement> nodeList = htmlElement.getElementsByTagName("a");
                        nodeList.forEach(new Consumer<HtmlElement>() {
                            @Override
                            public void accept(HtmlElement aNode) {
                                String path = aNode.getAttribute("href"); //获取到path
                                Page subPage = new Page(page.getBase(), path, true);
                                page.getSubPage().add(subPage);
                            }
                        });
                    }
                });
    }
}
