package gradleDemo.test1;

import java.lang.management.ManagementFactory;

public class Main1 {

	public static String message = "Hello1";

	public static void main(String[] args) {
		System.out.println("Hello from zzzTest1#Main1");
		System.out.println(ManagementFactory.getRuntimeMXBean().getName());
		message += " Main1 !";
	}
}
