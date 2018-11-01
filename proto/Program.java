import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;
import javax.swing.tree.*;

public class Program extends JFrame {
	
	JSplitPane navigation;
	
	JSplitPane splitPane;
	JPanel panel;
// 	ClassTree tree;
	JLabel label = new JLabel("Don't");
	DefaultMutableTreeNode top = new DefaultMutableTreeNode("Top of tree");
	ClassTree classTree;
	
	public Program() {
		super();
// 		tree = new MyTree(top);
		panel = new JPanel();
		classTree = new ClassTree(panel);
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel, label);
		add(splitPane);
		
	}
	
}

class ClassTree extends JPanel {
	
	DefaultMutableTreeNode root;
	DefaultTreeModel model;
	JTree tree;
	
	public ClassTree(Container container) {
		super();
		root = new DefaultMutableTreeNode("Root");
		model = new DefaultTreeModel(root);
		
		tree = new JTree(model);
		tree.setEditable(true);
// 		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);
		tree.setRootVisible(true);
		JScrollPane scrollPane = new JScrollPane(tree);
		container.add(scrollPane);
	}
	
	private void clear() {
		root.removeAllChildren();
		model.reload();
	}
	
	public void removeCurrentNode() {
		TreePath currentSelection = tree.getSelectionPath();
		if (currentSelection != null) {
			MutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
			MutableTreeNode parent = (MutableTreeNode) (currentNode.getParent());
// 			if (parent != null) {
// 				treeModel.removeNodeFromParent(currentNode);
// 				return;
// 			}
		}
	}
	
	public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child) {
		return addObject(parent, child, false);
	}
}