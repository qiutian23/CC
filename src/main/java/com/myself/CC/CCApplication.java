package com.myself.CC;

import com.myself.CC.config.ObjectFactory;
import com.myself.CC.crawler.Crawler;
import com.myself.CC.web.WebController;

public class CCApplication {
    public static void main(String[] args) {
        ObjectFactory objectFactory = ObjectFactory.getInstance();
        WebController webController = objectFactory.getObject(WebController.class);
        webController.launch();
        Crawler crawler = objectFactory.getObject(Crawler.class);
        crawler.start();
    }
}
