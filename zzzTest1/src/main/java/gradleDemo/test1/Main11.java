package gradleDemo.test1;

import java.lang.management.ManagementFactory;

public class Main11 {

	public static String message = "Hello11";

	public static void main(String[] args) {
		System.out.println("Hello zzzTest1#Main11");
		System.out.println(ManagementFactory.getRuntimeMXBean().getName());
		message += " Main11 !";
	}
}
