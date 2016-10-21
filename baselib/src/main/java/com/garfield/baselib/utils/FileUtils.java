package com.garfield.baselib.utils;

/**
 * Created by gaowei3 on 2016/10/21.
 */

public class FileUtils {

    public static String removeFileSuffix(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }
}
