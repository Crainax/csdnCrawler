package com.ruffneck.reptitle.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {


    /**
     * 爬取csdn文章中的特定内容
     *
     * @param html 需要进行处理的html文件
     * @return
     */
    public static ArrayList<HashMap<String, String>> parseArticlePage(String html) {
        ArrayList<HashMap<String, String>> articlePage = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();


        //获取博主的名字
        String authorName = HTMLUtils.getAuthorName(html);
        map.put("authorName", HTMLUtils.getSaveFormat(authorName));

        //获取博客的标题
        String blogTitle = HTMLUtils.getBlogTitle(html);
        map.put("blogTitle", HTMLUtils.getSaveFormat(blogTitle));

        //获取文章的标题
        String articleTitle = HTMLUtils.getArticleTitle(html);
        map.put("articleTitle", HTMLUtils.getSaveFormat(articleTitle));

        //文章内容的正则,这里可以写死这个正则
        //<div id="article_details" class="details">
        //String regex = "<div id=\"article_details\" class=\"details\">(<div(\\s+\\w+=\".*\")*>.*</div>)</div>";
//        String regex = "<div.*>.*</div>";
//        String regex = "<div id=\"article_content\" class=\"article_content\">(<div.*>.*</div>)*</div>";
//        String regex = "<div id=\"article_content\" class=\"article_content\">(.*)<!-- Baidu Button BEGIN -->";
//        String regex = "<div id=\"article_content\" class=\"article_content\">[^(<!-- Baidu Button BEGIN -->)]*<!-- Baidu Button BEGIN -->";
        //下面这个正则表达式是可以的,但是不太好.，

        System.out.println("html = " + html);
        String regex = "<div id=\"article_content\" class=\"article_content\">(.*?)(?=</div>\\s*<!-- Baidu Button BEGIN -->)";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
//        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
//            System.out.println("matcher.group() = " + matcher.group());
            //捕获组1是文章内容.
//            System.out.println("matcher.group() = " + matcher.group(1));
            String content = matcher.group(1);
            String result = HTMLUtils.parseContentHTML(content, map);

            map.put("content", result);
//            System.out.println("result = " + result);
            articlePage.add(map);
        }

        return articlePage;
    }


    /**
     * 爬取csdn首页中的特定内容
     *
     * @param html 需要进行处理的html文件
     * @return
     */
    public static ArrayList<HashMap<String, String>> parseHomePage(String html) {
        ArrayList<HashMap<String, String>> homePageList = new ArrayList<>();

        String allRegex = "<div class=\"blog_list\">.*?(<div.*?></div>)*?.*?</div>";
        Matcher allMatcher = Pattern.compile(allRegex, Pattern.DOTALL).matcher(html);
        HashMap<String, String> map;
        while(allMatcher.find()){
            map = new HashMap<>();
            String blog_list = allMatcher.group();
            String regex = "<a\\s*name=\"(\\d+)\".*href=\"(.*?)\".*>(.*?)</a>";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(blog_list);
            while (matcher.find()) {
                //获取匹配到的所有标签
//            String resultTag = matcher.group();
                //获得文章索引
                map.put("name", HTMLUtils.getSaveFormat(matcher.group(1)));
//            attrSaveMap(map, resultTag, "name");
                //获得文章的链接
                map.put("href", HTMLUtils.getSaveFormat(matcher.group(2)));
//            attrSaveMap(map, resultTag, "href");
                //获得文章的标题
                map.put("title", HTMLUtils.getSaveFormat(matcher.group(3)));
//            textSaveMap(map,resultTag,"title");
            }

            regex = "<div class=\"blog_list\">.*?(?<=<dd>)(.*?)</dd>";
            matcher = Pattern.compile(regex, Pattern.DOTALL).matcher(blog_list);
            while(matcher.find()){
                map.put("content",HTMLUtils.getSaveFormat(matcher.group(1)));
            }

            regex = "<a.*?class=\"user_name\".*?>(.*?)</a>";
            matcher = Pattern.compile(regex, Pattern.DOTALL).matcher(blog_list);
            while(matcher.find()){
                map.put("userName",HTMLUtils.getSaveFormat(matcher.group(1)));
            }

            homePageList.add(map);
        }

        //首页的文章正则,标题与链接那块区域
        return homePageList;
    }

    public static String getFileName(String url){
        int index = url.lastIndexOf("/");
        return url.substring(index+1);
    }

    //以下方法完全可以用java正则的捕获组去解决.....

    /**
     * 从标签中的属性存取到map集合中.
     * @param map 需要存取的集合
     * @param from 需要获取数据的源字符串
     * @param regex 需要获取数据的正则表达式
     */
/*    private static void attrSaveMap(HashMap<String, String> map, String from, String regex) {
        String result = from.replaceAll(".*" +
                regex +
                "=\"([^\"]*)\".*", "$1");
        map.put(regex, result);
    }*/

    /**
     * 从标签中的文本存取到map集合中
     * @param map 需要存取的集合
     * @param from 需要获取数据的源字符串
     */
/*    private static void textSaveMap(HashMap<String, String> map, String from,String textName) {
        //属性的正则:(\s+\w+=".*")*
        //匹配标签,并获取标签内容的正则
        String result = from.replaceAll("<(\\w+)(\\s+\\w+=\".*?\")*?>" +
                "(.*)" +
                "</\\1>", "$3");
        map.put(textName, result);
    }*/
}
