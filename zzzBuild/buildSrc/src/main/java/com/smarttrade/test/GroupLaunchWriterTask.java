package com.smarttrade.test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.gradle.api.UncheckedIOException;
import org.gradle.api.internal.AbstractTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.internal.xml.SimpleXmlWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroupLaunchWriterTask extends AbstractTask{

    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    public File output;
    protected SimpleXmlWriter w;
	
    protected ArrayList<String> launchers = new ArrayList<String>();
    
    public void launcher(String launcher) {
    	launchers.add(launcher);
    }
    
    @TaskAction
    public void generate() {
        log.info("Generating grouplaunch #" + output.getName());

        try {
            output.getParentFile().mkdirs();
            final OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(output));
            try {
                w = new SimpleXmlWriter(outputStream, "  ");
                writeGroupLaunch();
                w.flush();
            } finally {
                outputStream.close();
            }
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }

        log.info("grouplaunch generated");
    }


	private void writeGroupLaunch()  throws IOException {
		w.startElement("launchConfiguration");
		w.attribute("type", "org.eclipse.debug.core.groups.GroupLaunchConfigurationType");
		w.startElement("stringAttribute");
		w.attribute("key", "");
		w.attribute("value", "");
		w.endElement();
		for(int i = 0; i < launchers.size(); i ++) {
			writeLauncherEntry(i, launchers.get(i));
		}
		w.endElement();
	}


	private void writeLauncherEntry(int i, String launcher)  throws IOException {
		w.startElement("stringAttribute");
		w.attribute("key", "org.eclipse.debug.core.launchGroup."+ String.valueOf(i) +".action");
		w.attribute("value", "WAIT_FOR_TERMINATION");
		w.endElement();
		
		w.startElement("booleanAttribute");	
		w.attribute("key", "org.eclipse.debug.core.launchGroup."+ String.valueOf(i) +".adoptIfRunning");
		w.attribute("value", "false");
		w.endElement();
		
		w.startElement("booleanAttribute");	
		w.attribute("key", "org.eclipse.debug.core.launchGroup."+ String.valueOf(i) +".enabled");
		w.attribute("value", "true");
		w.endElement();
		
		w.startElement("stringAttribute");	
		w.attribute("key", "org.eclipse.debug.core.launchGroup."+ String.valueOf(i) +".mode");
		w.attribute("value", "inherit");
		w.endElement();
		
		w.startElement("stringAttribute");	
		w.attribute("key", "org.eclipse.debug.core.launchGroup."+ String.valueOf(i) +".name");
		w.attribute("value", launcher);
		w.endElement();
	}
}
