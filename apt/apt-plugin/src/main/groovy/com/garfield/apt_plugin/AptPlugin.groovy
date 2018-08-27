package com.garfield.apt_plugin

import com.garfield.apt_plugin.spi.SpiGeneratorMain
import org.gradle.api.Plugin
import org.gradle.api.Project

class AptPlugin implements Plugin<Project> {


    @Override
    void apply(Project project) {

        SpiGeneratorMain.doSpi(project)
    }
}