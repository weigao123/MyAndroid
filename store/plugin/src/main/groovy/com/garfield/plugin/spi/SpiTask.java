package com.garfield.plugin.spi;

import com.garfield.plugin.util.Logger;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class SpiTask extends DefaultTask {

    private FileCollection compileClassPath;
    private File targetDir;
    private String variantName;
    private SpiSetting setting;

    private ClassPool pool;

    @TaskAction
    void run() {
        Logger.d("SpiTask " + variantName + " start");
        this.pool = new ClassPool();
        startExecute();
    }

    private void startExecute() {
        try {
            for (File file : this.compileClassPath) {
                Logger.d("scan path: " + file.getCanonicalPath());
                //pool.appendClassPath(file.getAbsolutePath());
            }

            List<CtClass> classFiles = loadClasses(compileClassPath);
            Logger.d("scan size: " + classFiles.size());

            processClass(classFiles);

        } catch (Exception e) {
            throw new GradleException("Could not generate ServiceRegistry", e);
        }
    }

    private void processClass(List<CtClass> ctClasses) {
        for (CtClass cc : ctClasses) {
            doProcessCc(cc);
        }
    }

    private void doProcessCc(CtClass cc) {
//        if (cc.hasAnnotation(PackType.class)) {
//            try {
//                CtClass c = cc.getSuperclass();
//                Logger.d("super: " + c.getName());
//            } catch (NotFoundException e) {
//                e.printStackTrace();
//            }
//            Logger.d("===== " + cc.getName());
//        }
    }

    private List<CtClass> loadClasses(Iterable<File> files) throws IOException {
        List<CtClass> classes = new LinkedList<>();
        for (File file : files) {
            loadClass(file, classes);
        }
        return classes;
    }

    private void loadClass(File file, List<CtClass> classes) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File childFile : files) {
                    loadClass(childFile, classes);
                }
            }
        } else if (file.getName().endsWith(".class")) {
            classes.add(classToCc(file));
        } else if (file.getName().endsWith(".jar")) {
            classes.addAll(jarToCc(new JarFile(file)));
        }
    }

    private CtClass classToCc(File file) throws IOException {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
            return pool.makeClass(stream);
        } finally {
            if (null != stream) {
                stream.close();
            }
        }
    }

    private List<CtClass> jarToCc(JarFile jar) throws IOException {
        List<CtClass> ctClasses = new LinkedList<>();
        InputStream stream = null;
        final Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            final JarEntry entry = entries.nextElement();
            if (entry.getName().endsWith(".class")) {
                try {
                    stream = jar.getInputStream(entry);
                    ctClasses.add(pool.makeClass(stream));
                } finally {
                    if (null != stream) {
                        stream.close();
                    }
                }
            }
        }
        return ctClasses;
    }

    public void setCompileClassPath(FileCollection compileClassPath) {
        this.compileClassPath = compileClassPath;
    }

    public void setTargetDir(File targetDir) {
        this.targetDir = targetDir;
    }

    public void setSetting(SpiSetting setting) {
        this.setting = setting;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }
}
