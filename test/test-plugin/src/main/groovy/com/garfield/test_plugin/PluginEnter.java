package com.garfield.test_plugin;

import com.android.build.gradle.AppExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class PluginEnter implements Plugin<Project> {


    @Override
    public void apply(Project project) {

        Logger.d("apply");
        AppExtension android = project.getExtensions().getByType(AppExtension.class);
        //android.registerTransform(new MyTransform());
    }
}