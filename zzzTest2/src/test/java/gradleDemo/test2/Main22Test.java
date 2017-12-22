package gradleDemo.test2;

import java.lang.management.ManagementFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import gradleDemo.test2.Main;

@RunWith(JUnit4.class)
public class Main22Test {

	@Test
	public void testMain22() throws InterruptedException {
		System.out.println("@@@testMain22#id: " + System.getProperty("org.gradle.test.worker") + " / "
				+ ManagementFactory.getRuntimeMXBean().getName());
		Main.main(null);
		Main2Generated.main(null);
		Thread.sleep(5000);
	}
}
