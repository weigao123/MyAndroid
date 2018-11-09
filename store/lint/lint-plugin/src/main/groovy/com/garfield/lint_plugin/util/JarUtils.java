package com.garfield.lint_plugin.util;

import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

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
}
