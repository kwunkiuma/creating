// import java.awt.FlowLayout;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;
// import java.awt.event.ActionListener;
// import java.awt.event.ActionEvent;
// import java.awt.event.MouseMotionListener;
// import java.awt.event.*
// import javax.swing.JFrame;
// import javax.swing.JLabel;
// import javax.swing.JTextField;


class MyWindow extends JFrame implements MouseListener {
	
	JLabel x;
	JLabel y;
	JButton button;
	
	public MyWindow() {
		super("Title");
		
		addMouseListener(this);
		
		setLayout(new FlowLayout());
		
		JLabel label = new JLabel("Words here");
		JTextField textField = new JTextField("Test");
// 		add(label);
// 		add(textField);
		x = new JLabel("x");
		y = new JLabel("y");
		button = new JButton("Button");
		add(x);
		add(y);
		add(button);
		MyButtonListener butLis = new MyButtonListener(button);
		
		MyHandler handler = new MyHandler();
		
		textField.addActionListener(handler);
	}
	
	public void mouseClicked(MouseEvent e) {
		x.setText(Integer.toString(e.getX()));
	}
	
	public void mouseEntered(MouseEvent e) {
		System.out.println("Ahh");
		if (e.getSource() == y) {
			y.setText("Hello");
		}
	}
	
	public void mouseExited(MouseEvent e) {
		
	}
	
	public void mousePressed(MouseEvent e) {
		
	}
	
	public void mouseReleased(MouseEvent e) {
		
	}
	
	private class MyHandler implements ActionListener {
		
		public void actionPerformed(ActionEvent event) {
			
		}
		
	}
	
	private class MyButtonListener extends BasicButtonListener {
		public MyButtonListener(AbstractButton b) {
			super(b);
		}
		public void mouseEntered(MouseEvent e) {
			System.out.println("Ello");
			y.setText("Hey");
		}
	}
}