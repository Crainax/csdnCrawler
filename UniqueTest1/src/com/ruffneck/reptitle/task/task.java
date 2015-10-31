package com.ruffneck.reptitle.task;

import com.ruffneck.reptitle.info.ExecutorInfo;

public abstract class Task implements Runnable{

    ExecutorInfo executorInfo;

    public int times = 10;

    public void setTimes(int times) {
        this.times = times;
    }

    private String info = "";

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
