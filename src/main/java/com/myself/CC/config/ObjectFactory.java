package com.myself.CC.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.myself.CC.analyze.dao.MessageDao;
import com.myself.CC.analyze.dao.impl.MessageDaoImpl;
import com.myself.CC.analyze.service.AnalyzeService;
import com.myself.CC.analyze.service.impl.AnalyzeServiceImpl;
import com.myself.CC.crawler.Crawler;
import com.myself.CC.crawler.common.Page;
import com.myself.CC.crawler.parse.impl.DataPageParse;
import com.myself.CC.crawler.parse.impl.DocumentParse;
import com.myself.CC.crawler.pipeline.impl.ConsolePipeline;
import com.myself.CC.crawler.pipeline.impl.DatabasePipeline;
import com.myself.CC.web.WebController;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class ObjectFactory {
    private static final ObjectFactory instance = new ObjectFactory();

    //存放所有对象
    private final Map<Class, Object> objectMap = new HashMap<>();

    private ObjectFactory() {
        //1.初始化配置类
        initConfigProperties();
        //2.初始化数据源
        initDataSource();
        //3.初始化爬虫类
        initCrawler();
        //4.初始化Web类
        initWebController();
        //5.打印对象列表
        printObjectList();
    }

    private void initWebController() {
        DataSource dataSource = getObject(DataSource.class);
        MessageDao messageDao = new MessageDaoImpl(dataSource);
        AnalyzeService analyzeService = new AnalyzeServiceImpl(messageDao);
        WebController webController = new WebController(analyzeService);
        objectMap.put(WebController.class, webController);
    }

    private void printObjectList() {
        System.out.println("==============ObjectFactory List==================");
        for (Map.Entry<Class, Object> entry : objectMap.entrySet()) {
            System.out.println(String.format("\t[%s] -> [%s]", entry.getKey().getCanonicalName(), entry.getValue().getClass().getCanonicalName()));
        }
        System.out.println("==================================================");
    }

    private void initDataSource() {
        ConfigProperties configProperties = getObject(ConfigProperties.class);
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(configProperties.getDbUsername());
        dataSource.setPassword(configProperties.getDbPassword());
        dataSource.setDriverClassName(configProperties.getDbDriverClass());
        dataSource.setUrl(configProperties.getDbUrl());
        objectMap.put(DataSource.class, dataSource);
    }

    private void initCrawler() {
        ConfigProperties configProperties = getObject(ConfigProperties.class);
        DruidDataSource dataSource = getObject(DataSource.class);
        final Page page1 = new Page(configProperties.getCrawlerBase1(), configProperties.getCrawlerPath1(), configProperties.isCrawlerDetail());
        final Page page2 = new Page(configProperties.getCrawlerBase2(), configProperties.getCrawlerPath2(), configProperties.isCrawlerDetail());

        Crawler crawler = new Crawler();
        crawler.addPage(page1);
        crawler.addPage(page2);
        crawler.addParse(new DataPageParse());
        crawler.addParse(new DocumentParse());
        if (configProperties.isEnableConsole()) {
            crawler.addPipeline(new ConsolePipeline());
        }
        crawler.addPipeline(new DatabasePipeline(dataSource));
        objectMap.put(Crawler.class, crawler);
    }

    private void initConfigProperties() {
        ConfigProperties configProperties = new ConfigProperties();
        objectMap.put(ConfigProperties.class, configProperties);
    }

    public static ObjectFactory getInstance() {
        return instance;
    }

    public <T> T getObject(Class classz) {
        return (T) objectMap.get(classz);
    }
}
