package gradleDemo.test1;

public class Main11 {
	public static void main(String[] args) {
		System.out.println("Hello Gradle from zzzTest1");
		
		for (int i = 0; i < 1; i++) {
			String[] s = new String[1000000];
		}
		try {
			Class.forName("gradleDemo.test1.Test1TestSuite");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
