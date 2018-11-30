package com.garfield.plugin.transform

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.garfield.plugin.util.Logger
import com.google.common.collect.ImmutableSet
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

/**
 * Created by gaowei on 2018/11/12
 */
class MyTransform extends Transform {

    Project project

    MyTransform(Project project) {
        this.project = project
    }

    @Override
    String getName() {
        return "My"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation invocation) throws TransformException, InterruptedException, IOException {

        if (!invocation.incremental) {
            invocation.outputProvider.deleteAll()
        }

        List<File> compilePath = new ArrayList()
        for (TransformInput transformInput : invocation.inputs) {
            for (JarInput jarInput : transformInput.jarInputs) {
                Logger.d("transform jarInput: " + jarInput.file.absolutePath)
                compilePath.add(jarInput.file)
                File dest = invocation.outputProvider.getContentLocation(
                        jarInput.name, jarInput.contentTypes,
                        jarInput.scopes, Format.JAR)
                try {
                    FileUtils.copyFile(jarInput.file, dest)
                } catch (IOException e) {
                    throw new RuntimeException(e)
                }
            }
            for (DirectoryInput directoryInput : transformInput.directoryInputs) {
                Logger.d("transform directoryInput: " + directoryInput.file.absolutePath)
                compilePath.add(directoryInput.file)
                File dest = invocation.outputProvider.getContentLocation(
                        directoryInput.name, directoryInput.contentTypes,
                        directoryInput.scopes, Format.DIRECTORY)
                try {
                    FileUtils.copyDirectory(directoryInput.file, dest)
                } catch (IOException e) {
                    throw new RuntimeException(e)
                }
            }
        }

        File dest = invocation.getOutputProvider().getContentLocation(
                "DRouterOutput", TransformManager.CONTENT_CLASS,
                ImmutableSet.of(QualifiedContent.Scope.PROJECT), Format.DIRECTORY)

    }
}