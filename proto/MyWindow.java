import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;
import javax.swing.tree.*;

public class MyWindow extends JFrame {
	
	JSplitPane splitPane;
	MyTree tree;
	JLabel label = new JLabel("Don't");
	DefaultMutableTreeNode top = new DefaultMutableTreeNode("Top of tree");
	
	public MyWindow() {
		super();
		tree = new MyTree(top);
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, label, tree);
		add(splitPane);
		DefaultMutableTreeNode category = null;
		category = new DefaultMutableTreeNode("Test");
		top.add(category);
	}
	
}

class MyTree extends JTree {
	
	public MyTree(DefaultMutableTreeNode arg) {
		super(arg);
		
	}
	
}