package com.ruffneck.reptitle.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by �𽣷�˵ on 2015/10/23.
 */
public class HTMLUtils {

    public static final String IMAGE_REPLACEMENT = "########IMAGE########";


    /**
     * ���ڷ��ص����ݵĻ��з��ַ����м�ֵ(Jsoup)
     */
    public static final String newLineSignal = "#%RuffneckNewLine%#";

    public static Element replaceNewLine(Element element) {

        String content = element.toString();
        String regex1 = "(<\\s*?/p\\s*?>)|(<br.*?>)|(<hr.*?>)";
        content = content.replaceAll(regex1, newLineSignal).replaceAll("<\\s*p\\s*+>", "");
        Document document = Jsoup.parse(content);

        return document.body();
    }

    public static String replaceNewLine(String string) {

        String regex1 = "(<\\s*?/p\\s*?>)|(<br.*?>)|(<hr.*?>)";
        return string.replaceAll(regex1, "\n");

    }

    public static String removeJavaScript(String string) {
        String regex = "<script.*?>.*?</script>";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(string);
//        ArrayList<String> matchResult = new ArrayList<>();
        while (matcher.find()) {
            string = matcher.replaceAll("");
//            String result = matcher.group();
//            matchResult.add(result);
        }
//        for (String s : matchResult) {
//            string = string.replaceAll(s, "");
//        }
        return string;
    }

    /*
            ��ʾ���	����	ʵ������	ʵ����
        ��	��	&cent;	&#162;
        ?	��	&pound;	&#163;
        ?	��Բ	&yen;	&#165;
        ��	��	&sect;	&#167;
        ?	��Ȩ	&copy;	&#169;
        ?	ע���̱�	&reg;	&#174;
        ��	�˺�	&times;	&#215;
        ��	����	&divide;	&#247;
     */
    public static String changeEscapeChar(String string) {
        //�滻����ת���ַ�:
        string = string.replaceAll("(&nbsp;)|(&#160;)", " ").replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&")
                .replaceAll("&quot;", "\"").replaceAll("&apos;", "'").replaceAll("&cent;", "��").replaceAll("&pound;", "?")
                .replaceAll("&yen;", "?").replaceAll("&sect;", "��").replaceAll("&copy;", "?").replaceAll("&reg;", "?")
                .replaceAll("&times;", "��").replaceAll("\t&divide;", "��");

        return string;
    }

    public static String removeAllTag(String string) {
        String regex = "<(\\w+)(\\s+\\w+=.*?)*?>";
        String regex2 = "</??\\s*?\\w+\\s*?/??>";

        return string.replaceAll(regex, "").replaceAll(regex2, "");
    }


    public static String getAuthorName(String string) {
        String regex = "<div id=\"blog_userface\">.*?(?<=<span>)(.*?)</span>";
        return getByRegex(string, regex);
    }

    public static String getBlogTitle(String string) {
        String regex = "<div id=\"blog_title\">.*?(?<=<h2>)(.*?)</h2>";
        return getByRegex(string, regex);
    }

    public static String getArticleTitle(String string) {
        String regex = "<span class=\"link_title\">(.*?)</span>";
        return getByRegex(string, regex);
    }

    private static String getByRegex(String string, String regex) {
//<a\s*name="(\d+)".*href="(.*)".*>(.*)</a>
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(string);
        String result = "";
        while (matcher.find()) {
            result = matcher.group(1);
//            System.out.println("result = " + result);
            result = removeAllTag(result);
            result = removeNewLine(result).trim();
        }
        return result;
    }


    public static String getSaveFormat(String string) {
//        string = replaceNewLine(string);
//        string = removeJavaScript(string);
//        string = changeEscapeChar(string);
//        return removeAllTag(string);        //�滻����ת���ַ�:
        return string.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("&amp;", "&");
    }

    public static String parseContentHTML(String string, HashMap<String, String> map) {
        string = replaceNewLine(string);
        string = removeJavaScript(string);
//        System.out.println("string = " + string);
        string = getAllImagesLinks(string, map);
        // string = changeEscapeChar(string);
        return removeAllTag(string);

    }

//    public static String markImageLocation(String string){
//        String regex = "<img.*?src=\"(.*?)\".*?>";
//
//    }

    public static String removeNewLine(String string) {
//        String string = "(?m)(\\n)|(\\r)";

        String regex = "(?m)(\\n)|(\\r)";
        return string.replaceAll(regex, "");
    }

    public static String getAllImagesLinks(String string, HashMap<String, String> map) {
        String regex = "<img.*?src=\"(.*?)\".*?>";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(string);
        int index = 0;
        while (matcher.find()) {
            String result = matcher.group(1);
//            string = matcher.replaceAll("########IMAGE########");
//            System.out.println("result = " + result);
            map.put("image" + index++, result);
//            System.out.println("index = " + index);
        }
        matcher = pattern.matcher(string);
        while(matcher.find()){
            string = matcher.replaceAll(IMAGE_REPLACEMENT);
        }
        //string = matcher.replaceAll("");
        return string;
    }

    /*
        public static void getAllImagesLinks(String string,LinkedHashMap<String,String> map){
        String regex = "<img.*?src=\"(.*?)\".*?>";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(string);
        int index = 0;
        while(matcher.find()){
            String result = matcher.group(1);
            map.put("image"+index,result);

        }
        String[] contents = string.split(regex);
        for (int i = 0; i < contents.length; i++) {
            map.put("content"+i,contents[i]);


        }
    }
     */

}
