package gradleDemo.test1b;

import java.lang.management.ManagementFactory;

public class Main1b {

	public static String message = "Hello1b";

	public static void main(String[] args) {
		System.out.println("Hello from zzzTest1#Main1b");
		System.out.println(ManagementFactory.getRuntimeMXBean().getName());
		message += " Main1b !";
	}
}
