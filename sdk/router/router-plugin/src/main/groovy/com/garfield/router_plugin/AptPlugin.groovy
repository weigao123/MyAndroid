package com.garfield.router_plugin

import com.garfield.router_plugin.router.RouterGeneratorMain
import org.gradle.api.Plugin
import org.gradle.api.Project

class AptPlugin implements Plugin<Project> {


    @Override
    void apply(Project project) {

        RouterGeneratorMain.doSpi(project)
    }
}