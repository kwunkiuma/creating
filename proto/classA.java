public class classA {
	public static void main(String[] args) {
		for (int n=1; n<100; n++) {
		
			String message = "";
			if (n % 3 == 0) {
				message = message + "fizz ";
			}
			if (n % 5 == 0) {
				message = message + "buzz";
			}
			if (message.equals("")) {
				message = message + n;
			}
			System.out.println(message);
		}
	}

	public void methodB() {
	}

	public void methodC() {
	}

	public void methodD() {
	}
}