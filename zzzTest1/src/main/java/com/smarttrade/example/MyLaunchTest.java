
package com.smarttrade.example;

import java.io.File;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.DemuxOutputStream;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Echo;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Environment.Variable;
import org.apache.tools.ant.types.Path;

public class MyLaunchTest {

	public static void main(String[] args) {

		final Project project = new Project();
		project.setBaseDir(new File(System.getProperty("user.dir")));
		project.init();
		final DefaultLogger logger = new DefaultLogger();
		project.addBuildListener(logger);
		logger.setOutputPrintStream(System.out);
		logger.setErrorPrintStream(System.err);
		logger.setMessageOutputLevel(Project.MSG_INFO);
		System.setOut(new PrintStream(new DemuxOutputStream(project, false)));
		System.setErr(new PrintStream(new DemuxOutputStream(project, true)));
		project.fireBuildStarted();

		System.out.println("[" + ManagementFactory.getRuntimeMXBean().getName() + "]" + "running");
		Throwable caught = null;
		try {
			final Echo echo = new Echo();
			echo.setTaskName("Echo");
			echo.setProject(project);
			echo.init();
			echo.setMessage("Launching Some Class");
			echo.execute();

			final Java javaTask = new Java();
			javaTask.setTaskName("runjava");
			javaTask.setProject(project);
			javaTask.setFork(true);
			final Variable variable = new Variable();
			variable.setKey("JAVA_HOME");
			variable.setValue("C:/");

			final Variable variable2 = new Variable();
			variable2.setKey("hehe");
			variable2.setValue("HEHEHAHA");
			javaTask.addEnv(variable);
			javaTask.addSysproperty(variable2);
			javaTask.setFailonerror(true);
			javaTask.setClassname(MainClass.class.getName());
			final Path path = new Path(project,
					new File("C:\\Repos\\git\\BUILDERS\\STPlugin\\develop\\stplugin\\bin\\main").getAbsolutePath());
			javaTask.setClasspath(path);
			javaTask.init();
			final CommandlineJava commandLine = javaTask.getCommandLine();
			System.out.println(commandLine.describeCommand());
			System.out.println();
			System.out.println(commandLine);
			final int ret = javaTask.executeJava();
			System.out.println(
					"[" + ManagementFactory.getRuntimeMXBean().getName() + "]" + "java task return code: " + ret);
		} catch (final BuildException e) {
			caught = e;
		}
		project.log("finished");
		project.fireBuildFinished(caught);
	}
}