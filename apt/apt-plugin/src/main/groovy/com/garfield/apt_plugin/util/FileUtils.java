package com.garfield.apt_plugin.util;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarFile;

public class FileUtils {

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                // 文件夹内有内容
                for (String child : children) {
                    boolean success = deleteDir(new File(dir, child));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public static List<String> loadClasses(Iterable<File> files) {
        List<String> classes = new LinkedList<>();
        for (File file : files) {
            try {
                loadClass(file, classes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return classes;
    }

    private static void loadClass(File file, List<String> classes) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File childFile : files) {
                    loadClass(childFile, classes);
                }
            }
        } else if (file.getName().endsWith(".class")) {
            classes.add(file.getCanonicalPath());
        } else if (file.getName().endsWith(".jar")) {
            //classes.addAll(JarUtils.getJarClass(new JarFile(file)));
        }
    }

}
