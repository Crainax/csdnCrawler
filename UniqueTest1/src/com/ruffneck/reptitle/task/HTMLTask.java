package com.ruffneck.reptitle.task;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 佛剑分说 on 2015/10/31.
 */
public abstract class HTMLTask extends Task{

    protected int parseWay;
    protected boolean isDatas;
    protected ArrayList<HashMap<String, String>> list;

    public static final int REGEX = 1;
    public static final int JSOUP = 2;

    public HTMLTask(int parseWay, boolean isDatas) {
        this.parseWay = parseWay;
        this.isDatas = isDatas;
    }

    public ArrayList<HashMap<String, String>> getList() {
        return list;
    }
}
