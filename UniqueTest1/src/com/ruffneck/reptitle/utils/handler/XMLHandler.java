package com.ruffneck.reptitle.utils.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class XMLHandler extends DefaultHandler {

    private ArrayList<HashMap<String, String>> list;

    private HashMap<String, String> map;

    private String key;

    private String value;

    public ArrayList<HashMap<String, String>> getContent() {
        return list;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if ("data".equals(qName)) {
            map = new HashMap<>();
        } else {
            key = qName;
        }
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        list = new ArrayList<>();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        value = new String(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if ("data".equals(qName)) {
            list.add(map);
        } else {
            map.put(key, value);
        }
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }
}
