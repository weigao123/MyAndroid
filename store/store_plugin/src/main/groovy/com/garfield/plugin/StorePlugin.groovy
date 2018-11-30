package com.garfield.plugin

import com.garfield.plugin.aar.ReadLibrary
import com.garfield.plugin.spi.SpiRegister
import com.garfield.plugin.transform.MyTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

class StorePlugin implements Plugin<Project> {


    @Override
    void apply(Project project) {

        project.afterEvaluate {
            SpiRegister.register(project)

            project.android.registerTransform(new MyTransform(project))

            ReadLibrary.read(project)
        }

    }
}