package com.garfield.baselib.utils.file;

/**
 * Created by gaowei3 on 2016/10/21.
 */

public class FileUtils {

    /**
     * 移除文件后缀
     */
    public static String removeFileSuffix(String filePath) {
        if ((filePath != null) && (filePath.length() > 0)) {
            int dot = filePath.lastIndexOf('.');
            if ((dot > -1) && (dot < (filePath.length()))) {
                return filePath.substring(0, dot);
            }
        }
        return filePath;
    }

    public static String getFileName(String filePath) {
        if ((filePath != null) && (filePath.length() > 0)) {
            int divide = filePath.lastIndexOf('/');
            if ((divide > -1) && (divide < (filePath.length()))) {
                return filePath.substring(divide, filePath.length() - 1);
            }
        }
        return filePath;
    }

    public static String getFileNameWithoutSuffix(String filePath) {
        return getFileName(removeFileSuffix(filePath));
    }
}
