/*============================================================================
 *
 * Copyright (c) 2000-2012 Smart Trade Technologies. All Rights Reserved.
 *
 * This software is the proprietary information of Smart Trade Technologies
 * Use is subject to license terms. Duplication or distribution prohibited.
 *
 *============================================================================*/

package com.smarttrade.builders.task.eclipse;

import java.io.IOException;

/**
 * @author mbarbier
 */
public class EclipseJUnitLauncherGenerator extends AbstractEclispeLauncherGenerator {

    @Override
    protected void writeLauncher() throws IOException {
        w.startElement("launchConfiguration");
        w.attribute("type", "org.eclipse.jdt.junit.launchconfig");

        addCoveragePaths(model.getProjectName() + "/src/main/java");

        final String mappedResource = model.getMain().replace('.', '/');
//        addResourcePaths(model.getProjectName() + model.getMapperResourceRelativePath() + mappedResource + ".java");
//        addResourceTypes("1");

        if (model.getEnvironmentVariables().isEmpty()) {
            addBooleanAttribute("org.eclipse.debug.core.appendEnvironmentVariables", true);
        } else {
            addEnvironmentVariables();
        }
        addStringAttribute("org.eclipse.jdt.junit.CONTAINER", "");
        addBooleanAttribute("org.eclipse.jdt.junit.KEEPRUNNING_ATTR", false);
        addStringAttribute("org.eclipse.jdt.junit.TESTNAME", "");
        addStringAttribute("org.eclipse.jdt.junit.TEST_KIND", "org.eclipse.jdt.junit.loader.junit4");

        addResourceClasspath();

        addBooleanAttribute("org.eclipse.jdt.launching.DEFAULT_CLASSPATH", false);
        addStringAttribute("org.eclipse.jdt.launching.MAIN_TYPE", model.getMain());
        addStringAttribute("org.eclipse.jdt.launching.PROJECT_ATTR", model.getProjectName());

        if (model.getVmargs() != null) {
            addStringAttribute("org.eclipse.jdt.launching.VM_ARGUMENTS", model.getVmargs());
        }

        w.endElement();
    }

}
