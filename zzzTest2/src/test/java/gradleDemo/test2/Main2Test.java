package gradleDemo.test2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import gradleDemo.test2.Main;

@RunWith(JUnit4.class)
public class Main2Test {

	@Test
	public void testMain2() {
		System.out.println("@@@testMain2!!!!");
		Main.main(null);
//		throw new RuntimeException("FAILED");
	}
}
