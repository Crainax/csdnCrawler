package com.ruffneck.reptitle.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by �𽣷�˵ on 2015/10/24.
 */
public class StringUtils {

    public static boolean isEmpty(String string) {
        if (string == null) return true;
        if (string.length() < 1) return true;

        return false;
    }

    /**
     * 用于判断一个地址下载下来的图片的后缀名.
     * @param url 图片下载的地址.
     * @param file 图片下载的位置.
     * @return
     * @throws IOException
     */
    public static String getSuffix(String url, File file) throws IOException {
        int index = url.lastIndexOf("/");
        String urlResult = url.substring(index + 1);

        Matcher matcher = Pattern.compile("^.*?(\\.[a-zA-Z]{3,4})$").matcher(urlResult);
        if (matcher.find()) {
            return matcher.group(1);
        }

        FileInputStream fis = new FileInputStream(file);
        byte[] src = new byte[2];
        fis.read(src);

        fis.close();
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return ".jpg";
        }
        for (byte aSrc : src) {
            int v = aSrc & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        //通过魔术数字来返回类型,这里只写4种类型,更多的看这里.http://blog.csdn.net/fenglibing/article/details/7733496
        String magicNumber =stringBuilder.toString();
        switch (magicNumber) {
            case "424d":
                return ".bmp";
            case "ffd8":
                return ".jpg";
            case "4749":
                return ".gif";
            case "8950":
                return ".png";
        }
        return ".jpg";
    }

}
