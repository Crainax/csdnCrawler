package com.ruffneck.reptitle.info;

import com.ruffneck.reptitle.task.Task;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by �𽣷�˵ on 2015/10/29.
 */
public class ExecutorInfo {

    Task[] homePageInfo;
    Task[] articleInfo;
    Task[] pictureInfo;

    public ExecutorInfo(ThreadPoolExecutor homePageExecutor, ThreadPoolExecutor articleExecutor, ThreadPoolExecutor pictureExectuor) {
        homePageInfo = new Task[homePageExecutor.getMaximumPoolSize()];
        articleInfo = new Task[articleExecutor.getMaximumPoolSize()];
        pictureInfo = new Task[pictureExectuor.getMaximumPoolSize()];
    }

    public void addHomePageInfo(Task task) {
        for (int i = 0; i < homePageInfo.length; i++) {
            if (homePageInfo[i] == null || homePageInfo[i].getInfo().equals("")) {
                homePageInfo[i] = task;
//                homePageInfo[i].setInfo("初始化中....");
                break;
            }
        }
    }

    public void addArticleInfo(Task task) {
        for (int i = 0; i < articleInfo.length; i++) {
            if (articleInfo[i] == null ||articleInfo[i].getInfo().equals("")){
                articleInfo[i] = task;
//                articleInfo[i].setInfo("初始化中....");................
                break;
            }
        }
    }

    public void addPictureInfo(Task task) {
        for (int i = 0; i < pictureInfo.length; i++) {
            if (pictureInfo[i] == null || pictureInfo[i].getInfo().equals("下载成功")){
                pictureInfo[i] = task;
                break;
            }
        }
    }

    public String getHomePageInfo(int TaskIndex) {
//        System.out.println(Arrays.toString(homePageInfo));
        if(homePageInfo[TaskIndex] == null) return null;
        return homePageInfo[TaskIndex].getInfo();
    }

    public String getArticleInfo(int TaskIndex) {
        if(articleInfo[TaskIndex] == null) return null;
        return articleInfo[TaskIndex].getInfo();
    }

    public String getPictureInfo(int TaskIndex) {
        if(pictureInfo[TaskIndex] == null) return null;
        return pictureInfo[TaskIndex].getInfo();
    }


}
