package gradleDemo.test2;

import java.lang.management.ManagementFactory;

public class Main {
	public static void main(String[] args) {
		System.out.println("Hello Gradle from zzzTest2#Main: " + System.getProperty("org.gradle.test.worker"));
		System.out.println(ManagementFactory.getRuntimeMXBean().getName());

		System.err.println("zzzTest1#Main1: message = " + gradleDemo.test1.Main1.message);
		System.err.println("zzzTest1#Main11: message = " + gradleDemo.test1.Main11.message);
		System.err.println("zzzTest1#Main1b: message = " + gradleDemo.test1b.Main1b.message);
	}
}
