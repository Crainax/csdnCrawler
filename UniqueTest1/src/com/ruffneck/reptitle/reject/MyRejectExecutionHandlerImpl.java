package com.ruffneck.reptitle.reject;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by ·ð½£·ÖËµ on 2015/10/28.
 */
public class MyRejectExecutionHandlerImpl implements RejectedExecutionHandler{
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
    }
}
