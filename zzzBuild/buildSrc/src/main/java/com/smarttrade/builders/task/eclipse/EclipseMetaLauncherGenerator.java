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
import java.util.ArrayList;
import java.util.List;

/**
 * @author acoulon
 */
public class EclipseMetaLauncherGenerator extends AbstractEclispeLauncherGenerator {

    private List<String> launchers = new ArrayList<>();

    public void setLaunchers(List<String> launchers) {
        this.launchers = launchers;
    }

    @Override
    protected void writeLauncher() throws IOException {
        w.startElement("launchConfiguration");
        w.attribute("type", "org.eclipse.cdt.launch.launchGroup");
        int index = 0;
        for (final String launcher : launchers) {
            addStringAttribute("org.eclipse.cdt.launch.launchGroup." + index + ".action", "WAIT_FOR_TERMINATION");
            addStringAttribute("org.eclipse.cdt.launch.launchGroup." + index + ".enabled", "true");
            addStringAttribute("org.eclipse.cdt.launch.launchGroup." + index + ".mode", "run");
            addStringAttribute("org.eclipse.cdt.launch.launchGroup." + index + ".name", launcher);
            index++;
        }
        w.endElement();
    }

}
