package gradleDemo.test3;

import java.lang.management.ManagementFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MainTest {

	@Test
	public void testMain3() throws InterruptedException {
		System.out.println("@@@testMain3#id: " + System.getProperty("org.gradle.test.worker") + " / "
				+ ManagementFactory.getRuntimeMXBean().getName());
		Main.main(null);
//		Thread.sleep(5000);
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		System.out.println(contextClassLoader);
	}
}
