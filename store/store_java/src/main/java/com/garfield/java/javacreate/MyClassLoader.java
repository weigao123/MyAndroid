package com.garfield.java.javacreate;

import com.garfield.java.util.L;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by gaowei on 2017/7/27.
 */

public class MyClassLoader extends URLClassLoader {

    /**
     * 下面的路径属于系统级AppClassLoader，不会调用到自定义类加载器
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
        L.dl("[loadClass] start name:" + name + "  resolve:" + resolve + "  findLoadedClass:" + super.findLoadedClass(name));
        //Class<?> result = super.loadClass(name, resolve);
        Class<?> result = loadBySelfThenParent(name, resolve);
        L.dl("[loadClass] end name:" + name + "  result loader: " + (result.getClassLoader() == null ? "BootstrapClassloader" : result.getClassLoader()));
        return result;
    }

    public Class<?> loadBySelf(String name) throws ClassNotFoundException {
        return loadBySelf(name, false);
    }

    private Class<?> loadBySelf(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> loadedClass = super.findLoadedClass(name);
        L.dl("[loadBySelf] start name:" + name + "  findLoadedClass:" + loadedClass);
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
        L.dl("[loadBySelf] end name:" + name + "  result:" + (clazz == null ? "fail":"success"));
        return clazz;
    }

    private Class<?> loadByParent(String name, boolean resolve) throws ClassNotFoundException {
        L.dl("[loadByParent] start name:" + name);
        Class<?> result = super.loadClass(name, resolve);
        L.dl("[loadByParent] end name:" + name + "  result:" + (result == null ? "fail":"success"));
        return result;
    }

    public Class<?> load(String name) throws ClassNotFoundException {
        return load(name, false);
    }

    public Class<?> load(String name, boolean resolve) throws ClassNotFoundException {
        return loadClass(name, resolve);
    }

    private Class<?> loadBySelfThenParent(String name) throws ClassNotFoundException {
        return loadBySelfThenParent(name, false);
    }

    private Class<?> loadBySelfThenParent(String name, boolean resolve) throws ClassNotFoundException {
        L.dl("[loadBySelfThenParent] name:" + name);
        Class<?> result = loadBySelf(name, resolve);
        if (result == null) {
            result = loadByParent(name, resolve);
        }
        return result;
    }
}
