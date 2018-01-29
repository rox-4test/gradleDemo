package com.smarttrade.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.file.FileCollection;
import org.gradle.api.internal.AbstractTask;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.SourceSetOutput;
import org.gradle.api.tasks.TaskAction;
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
			validateTestClassesExistence(project, this);
			if (logClasspath)
				doFirst(new DoFirstLog());
			jvmArgs(getArgs());
		});
	}

	private void validateTestClassesExistence(Project project, CoreTest testTask) {
		TestClassesExistenceCheckTask checkTask = project.getTasks()
				.create("checkTestClassesExistence_" + testTask.getName(), TestClassesExistenceCheckTask.class);
		checkTask.testTask = testTask;
		checkTask.mustRunAfter(project.getTasks().getByName("testClasses"));
		testTask.dependsOn(checkTask);
	}

	public static class TestClassesExistenceCheckTask extends AbstractTask {

		CoreTest testTask;

		@TaskAction
		public void check() {
			if (testTask.getCandidateClassFiles().getFiles().size() < 1) {
				String warnMessage = "No test class found in includes list for test: " + testTask.getProject().getName()
						+ ":" + testTask.getName() + "\n+ Includes list: " + testTask.getIncludes()
						+ "\n+ Excludes list: " + testTask.getExcludes();

				if (testTask.skipIfNoTestFound) {
					getLogger().warn("[WARN] " + warnMessage);
					return;
				} else {
					throw new RuntimeException(warnMessage);
				}
			}
			getLogger().quiet("@Tests: " + testTask.getCandidateClassFiles().getFiles());
		}
	}

	public void mainSourceSet(boolean fromMainSourceSet) {
		if (fromMainSourceSet) {
			final SourceSetContainer srcSetContainer = (SourceSetContainer) getProject().findProperty("sourceSets");
			setTestClassesDirs(srcSetContainer.getByName("main").getOutput().getClassesDirs());
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
		
		/*
		 * Unrecognized VM option 'PrintGCDateStamps'
Error: Could not create the Java Virtual Machine.
Error: A fatal exception has occurred. Program will exit.
[0.001s][warning][gc] -Xloggc is deprecated. Will use -Xlog:gc:testU_zzzTest1_gc.log instead.
Java HotSpot(TM) 64-Bit Server VM warning: Option UseConcMarkSweepGC was deprecated in version 9.0 and will likely be removed in a future release.
Unrecognized VM option 'PrintGCDateStamps'
Error: Could not create the Java Virtual Machine.
Error: A fatal exception has occurred. Program will exit.
Process 'Gradle Test Executor 4' finished with non-zero exit value 1

Error: Could not create the Java Virtual Machine.
Error: A fatal exception has occurred. Program will exit.
[0.001s][warning][gc] -Xloggc is deprecated. Will use -Xlog:gc:testU_zzzTest1_gc.log instead.
Java HotSpot(TM) 64-Bit Server VM warning: Option UseConcMarkSweepGC was deprecated in version 9.0 and will likely be removed in a future release.
Unrecognized VM option 'PrintGCTimeStamps'
Error: Could not create the Java Virtual Machine.
Error: A fatal exception has occurred. Program will exit.
Process 'Gradle Test Executor 3' finished with non-zero exit value 1


*/
//		c.add("-XX:+PrintGCDateStamps");
//		c.add("-XX:+PrintGCTimeStamps");
		
		/*
		[0.001s][warning][gc] -Xloggc is deprecated. Will use -Xlog:gc:testU_zzzTest2_gc.log instead.
		[0.001s][warning][gc] -XX:+PrintGCDetails is deprecated. Will use -Xlog:gc* instead.
		[0.014s][info   ][gc] Using Concurrent Mark Sweep
		Java HotSpot(TM) 64-Bit Server VM warning: Option UseConcMarkSweepGC was deprecated in version 9.0 and will likely be removed in a future release.
		*/
		c.add("-XX:+PrintGCDetails");
		return c;
	}

	private String getFileName() {
		return filePrefix + getName();
	}

	public class DoFirstLog implements Action<Task> {
		@Override
		public void execute(Task task) {
			System.out.println("@@@DoFirst: " + task);
			task.getLogger().quiet("JvmArgs :" + ((Test) task).getAllJvmArgs().toString());
			task.getLogger().quiet("Classpath :");
			for (File dep : ((Test) task).getClasspath()) {
				task.getLogger().quiet(dep.toString());
			}
		}
	}
}
