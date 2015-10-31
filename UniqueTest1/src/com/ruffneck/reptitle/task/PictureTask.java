package com.ruffneck.reptitle.task;

import com.ruffneck.reptitle.exception.ResponseCodeException;
import com.ruffneck.reptitle.utils.ConnectionUtils;
import com.ruffneck.reptitle.utils.StringUtils;

import java.io.File;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class PictureTask extends Task {

    private String url;
    private File file;

    public PictureTask(String url, File file) {
        this.url = url;
        this.file = file;
    }

    @Override
    public void run() {
        for (int i = 0; i < times; i++) {
            {
                try {
                    setInfo("下载中:"+url);
                    file = ConnectionUtils.connects4File(url, this.file);
                    String suffix = StringUtils.getSuffix(url, file);
                    File f2 = new File(file.getAbsolutePath().replaceAll("\\.[a-zA-Z]{3,4}$", suffix));
                    boolean b = file.renameTo(f2);
//                    setInfo("下载成功:" + file.getName());
                    setInfo("下载成功");
                    break;
                } catch (SocketTimeoutException e) {
                    setInfo("下载失败:" + file.getName() + ",超时重连中.....");
                } catch (ResponseCodeException e) {
                    setInfo("下载失败:" + file.getName() + ",网页出现错误.....");
                } catch (UnknownHostException e) {
                    setInfo("下载失败:" + file.getName() + ",连接错误.....");
                } catch (Exception e) {
                    setInfo("下载失败:" + file.getName() + ",未知错误.....");
                }
            }
        }
    }
}
