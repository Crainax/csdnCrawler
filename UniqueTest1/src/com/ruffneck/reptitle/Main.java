package com.ruffneck.reptitle;

import com.ruffneck.reptitle.exception.ResponseCodeException;
import com.ruffneck.reptitle.info.ExecutorInfo;
import com.ruffneck.reptitle.monitor.Monitor;
import com.ruffneck.reptitle.reject.MyRejectExecutionHandlerImpl;
import com.ruffneck.reptitle.task.AllArticleTask;
import com.ruffneck.reptitle.task.ArticleTask;
import com.ruffneck.reptitle.task.HTMLTask;
import com.ruffneck.reptitle.utils.BlogConnectionUtils;
import com.ruffneck.reptitle.utils.ConnectionUtils;
import com.ruffneck.reptitle.utils.RegexUtils;
import com.ruffneck.reptitle.utils.StreamUtils;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static final int REGEX = 1;
    public static final int JSOUP = 2;

    private static ThreadPoolExecutor homePageExecutor;
    private static ThreadPoolExecutor articleExecutor;
    private static ThreadPoolExecutor pictureExecutor;
    private static Monitor monitor;
    private static ExecutorInfo info;

    public static void main(String[] args) throws IOException, SAXException {
        initThreadPool();

//        String url = "http://blog.csdn.net/fansunion/article/details/49531089";
//        ArticleTask articleTask = new ArticleTask(HTMLTask.JSOUP,false,url,ArticleTask.SPLIT_WAY_PAGRM);
//        articleExecutor.execute(articleTask);
//
        for (int i = 1; i < 6; i++) {
        AllArticleTask allArticleTask = new AllArticleTask(i, HTMLTask.REGEX, false, articleExecutor, pictureExecutor, info, ArticleTask.SPLIT_TAG);
            info.addHomePageInfo(allArticleTask);
            homePageExecutor.execute(allArticleTask);
        }

        homePageExecutor.shutdown();
        //articleExecutor.shutdown();
        //pictureExecutor.shutdown();

        new Thread(monitor).start();
    }

    private static void initThreadPool() {
        MyRejectExecutionHandlerImpl rejectedHandler = new MyRejectExecutionHandlerImpl();
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        homePageExecutor = new ThreadPoolExecutor(2, 3, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100),
                threadFactory, rejectedHandler);
        articleExecutor = new ThreadPoolExecutor(2, 3, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100),
                threadFactory, rejectedHandler);
        pictureExecutor = new ThreadPoolExecutor(2, 10, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1000),
                threadFactory, rejectedHandler);
        info = new ExecutorInfo(homePageExecutor, articleExecutor, pictureExecutor);
        monitor = new Monitor(homePageExecutor, articleExecutor, pictureExecutor, 500, info);
    }

    /*
    public static void getHomePageDatasByJsoup(int page) throws IOException {
        String html = ConnectionUtils.connect4String("http://blog.csdn.net/mobile/newest.html", page);
        ArrayList<HashMap<String, String>> list = ConnectionUtils.getContentList(html);

        File dir = new File("homePageDatasByJsoup");
        File file = new File(dir, "page_" + page + ".xml");

        if (!dir.exists()) dir.mkdirs();
        StreamUtils.writeData(list, file);

    }*/

    public static void getHomePage(int page, int parseWay, boolean isDatas) throws IOException, ResponseCodeException {
        String html = ConnectionUtils.connect4String("http://blog.csdn.net/mobile/newest.html", page);

        File dir = null;
        ArrayList<HashMap<String, String>> list = null;
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

    }

    /*public static void getHomePageDatasByRegex(int page, int parseWay) throws IOException {
        String html = ConnectionUtils.connect4String("http://blog.csdn.net/mobile/newest.html", page);
        ArrayList<HashMap<String, String>> list = RegexUtils.parseHomePage(html);

        File dir = new File("homePageDatasByRegex");
        File file = new File(dir, "page_" + page + ".xml");

        if (!dir.exists()) dir.mkdirs();
        StreamUtils.writeData(list, file);


    }*/

    public static void getArticle(String path, int parseWay, boolean isDatas) throws IOException {
//        Runnable r = new Runnable() {
//            @Override
//            public void run() {
        System.out.println("parsing article:" + path);
        String html = ConnectionUtils.connect4String(path);
        ArrayList<HashMap<String, String>> articleList = new ArrayList<>();

        File dir = null;
        String name = null;
        if (parseWay == JSOUP) {
            articleList = BlogConnectionUtils.getBlogContent(path, BlogConnectionUtils.SPLIT_WAY_TAG);
            name = articleList.get(0).get("article_title").replaceAll("[^\\w\\u4e00-\\u9fa5]", "");
            dir = new File("articleByJsoup\\" + name);
        } else if (parseWay == REGEX) {
            articleList = RegexUtils.parseArticlePage(html);
            name = articleList.get(0).get("articleTitle").replaceAll("[^\\w\\u4e00-\\u9fa5]", "");
            dir = new File("articleByRegex\\" + name);
        }


        File file;
        if (isDatas) {
            file = new File(dir, name + ".xml");
            if (!dir.exists()) dir.mkdirs();
            StreamUtils.writeData(articleList, file);
        } else {
            file = new File(dir, name + ".txt");
            if (!dir.exists()) dir.mkdirs();
            StreamUtils.writeMap(articleList, file);
        }

        if (parseWay == REGEX) {
            for (HashMap<String, String> map : articleList) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String imageNum = entry.getKey();
                    String imageURL = entry.getValue();
                    Matcher matcher = Pattern.compile("image(\\d+)").matcher(imageNum);
                    if (matcher.find()) {
                        String imageName = matcher.group(1);
                        File image = new File(dir, imageName + ".jpg");
                        ConnectionUtils.connects4File(imageURL, image);
                    }

                }
            }
        } else if (parseWay == JSOUP) {
            for (HashMap<String, String> map : articleList) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String imageNum = entry.getKey();
                    String imageURL = entry.getValue();
                    Matcher matcher = Pattern.compile("content(\\d+)_img(\\d+)").matcher(imageNum);
                    if (matcher.find()) {
                        String imageName = matcher.group(1) + "_" + matcher.group(2);
                        File image = new File(dir, imageName + ".jpg");
//                                    Runnable r2 = new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            try {
                        ConnectionUtils.connects4File(imageURL, image);
//                                            } catch (IOException e) {
//                                                System.out.println("downloading image:fail!");
//                                            }
//                                        }
                    }
//                                    pictureExecutor.execute(r2);
                }

            }
        }
//                } catch (SocketException e){
//                    System.out.println("parsing article:fail!");
//                } catch (Exception e){
//                    e.printStackTrace();
//                }
//
//            }
//        };
//
//        articleExecutor.execute(r);
    }

    /*
    public static void getArticleDataByJsoup(String path, int parseWay, boolean isDatas) throws IOException {
        String html = ConnectionUtils.connect4String(path);
        File file = new File(dir, name + ".xml");

        if (!dir.exists()) dir.mkdirs();
        StreamUtils.writeData(articleList, file);
    }*/

    public static void getAllArticleFromPage(int page, int parseWay, boolean isDatas) throws IOException {

        String html = ConnectionUtils.connect4String("http://blog.csdn.net/mobile/newest.html", page);
        ArrayList<HashMap<String, String>> list = null;
        if (parseWay == JSOUP) {
            list = BlogConnectionUtils.getContentList(html);
        } else if (parseWay == REGEX) {
            list = RegexUtils.parseHomePage(html);
        }

        for (HashMap<String, String> map : list) {
            String href = map.get("href");
//            System.out.println("Jsoup:href = " + href);
            getArticle(href, parseWay, isDatas);
        }

    }

/*    public static void getAllArticleFromPageByRegex(int page) throws IOException {

        String html = ConnectionUtils.connect4String("http://blog.csdn.net/mobile/newest.html", page);
        ArrayList<HashMap<String, String>>

        for (HashMap<String, String> map : list) {
            String href = map.get("href");
            System.out.println("Regex:href = " + href);
            getArticleDataByRegex(href);

        }

    }
*/

}
