package com.ruffneck.reptitle.monitor;

import com.ruffneck.reptitle.info.ExecutorInfo;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by �𽣷�˵ on 2015/10/29.
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

                sb.append("[首页]");
                if (homePageExecutor.isTerminated()) {
                    sb.append("解析完毕\n");
                    articleExecutor.shutdown();
                } else {
                    sb.append("解析中.......");
                    sb.append("(" + homePageExecutor.getCompletedTaskCount() + "/" + homePageExecutor.getTaskCount() + ")");
                    sb.append("\n");
//                    sb.append("[��ҳ��Ϣ]");
//                    sb.append("��ǰ����:");
//                    sb.append(info.getHomePageInfo());
/*                    for (int i = 0; i < homePageExecutor.getMaximumPoolSize(); i++) {
                        String detailInfo = info.getHomePageInfo(i);
                        if (!StringUtils.isEmpty(detailInfo)) {
                            sb.append("|--");
                            sb.append(detailInfo);
                            sb.append("\n");
                        }
                    }*/
                }
//                sb.append("\n");

                sb.append("[文章]");
                if (articleExecutor.isTerminated()) {
                    sb.append("解析完毕\n");
                    pictureExecutor.shutdown();
                } else {
                    sb.append("解析中.......");
                    sb.append("(" + articleExecutor.getCompletedTaskCount() + "/" + articleExecutor.getTaskCount() + ")");
                    sb.append("\n");
/*                    for (int i = 0; i < articleExecutor.getMaximumPoolSize(); i++) {
                        String detailInfo = info.getArticleInfo(i);
                        if (!StringUtils.isEmpty(detailInfo)) {
                            sb.append("|--");
                            sb.append(detailInfo);
                            sb.append("\n");
                        }
                    }*/

                }
//                sb.append("\n");

                sb.append("[图片]");
                if (pictureExecutor.isTerminated()) sb.append("解析完毕\n");
                else {
                    sb.append("解析中.......");
                    sb.append("(" + pictureExecutor.getCompletedTaskCount() + "/" + pictureExecutor.getTaskCount() + ")");
                    sb.append("\n");
/*                    for (int i = 0; i < pictureExecutor.getMaximumPoolSize(); i++) {
                        String detailInfo = info.getPictureInfo(i);
                        if (!StringUtils.isEmpty(detailInfo)) {
                            sb.append("|--");
                            sb.append(detailInfo);
                            sb.append("\n");
                        }
                    }*/


                    if (homePageExecutor.isTerminated() && articleExecutor.isTerminated() && pictureExecutor.isTerminated()) {
                        System.out.println("***********任务完成了!*************");
                        shutdown();
                    } else {
                        System.out.println(sb.toString());
                    }

                    Thread.sleep(delay);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
