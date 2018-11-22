import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;
import javax.swing.tree.*;
import javax.swing.event.*;

public class Proto extends JFrame {
	MenuBarListener menuBarListener = new MenuBarListener();
	JFrame
		parent;
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
	JScrollPane
		boxesScrollPane;
	JPanel
		classPanel,
		methodProperties = new JPanel(),
		boxes,
		propertiesPanel;
	JLabel label = new JLabel("Test");
	DefaultMutableTreeNode top = new DefaultMutableTreeNode("Top of tree");
	Classes classes;
	
	
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		JPanel panel4 = new JPanel();
	
	public Proto() {
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
		classNew.addActionListener(menuBarListener);
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
		classes = new Classes();
		ClassesListener classesListener = new ClassesListener(classes);
		classes.tree.addTreeSelectionListener(classesListener);
		classes.tree.addMouseListener(classesListener);
		classPanel.add(classes);
		
		// Populate table with sample data
		for (int i = 0; i < 10; i++) {
			DefaultMutableTreeNode temp = classes.addObject(null, new MyClass(Integer.toString(i), null));
			if (i == 4) {
				classes.addObject(temp, "Child");
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
		
		// Configure boxes
		boxes = new JPanel();
		boxesScrollPane = new JScrollPane(boxes, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		boxesScrollPane.setPreferredSize(new Dimension(100, 300));
		MyMethod method = new MyMethod("Mestot", null);
		method.addCommand(new Command("Text", "Value"));
		method.addCommand(new Command("Textqqqq2", "Value"));
		method.addCommand(new Command("Textqqqq2", "Value"));
		method.addCommand(new Command("Textqqqq2", "Value"));
		method.addCommand(new Command("Textqqqq2", "Value"));
		method.addCommand(new Command("Textqqqq2", "Value"));
		method.addCommand(new Command("Textqqqq2", "Value"));
		method.addCommand(new Command("Textqqqq2", "Value"));
		method.addCommand(new Command("Textqqqq2", "Value"));
		method.addCommand(new Command("Textqqqq2", "Value"));
		method.addCommand(new Command("Textqqqq2", "Value"));
		method.addCommand(new Command("Textqqqq2", "Value"));
		method.addCommand(new Command("Textqqqq2", "Value"));
		method.addCommand(new Command("Textqqqq2", "Value"));
		method.addCommand(new Command("Textqqqq2", "Value"));
		method.addCommand(new Command("Textqqqq2", "Value"));
		method.addCommand(new Command("Textqqqq2", "Value"));
// 		boxes.add(method.getPanel());
		boxesScrollPane.setViewportView(method.getPanel());
		SpringLayout sl = new SpringLayout();
		boxes.setLayout(sl);
		
		Command com1 = new Command("Texaasat", "Value");
		Command com2 = new Command("Text2", "Value");;
		JPanel pan1 = com1.getPanel();
		JPanel pan2 = com2.getPanel();
		JPanel prev = pan2;
		boxes = method.getPanel();
		System.out.println(sl.getConstraints(pan1));
		sl.putConstraint(SpringLayout.NORTH, pan1, 10, SpringLayout.SOUTH, pan2);
		System.out.println(sl.getConstraints(pan1));
		
		
		
		classSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, classPropertiesSplit, boxesScrollPane);
		classSplit.setResizeWeight(0);
		
		methodPropertySplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, methodProperties, label);
		
		methodPropertySplit.setResizeWeight(0.5);
		centreSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, classSplit, methodPropertySplit);
		centreSplit.setResizeWeight(1);
		
		
		add(centreSplit);
	}
	
	// Main method
	public static void main(String[] args) {
		Proto window = new Proto();
		window.parent = window;
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(1280, 720);
		window.pack();
		window.setVisible(true);
	}
	
	class MenuBarListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == classNew) {
				String name = JOptionPane.showInputDialog(parent, "Enter a name for your new class:", "New class..", JOptionPane.QUESTION_MESSAGE);
				if (name == null || name.trim().equals("")) {
					return;
				}
				classes.addObject(new MyClass(name, null));
			}
		}
	}
	
	class BoxMotionListener implements MouseMotionListener {
		public void mouseDragged(MouseEvent e) {
			System.out.println("Drag");
		}
		
		public void mouseMoved(MouseEvent e) {
			if (e.getSource() == panel1) {
				System.out.println("1");
			}
			System.out.println(e.getLocationOnScreen());
// 			System.out.println(e.getX() + ", " + e.getY());
		}
	}
	
	class ClassesListener implements TreeSelectionListener, MouseListener {
		JTree tree;
		
		public ClassesListener(Classes classes) {
			this.tree = classes.tree;
		}
		
		public void valueChanged(TreeSelectionEvent e) {
// 			label.setText(e.getPath().getLastPathComponent().toString());
		}
		
		public void mouseClicked(MouseEvent e) {
			int row = tree.getRowForLocation(e.getX(), e.getY());
			if (SwingUtilities.isRightMouseButton(e)) {
				if (row == -1) {
					return;
				}
				classes.tree.setSelectionRow(row);
				TreePopupMenu treePopupMenu = new TreePopupMenu();
				treePopupMenu.show(e.getComponent(), e.getX(), e.getY());
			} else {
				if (e.getClickCount() == 2) {
					if (row == -1) {
						return;
					}
					
					label.setText(tree.getPathForRow(row).toString());
				}
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
			remove = new JMenuItem("Remove"),
			rename = new JMenuItem("Rename");
		
		public TreePopupMenu() {
			super();
			
			remove.addActionListener(this);
			this.add(remove);
			this.add(rename);
			
		}
		
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == remove) {
				classes.removeCurrentNode();
			}
			
			if (e.getSource() == rename) {
// 				classes.renameCurrentNode();
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

class Command {
	String text, value;
	JPanel panel;
	JLabel label;
	JTextField textField;
	SpringLayout springLayout;
	public Command(String text, String value) {
		this.text = text;
		this.value = value;
		// Add components
		springLayout = new SpringLayout();
		panel = new JPanel();
		panel.setBorder(BorderFactory.createLineBorder(Color.black));
		panel.setBackground(Color.lightGray);
		label = new JLabel(text);
		panel.add(label);
// 		springLayout.putConstraint(SpringLayout.NORTH, label, 5, SpringLayout.NORTH, label.getParent());
// 		springLayout.putConstraint(SpringLayout.WEST, label, 5, SpringLayout.WEST, this);
		if (value != null) {
			textField = new JTextField(value);
// 				springLayout.putConstraint(SpringLayout.WEST, textField, 20, SpringLayout.EAST, label);
			panel.add(new JTextField(value));
		}
	}
	
	public JPanel getPanel() {
		return panel;
	}
}

class MyClass {
	String name;
	MyClass inherits;
	HashSet<String> implement; // Optional
	TreeSet<MyMethod> methods;
	JTabbedPane tabbedPane;
	
	public MyClass(String name, MyClass inherits) {
		this.name = name;
		this.inherits = inherits;
		this.methods = new TreeSet<MyMethod>();
// 		this.tabbedPane = new JTabbedPane();
	}
	
	public JTabbedPane getPanel() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane();
			Iterator<MyMethod> methodIterator = methods.iterator();
			while (methodIterator.hasNext()) {
				MyMethod next = methodIterator.next();
				tabbedPane.add(next.getName(), next.getPanel());
			}
		}
		return tabbedPane;
	}
	
	public String toString() {
		return this.name;
	}
}

class Argument {
	String name;
	MyClass type;
	
	public boolean equals(Argument operand) {
		if (this.type.name.equals(operand.type.name)) {
			return true;
		}
		return false;
	}
}

class MyMethod implements Comparable {
	String name;
	MyClass returnType;
	LinkedList<Command> script;
	LinkedList<Argument> arguments;
	JPanel panel;
	SpringLayout springLayout;
	
	public MyMethod(String name, MyClass returnType) {
		this.name = name;
		this.returnType = returnType;
		script = new LinkedList<Command>();
		panel = new JPanel();
		springLayout = new SpringLayout();
		panel.setLayout(springLayout);
	}
	
	// Two methods are same if they have the same method signature
	public boolean equals(MyMethod method) {
		
		// Compare names
		if (this.name != method.name) {
			return false;
		}
		
		// Compare arguments
		ListIterator<Argument> sourceIterator = this.arguments.listIterator(0);
		ListIterator<Argument> operandIterator = method.arguments.listIterator(0);
		while (sourceIterator.hasNext() && operandIterator.hasNext()) {
			if (!sourceIterator.next().equals(operandIterator.next())) {
				return false;
			}
		}
		return true;
	}
	
	public void addCommand(Command command) {
		addCommand(command, script.size());
	}
	
	public void addCommand(Command command, int index) {
		script.add(index, command);
		JPanel commandPanel = command.getPanel();
		if (index != 0) {
			System.out.println(command.text);
			System.out.println(script.get(index - 1).text);
			springLayout.putConstraint(SpringLayout.NORTH, commandPanel, 10, SpringLayout.SOUTH, script.get(index - 1).getPanel());
		}
		panel.add(commandPanel);
		System.out.println("Added " + command + " at " + index);
	}
	
	public JPanel getPanel() {
		return panel;
	}
	
	public String getName() {
		return name;
	}
	
	public int compareTo(Object o) {
		MyMethod method = (MyMethod) o;
		return this.name.compareTo(method.getName());
	}
}

class Classes extends JScrollPane {
	
	DefaultMutableTreeNode root;
	DefaultTreeModel model;
	JTree tree;
// 	Set<TreePath> pathSet;
	
	public Classes() {
		super();
		
		// Initialise tree properties
		root = new DefaultMutableTreeNode("Root");
		model = new DefaultTreeModel(root);
		tree = new JTree(model);
		tree.setEditable(true);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);
		tree.setRootVisible(false);
		
		// Place the tree in the scroll pane
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
	
	// Do ths?
// 	public DefaultMutableTreeNode getSelectionPath() {
// 		
// 	}
	
	public DefaultMutableTreeNode addObject(Object child) {
		return addObject(root, child);
	}
	
	public int getRowForLocation(MouseEvent e) {
		return tree.getRowForLocation(e.getX(), e.getY());
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
}