package com.garfield.java.javacreate;

import com.garfield.java.util.L;

import java.io.InputStream;

/**
 * Created by gaowei on 2017/7/31.
 */

public class ClassLoaderTest {

    public static void main(String[] args) throws Exception {


        Object object = new Object();
        L.dl("main");
        ClassLoader classLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                try {
                    L.dl("Name: "+name);
                    String fileName = name.substring(name.lastIndexOf('.')+1)+".class";
                    InputStream inputStream = getClass().getResourceAsStream(fileName);
                    if (inputStream == null) {
                        //L.dl("inputStream null: "+fileName);
                        return super.loadClass(name);
                    }
                    byte[] b = new byte[inputStream.available()];
                    inputStream.read(b);
                    return defineClass(name, b, 0, b.length);
                } catch (Exception e) {

                }
                return super.loadClass(name);
            }
        };

        new MyInterface() {
            @Override
            public void set() {

            }
        };
        classLoader.loadClass("com.garfield.java.javacreate.ClassLoaderTest").newInstance();
    }



}
