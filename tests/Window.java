import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JFrame;


class MyWindow extends JFrame {
	
	private JLabel item;
	
	public Words() {
		super("Title");
		setLayout(new FlowLayout());
		
		item = new JLabel("Words here");
		add(item);
	}
	
	private class MyHandler = 
}