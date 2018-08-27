package com.garfield.baselib.utils.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

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




    public void saveCrashToFile(Throwable ex) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        ex.printStackTrace(printWriter);

        BufferedWriter out = null;
        try {
            File file;
            for (int i = 0; ; i++) {
                file = new File(DirectoryUtils.getOwnCacheDirectory("crash/"), "bug_" + i + ".txt");
                if (!file.exists()) {
                    break;
                }
            }
            out = new BufferedWriter(new FileWriter(file), 2048);
            out.write(result.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
