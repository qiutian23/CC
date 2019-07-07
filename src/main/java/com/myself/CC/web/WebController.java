package com.myself.CC.web;

import com.google.gson.Gson;
import com.myself.CC.analyze.model.AuthorCount;
import com.myself.CC.analyze.model.WordCloud;
import com.myself.CC.analyze.service.AnalyzeService;
import com.myself.CC.config.ObjectFactory;
import com.myself.CC.crawler.Crawler;
import spark.*;

import java.util.List;

public class WebController {
    private final AnalyzeService analyzeService;

    public WebController(AnalyzeService analyzeService) {
        this.analyzeService = analyzeService;
    }


    //  http://127.0.0.1:4567/
    //  /analyze/author_count
    private List<AuthorCount> getAuthorCount(){
        return this.analyzeService.analyzeAuthorCount();
    }

    //  http://127.0.0.1:4567/
    //  /analyze/word_cloud
    private List<WordCloud> getWordCloud(){
        return this.analyzeService.analyzeWordCloud();
    }


    public void launch(){
        ResponseTransformer transformer=new JsonTransformer();
        //src/main/resources/static
        //前端静态文件目录  可以访问到该目录下的文件，有了页面之后就可以在里边加可视化界面了【可视化工具：ECharts】
        //要使用ECharts，就必须下载js库
        Spark.staticFileLocation("/static");
        //服务端接口
        Spark.get("/analyze/author_count", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return getAuthorCount();
            }
        },transformer);
        Spark.get("/analyze/word_cloud", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return getWordCloud();
            }
        },transformer);
        Spark.get("/crawler/stop", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                Crawler crawler=ObjectFactory.getInstance().getObject(Crawler.class);
                crawler.stop();
                return "爬虫停止";
            }
        });
    }

    //转换器
    public class JsonTransformer implements ResponseTransformer{

        //Object -> String【把Java对对象转化成字符串】
        private Gson gson=new Gson();
        @Override
        public String render(Object o) throws Exception {
            return gson.toJson(o);
        }
    }
}
