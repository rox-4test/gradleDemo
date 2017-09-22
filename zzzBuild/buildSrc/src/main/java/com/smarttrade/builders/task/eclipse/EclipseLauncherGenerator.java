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
public class EclipseLauncherGenerator extends AbstractEclispeLauncherGenerator {

    @Override
    protected void writeLauncher() throws IOException {
        w.startElement("launchConfiguration");
        w.attribute("type", "org.eclipse.jdt.launching.localJavaApplication");
        addBooleanAttribute("org.eclipse.debug.core.appendEnvironmentVariables", true);
        addBooleanAttribute("org.eclipse.jdt.launching.DEFAULT_CLASSPATH", false);
        addStringAttribute("org.eclipse.debug.core.source_locator_id",
            "org.eclipse.jdt.launching.sourceLocator.JavaSourceLookupDirector");

        addResourcePaths();
        addResourceTypes();
        addResourceClasspath();
        if (model.getEnvironmentVariables().isEmpty()) {
            addBooleanAttribute("org.eclipse.debug.core.appendEnvironmentVariables", true);
        } else {
            addEnvironmentVariables();
        }

        addStringAttribute("org.eclipse.jdt.launching.PROJECT_ATTR", model.getProjectName());
        addStringAttribute("org.eclipse.jdt.launching.MAIN_TYPE", model.getMain());

        if (model.getArgs() != null) {
            addStringAttribute("org.eclipse.jdt.launching.PROGRAM_ARGUMENTS", model.getArgs());
        }
        if (model.getVmargs() != null) {
            addStringAttribute("org.eclipse.jdt.launching.VM_ARGUMENTS", model.getVmargs());
        }
        if (model.getWorkingDirectory() != null) {
            addStringAttribute("org.eclipse.jdt.launching.WORKING_DIRECTORY", "${workspace_loc:" + model.getWorkingDirectory() + "}");
        }

        w.endElement();
    }

}
