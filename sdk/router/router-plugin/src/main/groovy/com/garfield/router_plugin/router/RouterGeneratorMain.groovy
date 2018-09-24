package com.garfield.router_plugin.router

import com.garfield.router_plugin.utils.Logger
import org.gradle.api.Project
import org.gradle.api.file.FileCollection

class RouterGeneratorMain {

    static void doSpi(Project project) {

        Logger.d("RouterGeneratorMain apply")

        project.afterEvaluate {
            project.android.applicationVariants.all { variant ->

                FileCollection t_compileClassPath = project.files(
                        project.android.bootClasspath,
                        variant.javaCompile.classpath,
                        variant.javaCompile.destinationDir)
                File t_spiSourceDir = project.file("${project.buildDir}/outputs2")
                File t_spiServiceDir = project.file("${project.buildDir}/intermediates/spi/${variant.dirName}/services")

                Logger.d("RouterGeneratorTask ${variant.name} create")

                def generateTask = project.tasks.create("generateServiceRegistry${variant.name.capitalize()}", RouterGeneratorTask.class) {
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
