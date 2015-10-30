package com.ruffneck.reptitle.monitor;

import com.ruffneck.reptitle.info.ExecutorInfo;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by 佛剑分说 on 2015/10/29.
 */
public class Monitor implements Runnable {

    private ThreadPoolExecutor homePageExecutor;
    private ThreadPoolExecutor articleExecutor;
    private ThreadPoolExecutor pictureExecutor;
    private int delay;
    private boolean run = true;
    private ExecutorInfo info;

    public Monitor(ThreadPoolExecutor homePageExecutor, ThreadPoolExecutor articleExecutor,
                   ThreadPoolExecutor pictureExecutor, int delay, ExecutorInfo info) {
        this.homePageExecutor = homePageExecutor;
        this.articleExecutor = articleExecutor;
        this.pictureExecutor = pictureExecutor;
        this.delay = delay;
        this.info = info;
    }

    public void shutdown() {
        run = false;
    }

    @Override
    public void run() {
        while (run) {
            StringBuilder sb = new StringBuilder();
            try {

                sb.append("[首页信息]");
                if (homePageExecutor.isTerminated()) sb.append("解析完毕.");
                else {
                    sb.append("解析中....");
                    sb.append("(" + homePageExecutor.getActiveCount() + "/" + homePageExecutor.getTaskCount() + ")");
                    sb.append("\n");
                    sb.append("[首页信息]");
                    sb.append("当前进度:");
                    sb.append(info.getHomePageInfo());
                }
                sb.append("\n");

                sb.append("[文章信息]");
                if (articleExecutor.isTerminated()) sb.append("解析完毕.");
                else {
                    sb.append("解析中....");
                    sb.append("(" + articleExecutor.getActiveCount() + "/" + articleExecutor.getTaskCount() + ")");
                    sb.append("\n");
                    sb.append("[文章信息]");
                    sb.append("当前进度:");
                    sb.append(info.getArticleInfo());
                }
                sb.append("\n");

                sb.append("[图片信息]");
                if (pictureExecutor.isTerminated()) sb.append("解析完毕.");
                else {
                    sb.append("解析中....");
                    sb.append("(" + pictureExecutor.getActiveCount() + "/" + pictureExecutor.getTaskCount() + ")");
                    sb.append("\n");
                    sb.append("[文章信息]");
                    sb.append("当前进度:");
                    sb.append(info.getPictureInfo());
                }


                //如果都解析完毕了,就完毕模拟器
                if(homePageExecutor.isTerminated() && articleExecutor.isTerminated() && pictureExecutor.isTerminated()){
                    System.out.println("***********所有任务都完成啦!*************");
                }else{
                    System.out.println(sb.toString());
                }

                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
