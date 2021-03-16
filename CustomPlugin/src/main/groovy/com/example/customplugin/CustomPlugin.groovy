package com.example.customplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class CustomPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.android.registerTransform(new CustomTransform(project))
    }
}