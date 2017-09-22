/*============================================================================
 *
 * Copyright (c) 2000-2012 Smart Trade Technologies. All Rights Reserved.
 *
 * This software is the proprietary information of Smart Trade Technologies
 * Use is subject to license terms. Duplication or distribution prohibited.
 *
 *============================================================================*/

package com.smarttrade.builders.task.eclipse.model;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.gradle.api.Project;
import org.slf4j.Logger;


/**
 * @author mbarbier
 */
public class LauncherModel implements Cloneable {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(LauncherModel.class);

    private String name;
    private String projectName;

    private String main;
    private String args;
    private String vmargs;
    private String workingDirectory;

    private final Set<File> libs = new LinkedHashSet<>();
    private final Set<String> resources = new LinkedHashSet<>();
    private final Set<Project> projects = new LinkedHashSet<>();
    private final Map<String, String> environmentVariables = new LinkedHashMap<>();
    private String mapperResourceRelativePath = "/src/main/java/";

    @Override
    public LauncherModel clone() {
        final LauncherModel model = new LauncherModel();
        model.main = this.main;
        model.name = this.name;
        model.projectName = this.projectName;
        model.args = this.args;
        model.vmargs = this.vmargs;
        model.libs.addAll(this.libs);
        model.resources.addAll(this.resources);
        model.environmentVariables.putAll(this.environmentVariables);
        model.projects.addAll(this.projects);
        return model;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(final String projectName) {
        this.projectName = projectName;
    }

    public String getMain() {
        return main;
    }

    public void setMain(final String main) {
        this.main = main;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(final String args) {
        if (this.args == null) this.args = args;
        else this.args += " " + args;
    }

    public String getVmargs() {
        return vmargs;
    }

    public void setVmargs(final String vmargs) {
        if (this.vmargs == null) this.vmargs = vmargs;
        else this.vmargs += " " + vmargs;
    }

    public Set<File> getLibs() {
        return libs;
    }

    public Set<String> getResources() {
        return resources;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public Map<String, String> getEnvironmentVariables() {
        return environmentVariables;
    }

    public void addLibs(final Iterable<File> in) {
        log.info("Add addLibs : {}", in);
        for (final File f : in) {
            libs.add(f);
        }
    }
    
    public void addLib(final File in) {
        log.info("Add addLib : {}", in);
        libs.add(in);
    }

    public void addResources(final Iterable<String> in) {
        log.info("Add resources : {}", in);
        for (final String f : in) {
            resources.add(f);
        }
    }
    
    public void addResource(final String in) {
        log.info("Add resource : {}", in);
        resources.add(in);
    }

    public void addProjects(final Iterable<Project> in) {
        log.info("Add projects : {}", in);
        for (final Project f : in) {
            projects.add(f);
        }
    }
    
    public void addProject(final Project in) {
        log.info("Add project : {}", in);
    	projects.add(in);
    }

    public void addEnvironmentVariables(final Map<String, String> in) {
        log.info("Add environmentVariables : {}", in);
        environmentVariables.putAll(in);
    }

    public void setMapperResourceRelativePath(final String mapperResourceRelativePath) {
        this.mapperResourceRelativePath = mapperResourceRelativePath;
    }

    public String getMapperResourceRelativePath() {
        return mapperResourceRelativePath;
    }

    public Object getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(final String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }
}
