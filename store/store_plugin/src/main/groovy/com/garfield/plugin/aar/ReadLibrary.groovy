package com.garfield.plugin.aar

import com.garfield.plugin.util.JarUtils
import com.garfield.plugin.util.Logger
import org.gradle.api.Project
import org.gradle.api.file.FileCollection

/**
 * Created by gaowei on 2018/11/12
 */
class ReadLibrary {

    static void read(Project project) {


        List<File> deps = new ArrayList<>()

        project.android.applicationVariants.all { variant ->

            variant.javaCompile.classpath.each { file ->
                if (!file.exists() || deps.contains(file)) {
                    return
                }
                deps.add(file)

                if (file.path.contains("store_lib")) {
                    Logger.d("aa: " + file.path)
                    List<String> content = JarUtils.processJarFile(file)
                    content.each {
                        Logger.d(it)
                    }
                }

            }



        }

    }

}