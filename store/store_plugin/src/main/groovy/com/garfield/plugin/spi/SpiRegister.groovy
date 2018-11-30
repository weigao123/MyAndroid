package com.garfield.plugin.spi

import com.garfield.plugin.util.Logger
import org.gradle.api.Project
import org.gradle.api.file.FileCollection

class SpiRegister {

    static void register(Project project) {

        project.extensions.create('spiSetting', SpiSetting.class)

        Logger.d("SpiRegister apply")

        project.android.applicationVariants.all { variant ->

            Logger.d("SpiTask ${variant.name} create")

            FileCollection varCompileClassPath = project.files(
                    project.android.bootClasspath,     //如果不加会造成无法识别系统类
                    variant.javaCompile.classpath,
                    variant.javaCompile.destinationDir)

            File varTargetDir = project.file(variant.javaCompile.destinationDir)

            // bootClasspath，List<File>，一个android.jar
//                project.android.bootClasspath.each {
//                    Logger.d("bootClasspath: " + it)
//                }

            // source，FileCollection，全是java文件，自己写的和R
//                variant.javaCompile.source.each {
//                    Logger.d("source: " + it)
//                }

            // 上面路径编译后目录，File，一个文件夹
//                Logger.d("destinationDir: " + variant.javaCompile.destinationDir)

            // classpath，FileCollection，所有依赖的aar/jar
//                variant.javaCompile.classpath.each {
//                    Logger.d("classpath: " + it)
//                }



            def spiTask = project.tasks.create("generateServiceRegistry${variant.name.capitalize()}", SpiTask.class) {
                description = "Generate ServiceRegistry for ${variant.name.capitalize()}"
                variantName = variant.name
                compileClassPath = varCompileClassPath
                targetDir = varTargetDir
                setting = project.spiSetting
            }

            spiTask.mustRunAfter(variant.javaCompile)
            project.tasks["assemble${variant.name.capitalize()}"].dependsOn(spiTask)
        }
    }
}
