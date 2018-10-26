import javax.swing.JFrame;

class Proto {
	
	public static void main(String[] args) {
		
		MyWindow window = new MyWindow();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(200,100);
		window.setVisible(true);
		
	}
}