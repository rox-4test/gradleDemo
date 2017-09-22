package com.smarttrade.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.testing.Test;

public class CoreTest extends Test {

	public Configuration aspectjweaver = null;

	public String filePrefix = "";
	public boolean logClasspath = true;
	private String testClass;

	public CoreTest() {
		super();

		workingDir(getProject().getProjectDir());
		getOutputs().upToDateWhen(CoreTest::isUpToDate);
		getProject().afterEvaluate(project -> {
			System.err.println("@Project: " + project.getName());
			System.err.println("@BuildDir: " + project.getBuildDir());
			if (logClasspath)
				doFirst(new DoFirstLog());
			jvmArgs(getArgs());
			for (String f : getIncludes()) {
				String clazz = f.replace("/", ".").replaceAll(".class", "");
				try {
					Class.forName(clazz);
					System.err.println("@@@Clazz: " + clazz);
				} catch (ClassNotFoundException e) {
					System.err.println("Class not found: " + clazz);
				}
			}
			// include(testClass);
			System.err.println("@@@: " + getIncludes());
		});

	}

	public void testclass(String testClass) {
		System.err.println("testClass: " + testClass);
		this.testClass = testClass;
	}

	public void mainSourceSet(boolean fromMainSourceSet) {
		if (fromMainSourceSet) {
			SourceSetContainer srcSetContainer = (SourceSetContainer) getProject().findProperty("sourceSets");
			setTestClassesDir(srcSetContainer.getByName("main").getOutput().getClassesDir());
		}
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
