package com.smarttrade.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.testing.Test;

public class CoreTest extends Test {

	public Configuration aspectjweaver = null;

	public String filePrefix = "";
	public boolean logClasspath = true;
	private boolean skipIfNoTestFound = true;

	public CoreTest() {
		super();

		workingDir(getProject().getProjectDir());
		getOutputs().upToDateWhen(CoreTest::isUpToDate);
		getProject().afterEvaluate(project -> {
			System.err.println("@Project: " + project.getName());
			System.err.println("@@@getIncludes: " + getIncludes());
			System.err.println("@@@getCandidateClassFiles: " + getCandidateClassFiles().getFiles());
			if (logClasspath)
				doFirst(new DoFirstLog());
			jvmArgs(getArgs());
			if (getCandidateClassFiles().getFiles().size() < 1) {
				String warnMessage = "No test class found in includes list for test: " + project.getName() + ":"
						+ getName() + ". Includes list: " + getIncludes();
				if (skipIfNoTestFound) {
					getLogger().warn("[WARN] " + warnMessage);
					return;
				} else {
					throw new RuntimeException(warnMessage);
				}
			}
		});

	}

	public void mainSourceSet(boolean fromMainSourceSet) {
		if (fromMainSourceSet) {
			SourceSetContainer srcSetContainer = (SourceSetContainer) getProject().findProperty("sourceSets");
			setTestClassesDir(srcSetContainer.getByName("main").getOutput().getClassesDir());
		}
	}

	public void setSkipIfNoTestFound(boolean skip) {
		skipIfNoTestFound = skip;
	}

	private static boolean isUpToDate(Task task) {
		return false;
	}

	private Iterable<String> getArgs() {
		Collection<String> c = new ArrayList<>();
		c.add("-ea");
		c.add("-server");
		c.add("-Xmx4g");
		c.add("-Dcom.sun.management.jmxremote");
		if (aspectjweaver != null)
			c.add("-javaagent:" + aspectjweaver.getSingleFile());
		c.add("-DlogFileName=" + getFileName());
		c.add("-XX:+UseConcMarkSweepGC");
		c.add("-verbose:gc");
		c.add("-Xloggc:" + getFileName() + "_gc.log");
		c.add("-XX:+PrintGCDateStamps");
		c.add("-XX:+PrintGCDetails");
		c.add("-XX:+PrintGCTimeStamps");
		return c;
	}

	private String getFileName() {
		return filePrefix + getName();
	}

	public class DoFirstLog implements Action<Task> {
		@Override
		public void execute(Task task) {
			task.getLogger().quiet("JvmArgs :" + ((Test) task).getAllJvmArgs().toString());
			task.getLogger().quiet("Classpath :");
			for (File dep : ((Test) task).getClasspath()) {
				task.getLogger().quiet(dep.toString());
			}

		}
	}
}
