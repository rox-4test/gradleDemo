package gradleDemo.test1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class Main1Test {

	@Test
	public void testMain1() {
		System.out.println("@@@testMain1!!!!");
		Main1.main(null);
//		throw new RuntimeException("FAILED");
	}
}
