package com.garfield.test_plugin;

import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.gradle.internal.pipeline.TransformManager;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

class MyTransform extends Transform {

    @Override
    public String getName() {
        return "MyTransform";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        Logger.d("transform");
        return TransformManager.CONTENT_JARS;
    }

    @Override
    public Set<QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(TransformInvocation invocation) throws TransformException, InterruptedException, IOException {
        super.transform(invocation);

        Logger.d("transform");
        for (TransformInput input : invocation.getInputs()) {
            input.getJarInputs().parallelStream().forEach( jarInput -> {

                Logger.d("aaa: " + jarInput.getName());

                File src = jarInput.getFile();
                Logger.d("bbb: " + src.getAbsolutePath());
                try {
                    JarFile jarFile = new JarFile(src);
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        }

    }
}