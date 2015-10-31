package com.ruffneck.reptitle.task;

import com.ruffneck.reptitle.info.ExecutorInfo;

import java.util.HashMap;
import java.util.concurrent.ThreadPoolExecutor;

public class AllArticleTask extends HomePageTask {

    private ThreadPoolExecutor articleExecutor;
    private ThreadPoolExecutor pictureExecutor;
    private ExecutorInfo info;

    private int splitWay;

    public AllArticleTask(int page, int parseWay, boolean isDatas, ThreadPoolExecutor articleExecutor,
                          ThreadPoolExecutor pictureExecutor, ExecutorInfo info, int splitway) {
        super(page, parseWay, isDatas);
        this.articleExecutor = articleExecutor;
        this.pictureExecutor = pictureExecutor;
        this.info = info;
        this.splitWay = splitway;
    }

    @Override
    public void run() {
        super.run();
        if(getList() != null){
            for (HashMap<String, String> map : list) {
                String href = map.get("href");
                ArticleTask articleTask = new ArticleTask(parseWay, isDatas, href, splitWay, pictureExecutor, info);
                info.addArticleInfo(articleTask);
                articleExecutor.execute(articleTask);
            }
        }
    }
}
