
package com.smarttrade.example;

import java.lang.management.ManagementFactory;

public class MainClass {

	public static void main(String[] args) {
		System.out.println("[" + ManagementFactory.getRuntimeMXBean().getName() + "]" + "MainClass.main()");
		System.out.println(System.getenv("JAVA_HOME"));
		System.out.println(System.getProperty("hehe"));
	}
}
