package gradleDemo.test1;

import java.lang.management.ManagementFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class Main11Test {

	@Test
	public void testMain11() throws InterruptedException {
		System.out.println("@@@testMain11#id: " + System.getProperty("org.gradle.test.worker") + " / "
				+ ManagementFactory.getRuntimeMXBean().getName());
		Main11.main(null);
		Main1Generated.main(null);
		Main1TestGenerated.main(null);
//		Thread.sleep(5000);
	}
}
