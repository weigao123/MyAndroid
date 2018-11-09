package com.garfield.plugin

import com.garfield.plugin.spi.SpiRegister
import org.gradle.api.Plugin
import org.gradle.api.Project

class StorePlugin implements Plugin<Project> {


    @Override
    void apply(Project project) {

        SpiRegister.register(project)
    }
}