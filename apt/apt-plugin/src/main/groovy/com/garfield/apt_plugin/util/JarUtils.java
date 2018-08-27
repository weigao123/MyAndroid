package com.garfield.apt_plugin.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javassist.ClassPool;
import javassist.CtClass;

/**
 * Created by jiangyiwang on 2018/4/11.
 */
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
}
