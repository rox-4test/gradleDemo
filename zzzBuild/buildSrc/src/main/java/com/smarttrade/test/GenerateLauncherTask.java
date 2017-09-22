package com.smarttrade.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.gradle.api.Project;
import org.gradle.api.internal.AbstractTask;
import org.gradle.api.internal.TaskInputsInternal;
import org.gradle.api.internal.TaskOutputsInternal;
import org.gradle.api.internal.tasks.testing.TestClassProcessor;
import org.gradle.api.internal.tasks.testing.TestClassRunInfo;
import org.gradle.api.internal.tasks.testing.TestResultProcessor;
import org.gradle.api.internal.tasks.testing.detection.TestFrameworkDetector;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.testing.Test;

import com.smarttrade.builders.task.eclipse.EclipseJUnitLauncherGenerator;
import com.smarttrade.builders.task.eclipse.model.LauncherModel;

public class GenerateLauncherTask extends AbstractTask{
	
	private Test testTask = null;
	private boolean isInitialized = false;
	private ArrayList<String> suites = new ArrayList<String>();
	
	private boolean useGradleClasspath = false;	
	
	public void testTask(Test testTask) {
		this.testTask = testTask;
		initDependsOn();		
	}	
	
	public GenerateLauncherTask() {
		getProject().afterEvaluate(project -> {
			if(!isInitialized) {
				isInitialized = true;
				OutputTestClassProcessor testClassDetector = new OutputTestClassProcessor(getOutputs());
				for(File suite : testTask.getCandidateClassFiles().getFiles()) {
					TestFrameworkDetector testFrameworkDetector = testTask.getTestFramework().getDetector();
					testFrameworkDetector.setTestClasses(testTask.getTestClassesDirs().getFiles());
		            testFrameworkDetector.setTestClasspath(testTask.getClasspath().getFiles());
		            testFrameworkDetector.startDetection(testClassDetector);
					testFrameworkDetector.processTestClass(suite);
				}
			}
		});
	}
	
	private class OutputTestClassProcessor implements TestClassProcessor {

		private TaskOutputsInternal outputs = null;
		
		public OutputTestClassProcessor(TaskOutputsInternal outputs) {
			this.outputs = outputs;
		}
		
		@Override
		public void processTestClass(TestClassRunInfo paramTestClassRunInfo) {
			String className = paramTestClassRunInfo.getTestClassName();
			suites.add(className);			
			outputs.file(getOutput(className));			
		}

		@Override
		public void startProcessing(TestResultProcessor paramTestResultProcessor) {			
		}

		@Override
		public void stop() {			
		}
		
	}

//	@Override
//	public TaskInputsInternal getInputs() {
//		TaskInputsInternal inputs = super.getInputs();
//		if(testTask != null) {
//			for(File f : testTask.getInputs().getFiles()) {
//				inputs.file(f);
//			}
//			inputs.property("jvmargs", testTask.getAllJvmArgs());
//			inputs.property("useGradleClasspath", useGradleClasspath);
//		}	
//		return inputs;
//	}
	
	private void initDependsOn() {
		if(testTask != null /*&& useGradleClasspath*/) {
			this.dependsOn(testTask.getDependsOn());
		}
	}
	
	/**
	 * this should only be used for debug purpose
	 */
	public boolean useGradleClasspath(boolean useGradleClasspath) {
		this.useGradleClasspath = useGradleClasspath;
		initDependsOn();
		return useGradleClasspath;
	}
	
	@TaskAction
	public void action () {
		if(testTask == null) {
			getLogger().error("testTask must not be null");
			return;
		}
		
		if(testTask.getCandidateClassFiles().getFiles().size() < 1) {
			getLogger().warn("No test suite classes for this project");
			return;
		}
		
		for(String suite : suites) {
		
			EclipseJUnitLauncherGenerator generator = new EclipseJUnitLauncherGenerator();
			LauncherModel lm = new LauncherModel();
			
			for(String arg : testTask.getAllJvmArgs())
				lm.setVmargs(arg);
			
			lm.setMain(suite);
			lm.setProjectName(testTask.getProject().getName());
			
			lm.setName(testTask.getName());
			lm.setWorkingDirectory(testTask.getProject().getProjectDir().toString());
			
			if(!useGradleClasspath) {
				buildEclipseClasspath(lm);
			} else {
				buildGradleClasspath(lm);
			}		
			
			generator.setModel(lm);
			generator.setOutput(getOutput(suite));
			generator.generate();
		}
	}
	
	private void buildGradleClasspath(LauncherModel lm) {
		for( File f : testTask.getClasspath()) {
			if(f.exists()) {
				if(f.isDirectory()) {				
					lm.addResource(f.toString());
				} else {
					lm.addLib(f);								
				}
			}
		}
		
	}

	private void buildEclipseClasspath(LauncherModel lm) {
		for( File f : testTask.getClasspath()) {
			if(f.isDirectory()) {
				//in case the directory is in the output directory, we should rather use current eclipse output dir (ie add project to classpath)
				if( isInDir(f, testTask.getProject().getBuildDir()) ) {
					lm.addProject(testTask.getProject());
				} else {					
					lm.addResource(f.toString());
				}
			} else {
				//if it is a file built from another project, we use the workspace project
				Project workspaceProject = null;
				for(Project p : getProject().getRootProject().getAllprojects()) {						
					if(isInDir(f, p.getBuildDir())) {
						workspaceProject = p;
					}
				}
				
				if( workspaceProject != null ) {
					lm.addProject(workspaceProject);
				}else {
					lm.addLib(f);		
				}					
						
			}
		}		
	}
	
	private File getOutput(String className){
		className = className.substring(className.lastIndexOf('.')+1, className.length());
		File outputDir = getProject().getBuildDir().toPath().resolve("launchers").toFile();
		return outputDir.toPath().resolve(testTask.getName() +"_" + className + ".launch").toFile();
		
	}
	
	private boolean isInDir(File file, File dir) {
		String canonicalDir;
		try {
			canonicalDir = dir.getCanonicalPath() + File.separator;
			boolean isInDir = file.getCanonicalPath().startsWith(canonicalDir);
			return isInDir;
		} catch (IOException e) {			
			getLogger().error("unable to find if {} is in {}", file, dir);
			getLogger().error("catched error :", e);
		}
		
		return false;
	}
}
