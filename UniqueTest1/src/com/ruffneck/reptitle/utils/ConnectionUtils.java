package com.ruffneck.reptitle.utils;

import com.ruffneck.reptitle.exception.ResponseCodeException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionUtils {


    public static InputStream connect4InputStream(String path) throws IOException {

        String html = null;
        URL url = new URL(path);


        HttpURLConnection conn;
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setReadTimeout(5000);
        conn.setConnectTimeout(5000);

        //如果不写请求头,会出现403拒绝访问.
        //因为服务器的安全设置不接受Java程序作为客户端访问，解决方案是设置客户端的User Agent
//        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.93 Safari/537.36");

        conn.connect();

        if (conn.getResponseCode() == 200) {
            InputStream is = conn.getInputStream();
            return is;
        } else {
            System.out.println("conn.getResponseCode() = " + conn.getResponseCode());
            return null;
        }



    }

    /**
     * 访问一个网站,获取该网站的html信息.(编码格式是utf-8).
     *
     * @param path 访问的String uri.
     * @return 该网站的html信息
     * @throws IOException
     */
    public static String connect4String(String path) throws IOException, ResponseCodeException {
        return connect4String(path, 1);
    }

    /**
     * 访问一个网站,获取该网站的html信息.(编码格式是utf-8).
     *
     * @param path 访问的String uri.
     * @param page 访问的页数
     * @return 该网站的html信息
     * @throws IOException
     */
    public static String connect4String(String path, int page) throws IOException, ResponseCodeException {

        String html = null;
        URL url = new URL(path + "?&page=" + page);


        HttpURLConnection conn;
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setReadTimeout(5000);
        conn.setConnectTimeout(5000);

        //如果不写请求头,会出现403拒绝访问.
        //因为服务器的安全设置不接受Java程序作为客户端访问，解决方案是设置客户端的User Agent
//        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.93 Safari/537.36");

        conn.connect();

        if (conn.getResponseCode() == 200) {
            InputStream is = conn.getInputStream();
            html = StreamUtils.Stream2String(is, "utf-8");
            /*FileOutputStream fos = new FileOutputStream(new File("hehe.txt"));
            byte[] b = new byte[1024];
            int len = 0;
            while((len = (is.read(b)))!= -1){
                fos.write(b,0,len);
            }
            fos.close();*/
            is.close();
        } else {
//            System.out.println("conn.getResponseCode() = " + conn.getResponseCode());
            throw new ResponseCodeException(""+conn.getResponseCode());
        }

        conn.disconnect();

        return html;
    }
    /**
     * 访问一个网站,保存内容到文件.
     */
    public static File connects4File(String path, File file) throws IOException {
        if(file.exists())return file;

        String html = null;
        URL url = new URL(path);

        HttpURLConnection conn;
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setReadTimeout(5000);
        conn.setConnectTimeout(5000);

        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.93 Safari/537.36");

        conn.connect();

        if (conn.getResponseCode() == 200) {
            InputStream is = conn.getInputStream();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int len;
            while((len =is.read(b))!= -1){
                fos.write(b,0,len);
            }
            is.close();
            fos.close();
        } else {
            System.out.println("conn.getResponseCode() = " + conn.getResponseCode());
        }

        conn.disconnect();
        return file;
    }


    /**
     * 之后可以写成迭代的方式去访问.(而不是写死访问的元素.)
     */
    public static ArrayList<HashMap<String, String>> getContentList(String html) {
        Document document = Jsoup.parse(html);

        //Jsoup:用正则表达式去匹配标签
        Elements blog_list = document.select("#wrap div.main_center div.blog_list");
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        HashMap<String, String> map;

        for (Element element : blog_list) {
            map = new HashMap<>();
            //通过匹配name元素来获取link标签.(从href属性中获取博文链接)
//            System.out.println("element.select(\"a[href]\") = " + element.select("a[name]"));
            Element title = element.select("a[name]").first();
            map.put("title", HTMLUtils.getSaveFormat(title.text()));
            map.put("href", HTMLUtils.getSaveFormat(title.attr("href")));

            //获取作者类型
            Element title_authorType = element.select("h1 img[title]").first();
            if (title_authorType != null) {
                map.put("authorType", HTMLUtils.getSaveFormat(title_authorType.attr("title")));
            } else {
                map.put("authorType", HTMLUtils.getSaveFormat(("普通用户")));
            }
            list.add(map);

            //作者的图片图标
            Element author_icon = element.select("dl a img[src]").first();
            map.put("author_icon", HTMLUtils.getSaveFormat(author_icon.attr("src")));

            //文章简略内容
            Element content = element.select("dl dd").first();
            map.put("content", HTMLUtils.getSaveFormat(content.text()));

            //用户名
            Element user_name = element.select("div.about_info a.user_name").first();
            map.put("userName", HTMLUtils.getSaveFormat(user_name.text()));

        }

        return list;
    }



}
