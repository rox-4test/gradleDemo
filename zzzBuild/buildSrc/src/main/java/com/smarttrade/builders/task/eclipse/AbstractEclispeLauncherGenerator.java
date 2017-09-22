/*============================================================================
 *
 * Copyright (c) 2000-2012 Smart Trade Technologies. All Rights Reserved.
 *
 * This software is the proprietary information of Smart Trade Technologies
 * Use is subject to license terms. Duplication or distribution prohibited.
 *
 *============================================================================*/

package com.smarttrade.builders.task.eclipse;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map.Entry;

import org.gradle.api.Project;
import org.gradle.api.UncheckedIOException;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.bundling.Jar;
import org.gradle.internal.xml.SimpleXmlWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smarttrade.builders.task.eclipse.model.LauncherModel;

/**
 * @author ldanesi
 */
public abstract class AbstractEclispeLauncherGenerator {

    protected static String JREENTRY = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n\r<runtimeClasspathEntry containerPath=\"org.eclipse.jdt.launching.JRE_CONTAINER\" javaProject=\"system\" path=\"1\" type=\"4\"/>\n\r";
    protected static String PATHENTRY = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n\r<runtimeClasspathEntry internalArchive=\"%s\" path=\"3\" type=\"2\"/>\n\r";
    protected static String PROJENTRY = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n\r<runtimeClasspathEntry path=\"3\" projectName=\"%s\" type=\"1\"/>\n\r";
    protected static String JARENTRY = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n\r<runtimeClasspathEntry externalArchive=\"%s\" path=\"3\" type=\"2\"/>\n\r";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected LauncherModel model;
    protected File output;
    protected SimpleXmlWriter w;

    public void generate() {
        log.info("Generating launcher #" + output.getName());

        try {
            output.getParentFile().mkdirs();
            final OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(output));
            try {
                w = new SimpleXmlWriter(outputStream, "  ");
                writeLauncher();
                w.flush();
            } finally {
                outputStream.close();
            }
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }

        log.info("Launcher generated");
    }

    protected abstract void writeLauncher() throws IOException;

    public void setModel(final LauncherModel model) {
        this.model = model;
    }

    public void setOutput(final File output) {
        this.output = output;
    }

    protected void addCoveragePaths(final String... listEntries) throws IOException {
        w.startElement("listAttribute");
        w.attribute("key", "com.mountainminds.eclemma.core.SCOPE_IDS");
        for (final String listEntry : listEntries) {
            addListEntry("=" + listEntry);
        }
        w.endElement();
    }

    protected void addResourcePaths(final String... listEntries) throws IOException {
        w.startElement("listAttribute");
        w.attribute("key", "org.eclipse.debug.core.MAPPED_RESOURCE_PATHS");
        for (final String listEntry : listEntries) {
            addListEntry("/" + listEntry);
        }
        w.endElement();
    }

    protected void addResourcePaths() throws IOException {
        w.startElement("listAttribute");
        w.attribute("key", "org.eclipse.debug.core.MAPPED_RESOURCE_PATHS");
        addListEntry("/" + model.getProjectName());
        w.endElement();
    }

    protected void addResourceTypes(final String... entryTypes) throws IOException {
        w.startElement("listAttribute");
        w.attribute("key", "org.eclipse.debug.core.MAPPED_RESOURCE_TYPES");
        for (final String entryType : entryTypes) {
            addListEntry(entryType);
        }
        w.endElement();
    }

    protected void addResourceTypes() throws IOException {
        w.startElement("listAttribute");
        w.attribute("key", "org.eclipse.debug.core.MAPPED_RESOURCE_TYPES");
        addListEntry("4");
        w.endElement();
    }

    protected void addResourceClasspath() throws IOException {

        w.startElement("listAttribute");
        w.attribute("key", "org.eclipse.jdt.launching.CLASSPATH");

        addListEntry(JREENTRY);

        for (final String folder : model.getResources()) {
            if (!folder.startsWith("/")) {
                addListEntry(String.format(PATHENTRY, "/" + folder));
            } else {
                addListEntry(String.format(PATHENTRY, folder));
            }
        }
        for (final Project project : model.getProjects()) {
            log.info("Inserted projects  : " + project);
            addListEntry(String.format(PROJENTRY, project.getName()));
        }
        for (final File lib : model.getLibs()) {
            boolean libAlreadyAdded = false;
            for (final Project project : model.getProjects()) {
                final Jar jar = (Jar) project.getTasks().getByName(JavaPlugin.JAR_TASK_NAME);
                if (lib.equals(jar.getArchivePath())) {
                    log.info("Libs dropped : " + lib);
                    libAlreadyAdded = true;
                }
            }
            if (!libAlreadyAdded) {
                addListEntry(String.format(JARENTRY, lib.getAbsolutePath()));
            }
        }

        w.endElement();

    }

    protected void addListEntry(final String value) throws IOException {
        w.startElement("listEntry");
        w.attribute("value", value);
        w.endElement();
    }

    protected void addStringAttribute(final String key, final String value) throws IOException {
        w.startElement("stringAttribute");
        w.attribute("key", key);
        w.attribute("value", value);
        w.endElement();
    }

    protected void addMapEntry(final String key, final String value) throws IOException {
        w.startElement("mapEntry");
        w.attribute("key", key);
        w.attribute("value", value);
        w.endElement();
    }

    protected void addBooleanAttribute(final String key, final Boolean value) throws IOException {
        w.startElement("booleanAttribute");
        w.attribute("key", key);
        w.attribute("value", value.toString());
        w.endElement();
    }

    protected void addEnvironmentVariables() throws IOException {
        w.startElement("mapAttribute");
        w.attribute("key", "org.eclipse.debug.core.environmentVariables");

        for (final Entry<String, String> envVar : model.getEnvironmentVariables().entrySet()) {
            addMapEntry(envVar.getKey(), envVar.getValue());
        }
        w.endElement();
    }
}
