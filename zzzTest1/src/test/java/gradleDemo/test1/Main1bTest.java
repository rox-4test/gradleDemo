package gradleDemo.test1;

import java.lang.management.ManagementFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import gradleDemo.test1b.Main1b;

@RunWith(JUnit4.class)
public class Main1bTest {

	@Test
	public void testMain1b() throws InterruptedException {
		System.out.println("@@@testMain1b#id: " + System.getProperty("org.gradle.test.worker") + " / "
				+ ManagementFactory.getRuntimeMXBean().getName());
		Main1b.main(null);
//		Thread.sleep(10000);
	}
}
