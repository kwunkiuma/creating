import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;
import javax.swing.tree.*;
import javax.swing.event.*;

public class BK extends JFrame {
	
	JMenuBar
		menuBar;
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
		classPropertiesSplit,
		classSplit,
		methodPropertySplit,
		centreSplit;
	JScrollPane scrollPane;
	JPanel
		classPanel,
		methodProperties = new JPanel(),
		boxes,
		propertiesPanel;
	JLabel label = new JLabel("Test");
	DefaultMutableTreeNode top = new DefaultMutableTreeNode("Top of tree");
	ClassTree classTree;
	
	public BK() {
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
		classPanel = new JPanel();
		classPanel.setLayout(new GridLayout(0, 1));
		classTree = new ClassTree();
		classPanel.add(classTree);
		
		// Populate table with sample data
		for (int i = 0; i < 10; i++) {
			DefaultMutableTreeNode temp = classTree.addObject(classTree.root, Integer.toString(i));
			if (i == 4) {
				classTree.addObject(temp, "Child");
			}
		}
		
		// Initialise table
		propertiesPanel = new JPanel();
		propertiesPanel.setLayout(new GridLayout(0,1));
		PropertiesTable propertiesTable = new PropertiesTable();
		propertiesPanel.add(propertiesTable);
		
		// Setting up split panes
		classPropertiesSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, classPanel, propertiesPanel);
		classPropertiesSplit.setResizeWeight(0.5);
		
		boxes = new JPanel();
		classSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, classPropertiesSplit, boxes);
		classSplit.setResizeWeight(0);
		
		
		methodPropertySplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, methodProperties, label);
		
		
		methodPropertySplit.setResizeWeight(0.5);
		centreSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, classSplit, methodPropertySplit);
		centreSplit.setResizeWeight(1);
		
		add(centreSplit);
	}
	
	// Main method
	public static void main(String[] args) {
		BK window = new BK();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(1280, 720);
		window.setVisible(true);
	}
	
	class ClassTree extends JScrollPane implements TreeSelectionListener, MouseListener {
		
		DefaultMutableTreeNode root;
		DefaultTreeModel model;
		JTree tree;
		
		public ClassTree() {
			super();
			
			// Initialise tree properties
			root = new DefaultMutableTreeNode("Root");
			model = new DefaultTreeModel(root);
			tree = new JTree(model);
			tree.setEditable(true);
			tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			tree.addTreeSelectionListener(this);
			tree.setShowsRootHandles(true);
			tree.setRootVisible(false);
			tree.addTreeSelectionListener(this);
			tree.addMouseListener(this);
			this.setViewportView(tree);
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
			label.setText(e.getPath().getLastPathComponent().toString());
		}
		
		public void mouseClicked(MouseEvent e) {
			if (SwingUtilities.isRightMouseButton(e)) {
				int row = tree.getRowForLocation(e.getX(), e.getY());
				if (row == -1) { // Nothing clicked
					return;
				}
				tree.setSelectionRow(row);
				TreePopupMenu treePopupMenu = new TreePopupMenu();
				treePopupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
		
		public void mouseExited(MouseEvent e) {
		}
		
		public void mouseEntered(MouseEvent e) {
		}
		
		public void mouseReleased(MouseEvent e) {
		}
		
		public void mousePressed(MouseEvent e) {
		}
	}
	
	class TreePopupMenu extends JPopupMenu implements ActionListener {
		
		JMenuItem
			removeNode = new JMenuItem("Remove"),
			clone = new JMenuItem("Clone");
		
		public TreePopupMenu() {
			super();
			
			removeNode.addActionListener(this);
			this.add(removeNode);
			
		}
		
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == removeNode) {
				classTree.removeCurrentNode();
			}
		}
	}
	
	class PropertiesTable extends JScrollPane {
		
	// 	DefaultTableModel model;
		JTable table;
		String[] classNames = {"One", "Two", "Three"};
	// 	JComboBox classes;
		public PropertiesTable() {
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
			this.setViewportView(table);
		}
		
	}
}