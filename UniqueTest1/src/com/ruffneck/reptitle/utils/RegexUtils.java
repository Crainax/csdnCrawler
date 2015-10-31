package com.ruffneck.reptitle.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {


    /**
     * ��ȡcsdn�����е��ض�����
     *
     * @param html ��Ҫ���д����html�ļ�
     * @return
     */
    public static ArrayList<HashMap<String, String>> parseArticlePage(String html) {
        ArrayList<HashMap<String, String>> articlePage = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();


        //��ȡ����������
        String authorName = HTMLUtils.getAuthorName(html);
        map.put("authorName", HTMLUtils.getSaveFormat(authorName));

        //��ȡ���͵ı���
        String blogTitle = HTMLUtils.getBlogTitle(html);
        map.put("blogTitle", HTMLUtils.getSaveFormat(blogTitle));

        //��ȡ���µı���
        String articleTitle = HTMLUtils.getArticleTitle(html);
        map.put("articleTitle", HTMLUtils.getSaveFormat(articleTitle));

        //�������ݵ�����,�������д���������
        //<div id="article_details" class="details">
        //String regex = "<div id=\"article_details\" class=\"details\">(<div(\\s+\\w+=\".*\")*>.*</div>)</div>";
//        String regex = "<div.*>.*</div>";
//        String regex = "<div id=\"article_content\" class=\"article_content\">(<div.*>.*</div>)*</div>";
//        String regex = "<div id=\"article_content\" class=\"article_content\">(.*)<!-- Baidu Button BEGIN -->";
//        String regex = "<div id=\"article_content\" class=\"article_content\">[^(<!-- Baidu Button BEGIN -->)]*<!-- Baidu Button BEGIN -->";
        //�������������ʽ�ǿ��Ե�,���ǲ�̫��.��

        String regex = "<div id=\"article_content\" class=\"article_content\">(.*?)(?=</div>\\s*<!-- Baidu Button BEGIN -->)";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
//        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
//            System.out.println("matcher.group() = " + matcher.group());
            //������1����������.
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
     * ��ȡcsdn��ҳ�е��ض�����
     *
     * @param html ��Ҫ���д����html�ļ�
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
                //��ȡƥ�䵽�����б�ǩ
//            String resultTag = matcher.group();
                //�����������
                map.put("name", HTMLUtils.getSaveFormat(matcher.group(1)));
//            attrSaveMap(map, resultTag, "name");
                //������µ�����
                map.put("href", HTMLUtils.getSaveFormat(matcher.group(2)));
//            attrSaveMap(map, resultTag, "href");
                //������µı���
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

        //��ҳ����������,�����������ǿ�����
        return homePageList;
    }

    public static String getFileName(String url){
        int index = url.lastIndexOf("/");
        return url.substring(index+1);
    }

    //���·�����ȫ������java����Ĳ�����ȥ���.....

    /**
     * �ӱ�ǩ�е����Դ�ȡ��map������.
     * @param map ��Ҫ��ȡ�ļ���
     * @param from ��Ҫ��ȡ���ݵ�Դ�ַ���
     * @param regex ��Ҫ��ȡ���ݵ�������ʽ
     */
/*    private static void attrSaveMap(HashMap<String, String> map, String from, String regex) {
        String result = from.replaceAll(".*" +
                regex +
                "=\"([^\"]*)\".*", "$1");
        map.put(regex, result);
    }*/

    /**
     * �ӱ�ǩ�е��ı���ȡ��map������
     * @param map ��Ҫ��ȡ�ļ���
     * @param from ��Ҫ��ȡ���ݵ�Դ�ַ���
     */
/*    private static void textSaveMap(HashMap<String, String> map, String from,String textName) {
        //���Ե�����:(\s+\w+=".*")*
        //ƥ���ǩ,����ȡ��ǩ���ݵ�����
        String result = from.replaceAll("<(\\w+)(\\s+\\w+=\".*?\")*?>" +
                "(.*)" +
                "</\\1>", "$3");
        map.put(textName, result);
    }*/
}
