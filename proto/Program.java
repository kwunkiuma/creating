import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;
import javax.swing.tree.*;
import javax.swing.event.*;

public class Program extends JFrame {
	
	JMenuBar menuBar;
	JMenu
		fileMenu,
		classMenu,
		methodMenu;
	JMenuItem
		fileNew,
		fileOpen,
		classNew,
		classRemove,
		methodNew,
		methodRemove;
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
		
		// File menu
		fileMenu = new JMenu("File");
		fileNew = new JMenuItem("New...");
		fileOpen = new JMenuItem("Open...");
		menuBar.add(fileMenu);
		fileMenu.add(fileNew);
		fileMenu.add(fileOpen);
		
		// Class menu
		classMenu = new JMenu("Class");
		classNew = new JMenuItem("New");
		classRemove = new JMenuItem("Remove");
		menuBar.add(classMenu);
		classMenu.add(classNew);
		classMenu.add(classRemove);
		
		// Method menu
		methodMenu = new JMenu("Method");
		methodNew = new JMenuItem("New");
		methodRemove = new JMenuItem("Remove");
		menuBar.add(methodMenu);
		methodMenu.add(methodNew);
		methodMenu.add(methodRemove);
		
		this.setJMenuBar(menuBar);
		
		// Initialise tree panel
		panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		scrollPane = new JScrollPane();
		classTree = new ClassTree(panel);
		
		// Populate table with sample data
		for (int i = 0; i < 10; i++) {
			DefaultMutableTreeNode temp = classTree.addObject(classTree.root, Integer.toString(i));
			if (i == 5) {
				classTree.addObject(temp, "Child");
			}
		}
		
		// Initialise table
		table = new JPanel();
		table.setLayout(new GridLayout(0,1));
		PropertiesTable propertiesTable = new PropertiesTable(table);
		
		// Setting up split panes
		classMethodSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, panel, table);
		classSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, classMethodSplit, label);
		methodPropertySplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, methodProperties, boxes);
		centreSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, classSplit, methodPropertySplit);
		
		add(centreSplit);
	}
	
	class ClassTree extends JPanel implements TreeSelectionListener {
		
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
			tree.addTreeSelectionListener(this);
			tree.setShowsRootHandles(true);
			tree.setRootVisible(false);
			tree.addTreeSelectionListener(this);
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
		
		public void valueChanged(TreeSelectionEvent e) {
			System.out.println(e.getPath().getLastPathComponent());
			label.setText(e.getPath().getLastPathComponent().toString());
		}
	}

}

class PropertiesTable extends JPanel {
	
// 	DefaultTableModel model;
	JTable table;
	String[] classNames = {"One", "Two", "Three"};
// 	JComboBox classes;
	public PropertiesTable(Container container) {
		super();
// 		classes.setEditable(true);
		String[] columns = {
			"Property",
			"Value"
		};
		Object[][] data = {
			{"Extends", "void"},
			{"Access modifier", "Tes"}
		};
		table = new JTable(data, columns);
		JScrollPane scrollPane = new JScrollPane(table);
		container.add(scrollPane);
	}
	
}

