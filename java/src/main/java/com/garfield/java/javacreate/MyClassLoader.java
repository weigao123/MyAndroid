package com.garfield.java.javacreate;

import com.garfield.java.util.L;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by gaowei on 2017/7/27.
 */

public class MyClassLoader extends URLClassLoader {

    /**
     * new URL[]{ new URL("file:/C:/Projects/MyAndroid/java/build/classes/main/") }
     */
    public MyClassLoader(URL[] urls) {
        super(urls);
    }

    public MyClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        L.p("[loadClass] name:" + name + "  resolve:" + resolve + "  findLoadedClass:" + super.findLoadedClass(name));
        //Class<?> result = super.loadClass(name, resolve);
        Class<?> result = loadBySelfFirst(name, resolve);
        L.p("[loadClass] result: " + result.getClassLoader());
        return result;
    }

    public Class<?> loadWithoutParent(String name) throws ClassNotFoundException {
        return loadWithoutParent(name, false);
    }

    public Class<?> loadWithoutParent(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> loadedClass = super.findLoadedClass(name);
        L.p("[loadWithoutParent] findLoadedClass:" + loadedClass);
        if (null != loadedClass) {
            return loadedClass;
        }

        Class<?> clazz = null;
        try {
            clazz = super.findClass(name);
            if (resolve) {
                super.resolveClass(clazz);
            }
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
        }
        L.p("[loadWithoutParent] result:" + clazz);
        return clazz;
    }

    public Class<?> load(String name) throws ClassNotFoundException {
        return load(name, false);
    }

    public Class<?> load(String name, boolean resolve) throws ClassNotFoundException {
        return loadClass(name, resolve);
    }

    public Class<?> loadBySelfFirst(String name) throws ClassNotFoundException {
        return loadBySelfFirst(name, false);
    }

    public Class<?> loadBySelfFirst(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> result = loadWithoutParent(name, resolve);
        if (result == null) {
            result = super.loadClass(name, resolve);
        }
        return result;
    }
}
