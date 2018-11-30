package com.garfield.plugin.util;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class JarUtils {
    public static String getManifestInfo(String key) {
        try {
            Class theClass = JarUtils.class;
            String classPath = theClass.getResource(theClass.getSimpleName() + ".class").toString();
            String libPath = classPath.substring(0, classPath.lastIndexOf("!"));
            String filePath = libPath + "!/META-INF/MANIFEST.MF";
            Manifest manifest = new Manifest(new URL(filePath).openStream());
            Attributes attributes = manifest.getMainAttributes();
            return attributes.getValue(key);
        } catch (Exception e) {
            // Silently ignore wrong manifests on classpath?
        }
        return "";
    }



    public static List<String> getJarClass(JarFile jar) {
        List<String> list = new LinkedList<>();
        final Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            final JarEntry entry = entries.nextElement();
            if (entry.getName().endsWith(".class")) {
                list.add(entry.getName());
            }
        }
        return list;
    }

    public static List<String> processJarFile(File jarFile) {
        List<String> content = new ArrayList<>();
        try (ZipFile zipFile = new ZipFile(jarFile)) {
            if (hasServiceEntry(zipFile)) {
                ZipInputStream is = null;
                ZipOutputStream os = null;
                try {
                    File tempFile = new File(jarFile.getPath() + ".tmp");
                    is = new ZipInputStream(new FileInputStream(jarFile));
                    os = new ZipOutputStream(new FileOutputStream(tempFile));

                    ZipEntry entry;
                    while ((entry = is.getNextEntry()) != null) {
                        if (isServiceEntry(entry)) {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                content.add(line);
                            }
                        } else {
                            os.putNextEntry(new ZipEntry(entry));
                            IOUtils.copy(is, os);
                        }
                    }
                    //jarFile.delete();
                    //tempFile.renameTo(jarFile);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (is != null) {
                        is.close();
                    }
                    if (os != null) {
                        os.flush();
                        os.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    private static boolean hasServiceEntry(ZipFile zipFile) {
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            Logger.d(entry.getName());
            if (isServiceEntry(entry)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isServiceEntry(ZipEntry entry) {
        return !entry.isDirectory() && entry.getName().startsWith("META-INF/services/");
    }

}
