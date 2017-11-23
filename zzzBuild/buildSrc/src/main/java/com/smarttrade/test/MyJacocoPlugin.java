package com.smarttrade.test;

import java.io.File;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.ConfigurableFileTree;
import org.gradle.testing.jacoco.plugins.JacocoPlugin;
import org.gradle.testing.jacoco.tasks.JacocoMerge;

public class MyJacocoPlugin implements Plugin<Project> {
	public void apply(Project project) {
		project.getPlugins().apply(JacocoPlugin.class);

		final Project rootProject = project.getRootProject();
		
		if (rootProject == project) return;
		final JacocoMerge jacocoMergeTask = project.getTasks().create("jacocoMerge", JacocoMerge.class);
		final File destinationFile = new File(rootProject.getBuildDir().getAbsolutePath() + "/jacoco/jacocoMerge.exec");
		if (!destinationFile.getParentFile().exists()) {
			destinationFile.getParentFile().mkdirs();
		}

		final ConfigurableFileTree fileTree = project.fileTree("build/jacoco");
//		fileTree.include("*.exec");
		System.out.println("@Files: " + fileTree.getFiles());
		jacocoMergeTask.setExecutionData(fileTree);
		jacocoMergeTask.setDestinationFile(destinationFile);
	}
}