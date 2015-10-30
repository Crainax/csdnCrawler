package com.ruffneck.reptitle.utils;

import com.ruffneck.reptitle.utils.handler.XMLHandler;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by 佛剑分说 on 2015/10/22.
 */
public class StreamUtils {

    public static String Stream2String(InputStream is, String charsetName) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int len;
        while ((len = is.read(b)) != -1) {
            baos.write(b, 0, len);
        }
        return baos.toString(charsetName);
    }


    /**
     * 存储集合中的信息到XML文件中(用的是dom4j库中的方法去写,其实也可以用字符串去拼接)
     *
     * @param list 需要写入文件的ArrayList集合
     * @param file 需要写入的文件名字
     * @return
     */
    public static File saveData(ArrayList<HashMap<String, String>> list, File file) throws IOException, SAXException {
        if (!file.exists()) file.createNewFile();

        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("utf-8");
        Element datas = document.addElement("datas");

        for (HashMap<String, String> hashMap : list) {
            Element data = datas.addElement("data");
            for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                Element element = data.addElement(entry.getKey());
                element.setText(entry.getValue());
            }
        }

        FileOutputStream fos = new FileOutputStream(file);
        //创建紧凑的格式,节省内存
        OutputFormat format = OutputFormat.createCompactFormat();
        XMLWriter writer = new XMLWriter(fos, format);
        format.setEncoding("utf-8");
        writer.write(document);
        writer.close();

        return file;
    }

    /**
     * 存储集合中的信息到XML文件中(用的是dom4j库中的方法去写,其实也可以用字符串去拼接)
     *
     * @param map  需要写入文件的LinkedHashMap集合
     * @param file 需要写入的文件名字
     * @return
     */
    public static File saveData(LinkedHashMap<String, String> map, File file) throws IOException {
        if (!file.exists()) file.createNewFile();

        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("utf-8");
        Element datas = document.addElement("datas");

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Element keyElement = datas.addElement(key);
            keyElement.addText(value);
        }

        FileOutputStream fos = new FileOutputStream(file);
        //创建紧凑的格式,节省内存
        OutputFormat format = OutputFormat.createCompactFormat();
        XMLWriter writer = new XMLWriter(fos, format);
        format.setEncoding("utf-8");
        writer.write(document);
        writer.close();

        return file;
    }




    /**
     * 自定义StringBuilder来把ArrayList<HashMap<String>>的数据存取到一个xml文件中.没有用到第三方库
     *
     * @param list 需要存储的集合
     * @param file 需要存储到的xml文件
     * @return 需要存储到的xml文件
     * @throws IOException
     */
    public static File writeData(ArrayList<HashMap<String, String>> list, File file) throws IOException {
        if (!file.exists()) file.createNewFile();else return null;

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
        //把xml的属性写下来
        bw.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        StringBuilder sb = new StringBuilder();
        sb.append("<datas>");
        for (HashMap<String, String> map : list) {
            sb.append("<data>");
            for (HashMap.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append("<").append(key).append(">");
                sb.append(value);
                sb.append("</").append(key).append(">");
            }
            sb.append("</data>");
        }
        sb.append("</datas>");

        bw.write(sb.toString());

        bw.close();

        return file;
    }

    /**
     * 以能方便查看的格式去写文件,原理同上
     * @param list 需要存储的集合
     * @param file 需要写到的文件
     * @return 需要写到的文件
     * @throws IOException
     */
    public static File writeMap(ArrayList<HashMap<String, String>> list, File file) throws IOException {

        if (!file.exists()) file.createNewFile();else return null;
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
        //把xml的属性写下来
        for (HashMap<String, String> map : list) {
            for (HashMap.Entry<String, String> entry : map.entrySet()) {
                String key = HTMLUtils.changeEscapeChar(entry.getKey());
                String value = HTMLUtils.changeEscapeChar(entry.getValue());
                bw.write("["+key+"]:\n");
                bw.write(value);
                bw.write("\n");
            }
            bw.write("*****************************************Split Character****************************************\n");
        }

        bw.close();

        return file;
    }

    /**
     * 把xml文件中的数据读取到ArrayList<HashMap<String,String>>中,没用到第三方库
     *
     * @param xmlFile 需要读取的文件
     * @return 读取并存取到的集合.
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public static ArrayList<HashMap<String, String>> readData(File xmlFile) throws IOException, SAXException, ParserConfigurationException {

        SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
        XMLHandler xmlHandler = new XMLHandler();
        saxParser.parse(xmlFile, xmlHandler);
        ArrayList<HashMap<String, String>> content = xmlHandler.getContent();
        System.out.println("content = " + content);

        return content;
    }

}
