package com.myself.CC.config;

import lombok.Data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Data
public class ConfigProperties {
    private String crawlerBase1;
    private String crawlerPath1;
    private String crawlerBase2;
    private String crawlerPath2;

    private boolean crawlerDetail;

    private String dbUsername;
    private String dbPassword;
    private String dbUrl;
    private String dbDriverClass;

    private boolean enableConsole;

    public ConfigProperties() {
        //从外部文件加载
        InputStream inputStream = ConfigProperties.class.getClassLoader().getResourceAsStream("config.properties");
        Properties p=new Properties();
        try {
            p.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.crawlerBase1= String.valueOf(p.get("crawler.base1"));
        this.crawlerPath1= String.valueOf(p.get("crawler.path1"));
        this.crawlerDetail =Boolean.valueOf(String.valueOf(p.get("crawler.detail")));

        this.crawlerBase2= String.valueOf(p.get("crawler.base2"));
        this.crawlerPath2= String.valueOf(p.get("crawler.path2"));
        this.crawlerDetail =Boolean.valueOf(String.valueOf(p.get("crawler.detail")));

        this.dbUsername= String.valueOf(p.get("db.username"));
        this.dbPassword= String.valueOf(p.get("db.password"));
        this.dbDriverClass= String.valueOf(p.get("db.driverClass"));
        this.dbUrl= String.valueOf(p.get("db.url"));

        this.enableConsole=Boolean.valueOf(String.valueOf(p.getProperty("config.enable_console","false")));
    }

}
