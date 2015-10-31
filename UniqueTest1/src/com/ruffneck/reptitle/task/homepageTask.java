package com.ruffneck.reptitle.task;

import com.ruffneck.reptitle.exception.ResponseCodeException;
import com.ruffneck.reptitle.utils.ConnectionUtils;
import com.ruffneck.reptitle.utils.RegexUtils;
import com.ruffneck.reptitle.utils.StreamUtils;

import java.io.File;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class HomePageTask extends HTMLTask {

    private int page;
    public static final String URL = "http://blog.csdn.net/mobile/index.html";

    public HomePageTask(int page, int parseWay, boolean isDatas) {
        super(parseWay,isDatas);
        this.page = page;
    }

    @Override
    public void run() {
        for (int i = 0; i < times; i++) {
            {
                setInfo("解析中:第" + page + "页内容");
                String html = null;
                try {
                    html = ConnectionUtils.connect4String(URL, page);

                    File dir = null;
                    list = null;
                    //判断解析方式
                    if (parseWay == JSOUP) {
                        list = ConnectionUtils.getContentList(html);
                        dir = new File("homePageByJsoup");
                    } else if (parseWay == REGEX) {
                        list = RegexUtils.parseHomePage(html);
                        dir = new File("homePageByRegex");
                    }

                    File file;
                    if (isDatas) {
                        file = new File(dir, "page_" + page + ".xml");
                        if (!dir.exists()) dir.mkdirs();
                        StreamUtils.writeData(list, file);
                    } else {
                        file = new File(dir, "page_" + page + ".txt");
                        if (!dir.exists()) dir.mkdirs();
                        StreamUtils.writeMap(list, file);
                    }
//                    System.out.println("解析成功:第" + page + "页内容");
                    setInfo("");
                    break;
                } catch (SocketTimeoutException e) {
                    setInfo("解析失败:第" + page + "页内容,超时重连中.......");
                } catch (ResponseCodeException e) {
                    setInfo("解析失败:第" + page + "页内容,网页出现错误,重连中.......");
                } catch (UnknownHostException e) {
                    setInfo("解析失败:第" + page + "页内容,连接错误,重连中.......");
                } catch (Exception e) {
                    setInfo("解析失败:第" + page + "页内容,未知错误,重连中.....");
                }

                if (i == times - 1)
                    setInfo("解析结束:第" + page + "页内容.");
            }
        }
    }
}
