import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;
import javax.swing.tree.*;

public class Program extends JFrame {
	
	JMenuBar menuBar;
	JMenu
		;
	JMenuItem menuItem;
	JSplitPane
		classMethodSplit,
		classSplit,
		methodPropertySplit,
		centreSplit;
	JScrollPane scrollPane;
	JPanel
		panel,
		methodProperties = new JPanel(),
		boxes = new JPanel(),
		table;
	JLabel label = new JLabel("Test");
	DefaultMutableTreeNode top = new DefaultMutableTreeNode("Top of tree");
	ClassTree classTree;
	
	public Program() {
		super();
		
		// Initialise menu bar
		menuBar = new JMenuBar();
		menu = new JMenu("Menu");
		menuBar.add(menu);
		menuItem = new JMenuItem("Menu item");
		menu.add(menuItem);
		this.setJMenuBar(menuBar);
		
		// Initialise tree panel
		panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		scrollPane = new JScrollPane();
		classTree = new ClassTree(panel);
		
		// Populate table with sample data
		for (int i = 0; i < 30; i++) {
			DefaultMutableTreeNode temp = classTree.addObject(classTree.root, Integer.toString(i));
			if (i == 5) {
				classTree.addObject(temp, "Child");
			}
		}
		
		
		// Initialise table
		table = new JPanel();
		table.setLayout(new GridLayout(0,1));
		MethodTable methodTable = new MethodTable(table);
		
		// Setting up split panes
		classMethodSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panel, table);
		classSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, classMethodSplit, label);
		methodPropertySplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, methodProperties, boxes);
		centreSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, classSplit, methodPropertySplit);
		
		add(centreSplit);
	}
	
}

class MethodTable extends JPanel {
	
// 	DefaultTableModel model;
	JTable table;
	
	public MethodTable(Container container) {
		super();
		String[] columns = {
			"Name",
			"Returns"
		};
		Object[][] data = {
			{"Remove", "void"},
			{"Add", "Integer"}
		};
		table = new JTable(data, columns);
		JScrollPane scrollPane = new JScrollPane(table);
		container.add(scrollPane);
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
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);
		tree.setRootVisible(false);
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
			if (parent != null) {
				model.removeNodeFromParent(currentNode);
				return;
			}
		}
	}
	
	public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child) {
		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
		if (parent == null) {
			parent = root;
		}
		model.insertNodeInto(childNode, parent, parent.getChildCount());
		tree.scrollPathToVisible(new TreePath(childNode.getPath()));
		return childNode;
	}