package com.ruffneck.reptitle.task;

import com.ruffneck.reptitle.exception.ResponseCodeException;
import com.ruffneck.reptitle.info.ExecutorInfo;
import com.ruffneck.reptitle.utils.*;

import java.io.File;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArticleTask extends HTMLTask {

    private String url;
    private int splitWay;
    private ThreadPoolExecutor pictureExecutor;
    private ExecutorInfo info;
    public static final int SPLIT_TAG = BlogConnectionUtils.SPLIT_WAY_TAG;
    public static final int SPLIT_WAY_PAGRM = BlogConnectionUtils.SPLIT_WAY_PAGRM;

    public ArticleTask(int parseWay, boolean isDatas, String url, int splitWay,
                       ThreadPoolExecutor pictureExecutor, ExecutorInfo info) {
        super(parseWay, isDatas);
        this.url = url;
        this.splitWay = splitWay;
        this.pictureExecutor = pictureExecutor;
        this.info = info;
    }

    public void setUrl() {
        setInfo("解析中:" + url);
    }

    @Override
    public void run() {
        for (int i = 0; i < times; i++) {
            {
                String html;
                try {
                    setInfo("解析中:" + url);
//                    System.out.println("解析开始:" + url);
                    html = ConnectionUtils.connect4String(url);
                    ArrayList<HashMap<String, String>> list = new ArrayList<>();

                    File dir = null;
                    String name = null;
                    if (parseWay == JSOUP) {
                        list = BlogConnectionUtils.getBlogContent(url, splitWay);
                        name = list.get(0).get("articleTitle").replaceAll("[^\\w\\u4e00-\\u9fa5]", "");
                        dir = new File("articleByJsoup\\" + name);
                    } else if (parseWay == REGEX) {
                        list = RegexUtils.parseArticlePage(html);
                        name = list.get(0).get("articleTitle").replaceAll("[^\\w\\u4e00-\\u9fa5]", "");
                        dir = new File("articleByRegex\\" + name);
                    }


                    File file;
                    if (isDatas) {
                        file = new File(dir, name + ".xml");
                        if (!dir.exists()) dir.mkdirs();
                        StreamUtils.writeData(list, file);
                    } else {
                        file = new File(dir, name + ".txt");
                        if (!dir.exists()) dir.mkdirs();
                        StreamUtils.writeMap(list, file);
                    }

                    if (parseWay == REGEX) {
                        for (HashMap<String, String> map : list) {
                            for (Map.Entry<String, String> entry : map.entrySet()) {
                                String imageNum = entry.getKey();
                                String imageURL = entry.getValue();
                                Matcher matcher = Pattern.compile("image(\\d+)").matcher(imageNum);
                                if (matcher.find()) {
                                    String imageName = matcher.group(1);
                                    File image = new File(dir, imageName + ".jpg");
                                    PictureTask pictureTask = new PictureTask(imageURL, image);
                                    info.addPictureInfo(pictureTask);
                                    pictureExecutor.execute(pictureTask);
                                }

                            }
                        }
                    } else if (parseWay == JSOUP) {
                        for (HashMap<String, String> map : list) {
                            for (Map.Entry<String, String> entry : map.entrySet()) {
                                String imageNum = entry.getKey();
                                String imageURL = entry.getValue();
                                Matcher matcher = Pattern.compile("content(\\d+)_img(\\d+)").matcher(imageNum);
                                if (matcher.find()) {
                                    String imageName = matcher.group(1) + "_" + matcher.group(2);
                                    File image = new File(dir, imageName + ".jpg");
                                    PictureTask pictureTask = new PictureTask(imageURL, image);
                                    info.addPictureInfo(pictureTask);
                                    pictureExecutor.execute(pictureTask);
                                }
                            }

                        }
                    }
                    String titleName = list.get(0).get("articleTitle");
//                    setInfo("解析成功:"+titleName);
                    setInfo("");
                    break;
                } catch (SocketTimeoutException e) {
                    setInfo("解析失败:超时重连中." + url);
                } catch (ResponseCodeException e) {
                    setInfo("解析失败:网页出现错误,重连中." + url);
                } catch (UnknownHostException e) {
                    setInfo("解析失败:连接错误,重连中." + url);
                } catch (Exception e) {
                    setInfo("解析失败:未知错误,重连中." + url);
                }

                if (i == times - 1)
                    setInfo("解析失败:" + url);
            }
        }
    }
}
