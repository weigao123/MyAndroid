package com.garfield.plugin

import org.gradle.api.Project
import org.gradle.api.Plugin

class CustomPlugin implements Plugin<Project> {


    @Override
    void apply(Project project) {
        project.task('CustomPluginTask') {
            group '组CustomPlugin'
            doLast {

            }
        }
    }
}