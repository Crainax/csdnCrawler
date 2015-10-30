package com.ruffneck.reptitle.utils;

/**
 * Created by ·ð½£·ÖËµ on 2015/10/24.
 */
public class StringUtils {

    public static boolean isEmpty(String string) {
        if (string == null) return true;
        if (string.length() < 1) return true;

        return false;
    }
}
