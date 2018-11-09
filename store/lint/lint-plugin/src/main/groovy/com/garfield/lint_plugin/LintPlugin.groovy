package com.garfield.lint_plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class LintPlugin implements Plugin<Project> {


    @Override
    void apply(Project project) {
        addRunLintTask(project)
    }

    private static void addRunLintTask(Project project) {

        RunLintTask runFullLint = project.getTasks().create("runFullLint", RunLintTask.class)
        runFullLint.setFullMode(true)

        RunLintTask runGitLint = project.getTasks().create("runGitLint", RunLintTask.class)
        runGitLint.setFromGit(true)
    }
}