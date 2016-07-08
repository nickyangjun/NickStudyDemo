package com.nick.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

public class AppExtension {

    /** File of release variant output */
    protected File outputFile

    /** Task of android packager */
    Task aapt


    public AppExtension(Project project) {

    }
}