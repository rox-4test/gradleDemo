package gradleDemo.test1;

public class Main1 {
	public static void main(String[] args) {
		System.out.println("Hello Gradle from zzzTest1");

		try {
			Class.forName("gradleDemo.test1.Test1TestSuite");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
