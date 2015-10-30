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

                sb.append("[��ҳ��Ϣ]");
                if (homePageExecutor.isTerminated()) sb.append("�������.");
                else {
                    sb.append("������....");
                    sb.append("(" + homePageExecutor.getActiveCount() + "/" + homePageExecutor.getTaskCount() + ")");
                    sb.append("\n");
                    sb.append("[��ҳ��Ϣ]");
                    sb.append("��ǰ����:");
                    sb.append(info.getHomePageInfo());
                }
                sb.append("\n");

                sb.append("[������Ϣ]");
                if (articleExecutor.isTerminated()) sb.append("�������.");
                else {
                    sb.append("������....");
                    sb.append("(" + articleExecutor.getActiveCount() + "/" + articleExecutor.getTaskCount() + ")");
                    sb.append("\n");
                    sb.append("[������Ϣ]");
                    sb.append("��ǰ����:");
                    sb.append(info.getArticleInfo());
                }
                sb.append("\n");

                sb.append("[ͼƬ��Ϣ]");
                if (pictureExecutor.isTerminated()) sb.append("�������.");
                else {
                    sb.append("������....");
                    sb.append("(" + pictureExecutor.getActiveCount() + "/" + pictureExecutor.getTaskCount() + ")");
                    sb.append("\n");
                    sb.append("[������Ϣ]");
                    sb.append("��ǰ����:");
                    sb.append(info.getPictureInfo());
                }


                //��������������,�����ģ����
                if(homePageExecutor.isTerminated() && articleExecutor.isTerminated() && pictureExecutor.isTerminated()){
                    System.out.println("***********�������������!*************");
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
