package com.smarttrade.test;

import java.io.File;

import org.gradle.api.internal.AbstractTask;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.TaskAction;

@CacheableTask
public class MyTask extends AbstractTask {
	@InputFile
	public File in = getProject().file("./src/main/in");
	public File out = getProject().file("./src/main/out");
	public String txt = "hello";

	@TaskAction
	public void execute1() {
		System.out.println("In: " + in.getAbsolutePath());
		System.out.println("Out: " + out.getAbsolutePath());
		System.out.println("Txt: " + txt);
		System.out.println("Hello");
	}
}
