package com.garfield.java.javacreate;

import com.garfield.java.util.L;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by gaowei on 2017/7/27.
 */

public class HotSwapClassLoader extends URLClassLoader {

    public HotSwapClassLoader(URL[] urls) {
        super(urls);
    }

    public HotSwapClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        L.dl("loadClass1 "+name);
        return super.loadClass(name);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        L.dl("loadClass2 "+name);
        return super.loadClass(name, resolve);
    }

    // 下面的两个重载load方法实现类的加载，仿照ClassLoader中的两个loadClass()
    // 具体的加载过程代理给父类中的相应方法来完成
    public Class<?> load(String name) throws ClassNotFoundException {
        return load(name, false);
    }

    public Class<?> load(String name, boolean resolve) throws ClassNotFoundException {
        // 若类已经被加载，则重新再加载一次
        if (null != super.findLoadedClass(name)) {
            return reload(name, resolve);
        }
        // 否则用findClass()首次加载它
        Class<?> clazz = super.findClass(name);
        if (resolve) {
            super.resolveClass(clazz);
        }
        return clazz;
    }

    public Class<?> reload(String name, boolean resolve) throws ClassNotFoundException {
        return new HotSwapClassLoader(super.getURLs(), super.getParent()).load(
                name, resolve);
    }
}
