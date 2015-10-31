package com.ruffneck.reptitle.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by 佛剑分说 on 2015/10/23.
 */
public class BlogConnectionUtils extends ConnectionUtils {

    /**
     * 以标签的名字去创建键,然后去存
     */
    public static final int SPLIT_WAY_TAG = 0;
    /**
     * 以段落来分割,然后去存.
     */
    public static final int SPLIT_WAY_PAGRM = 1;

/*    public static LinkedHashMap<String, String> getBlogContent(String path) throws IOException, ParserConfigurationException, SAXException {
        String html = connect4String(path);
        SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
        saxParser.parse(html, new BlogHandler());
        return null;
    }*/

    public static ArrayList<HashMap<String, String>> getBlogContent(String path, int splitWay) throws IOException {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        LinkedHashMap<String, String> datas = new LinkedHashMap<>();

        String html = ConnectionUtils.connect4String(path);
        Document document = Jsoup.parse(html);
        Elements elements = document.select("#article_content");

        //用正则表达式去匹配所有换行符.
        Element element = HTMLUtils.replaceNewLine(elements.first());
        //获取所有内容

//        Elements blog_contents = element();
        Elements blog_contents = element.select("*");
        if (splitWay == SPLIT_WAY_PAGRM) {
            StringBuilder sb = new StringBuilder();
            Iterator<Element> elementIterator = blog_contents.iterator();
            int index = 0;
            while (elementIterator.hasNext()) {
                Element blog_content = elementIterator.next();
                String[] contents = blog_content.text().split(HTMLUtils.newLineSignal);
                if (contents.length > 1) {
                    for (int i = 0; i < contents.length-1; i++) {
                        sb.append(contents[i]);
                        datas.put("content"+index++,sb.toString());
                        sb.delete(0,sb.length());
                    }
                    sb.append(contents[contents.length-1]);
                }else {
                    sb.append(contents[0]);
                }

                Elements imgs = blog_content.select("img[src]");
                for (int j = 0; j < imgs.size(); j++) {
                    String src = imgs.get(j).attr("src");
                    datas.put("content" + index + "_img" + j, HTMLUtils.getSaveFormat(src));
                }
            }
            if(!StringUtils.isEmpty(sb.toString())){

                datas.put("content"+index,HTMLUtils.getSaveFormat(sb.toString()));
            }
        } else if (splitWay == SPLIT_WAY_TAG) {
            for (int i = 0; i < blog_contents.size(); i++) {
                //存取并过滤不需要的内容
                Element blog_content = blog_contents.get(i);
                String content = blog_content.text().replaceAll(HTMLUtils.newLineSignal, "\n");
                datas.put("content" + i, HTMLUtils.getSaveFormat(content));

                //存取图片(以content+i+#img+j)
                Elements imgs = blog_content.select("img[src]");
                for (int j = 0; j < imgs.size(); j++) {
                    String src = imgs.get(j).attr("src");
                    datas.put("content" + i + "_img" + j, HTMLUtils.getSaveFormat(src));
                }
            }
        }

        Element blogTitle = document.select("#blog_title > h2 > a").first();
        datas.put("blogTitle", HTMLUtils.getSaveFormat(blogTitle.text()));

        Element articleTitle = document.select("#article_details > div.article_title > h1 > span > a").first();
        datas.put("articleTitle", HTMLUtils.getSaveFormat(articleTitle.text()));

        //获取这个之后可以用SAXParser去解析行号内容
//        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("test.html"),"utf-8"));
//        FileOutputStream bw = new FileOutputStream("text.html");
//        bw.write(elements.toString().getBytes());
//        bw.close();
        //还是不行的
/*        for (Element element : elements) {
            //获取子元素
            Elements children = element.children();
            for (Element child : children) {
                recursion(child);
            }
        }*/
        list.add(datas);

        return list;
    }

    //递归算法失败了.
/*    public static void recursion(Element element) {

        System.out.println("element = " + element.toString());
        Elements children = element.children();
        if (children != null) {
            for (Element child : children) {
                recursion(child);
            }
        }
//        System.out.println("element = " + element.text());
    }*/


}
