package com.smarttrade.gradle.core

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.internal.AbstractTask
import org.gradle.api.tasks.TaskAction

import groovy.lang.GroovyClassLoader

import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile

@CacheableTask
class GenerateFileTemplateTask extends AbstractTask {
    def bindings
    
    @InputFile
    File template 
	
	@OutputFile
	File outputFile 
    
    @TaskAction
    void action() {
        def templateEngine = new groovy.text.SimpleTemplateEngine()
        if(!outputFile.getParentFile().exists()) file.getParentFile().mkdirs()
        outputFile.withWriter('UTF-8') { writer ->
            templateEngine.createTemplate(template).make(bindings).writeTo(writer)
        }
    }
}