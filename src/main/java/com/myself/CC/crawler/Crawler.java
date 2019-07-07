package com.myself.CC.crawler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.myself.CC.crawler.common.Page;
import com.myself.CC.crawler.parse.Parse;
import com.myself.CC.crawler.pipeline.Pipeline;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class Crawler {
    //放置文档页面（超链接）
    //放置详情页面（数据）
    //未被采集和解析的页面
    //page htmlPage dataSet
    private Queue<Page> todoQueue = new LinkedBlockingDeque<>();

    //放置详情页面（处理完成，数据在dataSet）
    private Queue<Page> doneQueue = new LinkedBlockingDeque<>();

    //采集器
    private WebClient webClient;

    //所有解析器
    private List<Parse> parseList = new LinkedList<>();

    //所有清洗器（管道）
    private List<Pipeline> pipelineList = new LinkedList<>();

    //线程调度器
    private ExecutorService executorService;


    public Crawler() {
        this.webClient = new WebClient(BrowserVersion.CHROME);
        this.webClient.getOptions().setJavaScriptEnabled(false);
        this.webClient.getOptions().setCssEnabled(false);
        this.executorService = Executors.newFixedThreadPool(8, new ThreadFactory() {
            private final AtomicInteger id = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("Crawler-Thread-" + id.getAndIncrement());
                return thread;
            }
        });
    }

    public void start() {
        //采集
        //解析
        this.executorService.submit(new Runnable() {
            @Override
            public void run() {
                parse();
            }
        });
        //清洗
        this.executorService.submit(new Runnable() {
            @Override
            public void run() {
                pipeline();
            }
        });
    }

    private void parse() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final Page page = todoQueue.poll();
            if (page == null) {
                continue;
            }
            this.executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        //采集（爬取）后此时有base path detail htmlPage
                        HtmlPage htmlPage = Crawler.this.webClient.getPage(page.getUrl());
                        page.setHtmlPage(htmlPage);

                        //解析 后此时有base path detail htmlPage subPage等
                        for (Parse p : Crawler.this.parseList) {
                            p.parse(page);
                        }
                        if (page.isDetail()) {
                            //已经被采集和解析完毕的page  此时有base path detail htmlPage dataSet  subPage为空
                            Crawler.this.doneQueue.add(page);
                        } else {
                            Iterator<Page> iterator = page.getSubPage().iterator();
                            while (iterator.hasNext()) {
                                Page subPage = iterator.next();
                                //由于取出的子页面没有被采集，所以还需要将子页面放入docQueue中继续被采集解析
                                Crawler.this.todoQueue.add(subPage);
                                iterator.remove(); //将文档页队列中的元素依次删除【已经被解析了】
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void pipeline() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final Page page = this.doneQueue.poll();
            if (page == null) {
                continue;
            }
            this.executorService.submit(new Runnable() {
                @Override
                public void run() {
                    for (Pipeline p : Crawler.this.pipelineList) {
                        p.pipeline(page);
                    }
                }
            });
        }
    }

    public void addPage(Page page){
        this.todoQueue.add(page);
    }

    public void addParse(Parse parse){
        this.parseList.add(parse);
    }

    public void addPipeline(Pipeline pipeline){
        this.pipelineList.add(pipeline);
    }

    public void stop() {
        if (this.executorService != null && !this.executorService.isShutdown()) {
            this.executorService.shutdown();
        }
    }

}
