package com.garfield.apt_plugin.spi

import com.garfield.apt_plugin.util.Logger
import org.gradle.api.Project
import org.gradle.api.file.FileCollection

class SpiGeneratorMain {

    static void doSpi(Project project) {

        Logger.d("SpiGeneratorMain apply")

        project.afterEvaluate {
            project.android.applicationVariants.all { variant ->

                FileCollection t_compileClassPath = project.files(
                        project.android.bootClasspath,     //如果不加会造成无法识别系统类
                        variant.javaCompile.classpath,
                        variant.javaCompile.destinationDir)
                File t_spiSourceDir = project.file("${project.buildDir}/outputs2")
                File t_spiServiceDir = project.file("${project.buildDir}/intermediates/spi/${variant.dirName}/services")

//                // bootClasspath，List<File>，一个android.jar
//                project.android.bootClasspath.each {
//                    Logger.d("bootClasspath: " + it)
//                }
//                // classpath，FileCollection，所有依赖的aar/jar
//                variant.javaCompile.classpath.each {
//                    Logger.d("classpath: " + it)
//                }
//                // source，FileCollection，全是java文件，自己写的和R
//                variant.javaCompile.source.each {
//                    Logger.d("source: " + it)
//                }
//                // 编译后路径，File，一个文件夹
//                Logger.d("destinationDir: " + variant.javaCompile.destinationDir)

                Logger.d("SpiGeneratorTask ${variant.name} create")

                def generateTask = project.tasks.create("generateServiceRegistry${variant.name.capitalize()}", SpiGeneratorTask.class) {
                    description = "Generate ServiceRegistry for ${variant.name.capitalize()}"
                    variantName = variant.name
                    compileClassPath = t_compileClassPath
                    spiSourceDir = t_spiSourceDir
                    spiServiceDir = t_spiServiceDir
                }

                project.tasks["assemble${variant.name.capitalize()}"].dependsOn(generateTask)
            }
        }

    }
}
