import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;
import javax.swing.tree.*;

public class Program extends JFrame {
	
	JSplitPane navigation;
	
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
	
	private initTree() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
		JTree tree = new JTree(root);
	}
	
	// Initialise window structure
	private init() {
		
		navigation = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tree, table);
		
	}
	
}

class ClassTree extends JTree {
	
	public ClassTree() {
		super();
		setRootVisible(false);
	}
	
	private 
}