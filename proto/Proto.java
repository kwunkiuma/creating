import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;
import javax.swing.tree.*;
import javax.swing.event.*;

public class Proto extends JFrame {
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
		commandsScrollPane;
	JPanel
		classPanel,
		methodProperties = new JPanel(),
		commands,
		addCommandsPanel;
	JLabel label = new JLabel("Test");
	DefaultMutableTreeNode top = new DefaultMutableTreeNode("Top of tree");
	Classes classes;
	MyClass currentClass;
	
	public Proto() {
		initMenuBar();
		
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
				classes.addObject(temp, new MyClass("Child", null));
			}
		}
		
		// Initialise 
		addCommandsPanel = new JPanel();
		addCommandsPanel.setLayout(new GridLayout(0,1));
		AddCommandsScrollPane addCommandsScrollPane = new AddCommandsScrollPane();
		addCommandsPanel.add(addCommandsScrollPane);
		
		// Setting up split panes
		classPropertiesSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, classPanel, addCommandsPanel);
		classPropertiesSplit.setResizeWeight(0.5);
		
		// Configure commands
		commandsScrollPane = new JScrollPane(commands, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
// 		commandsScrollPane.setPreferredSize(new Dimension(100, 300));
		
// 		/* Test
		MyMethod a = new MyMethod("A", null);
		MyMethod z = new MyMethod("Z", null);
		MyMethod method = new MyMethod("Mestot", null);
// 		method.addCommand(new Command("Text", "Value"));
// 		method.addCommand(new Command("Textqqqq2", "Value"));
// 		method.addCommand(new Command("Textqqqq2", "Value"));
// 		method.addCommand(new Command("Textqqqq2", "Value"));
// 		method.addCommand(new Command("Textqqqq2", "Value"));
		
		MyMethod mtd = new MyMethod("Mestwo", null);
// 		mtd.addCommand(new Command("Tekusuto", "Baryu"));
		
		MyClass cls = new MyClass("Kurasu", null);
		cls.addMethod(method);
		cls.addMethod(a);
		cls.addMethod(z);
		cls.addMethod(mtd);
		classes.addObject(cls);
		commandsScrollPane.setViewportView(cls.getPanel());
		
		/**/
		
		classSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, classPropertiesSplit, commandsScrollPane);
		classSplit.setResizeWeight(0);
		
		methodPropertySplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, methodProperties, label);
		
		methodPropertySplit.setResizeWeight(0.5);
		centreSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, classSplit, methodPropertySplit);
		centreSplit.setResizeWeight(1);
		
		
		add(centreSplit);
	}
	
	/** Creates menu items and adds listeners.
	*/
	public void initMenuBar() {
		
		MenuBarListener menuBarListener = new MenuBarListener();
		
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
		classRemove.addActionListener(menuBarListener);
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
	}
	
	public void updateCommands() {
		commandsScrollPane.setViewportView(currentClass.getPanel());
	}
	
	/** Creates main window.
	*/
	public static void main(String[] args) {
		Proto window = new Proto();
		window.parent = window;
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(1280, 720);
		window.pack();
		window.setVisible(true);
	}
	
	/** Manages clicks on all menu bar items
	*/
	class MenuBarListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == classNew) {
				String name = JOptionPane.showInputDialog(parent, "Enter a name for your new class:", "New class..", JOptionPane.QUESTION_MESSAGE);
				if (name == null || name.trim().equals("")) {
					return;
				}
				classes.addObject(new MyClass(name, null));
			}
			// temp
			else {
				// TEST HERE
			}
		}
	}
	
	/** Listener for movement within code window
	*/
	class BoxMotionListener implements MouseMotionListener {
		public void mouseDragged(MouseEvent e) {
			System.out.println("Drag");
		}
		
		public void mouseMoved(MouseEvent e) {
			
		}
	}
	
	/** Listener for tree window displaying classes
	*/
	class ClassesListener implements TreeSelectionListener, MouseListener {
		JTree tree;
		
		public ClassesListener(Classes classes) {
			this.tree = classes.tree;
		}
		
		public void valueChanged(TreeSelectionEvent e) {
			DefaultMutableTreeNode dtn = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
			currentClass = (MyClass) dtn.getUserObject();
			commandsScrollPane.setViewportView(currentClass.getPanel());
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
	
	/** Popup menu that is shown when right mouse button is clicked.
	*/
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
	
	class AddCommandsScrollPane extends JScrollPane implements ActionListener {
		JButton print, compare;
		JPanel panel;
		
		public AddCommandsScrollPane() {
			print = new JButton("Print");
			print.addActionListener(this);
			compare = new JButton("Compare");
			panel = new JPanel();
			panel.add(print);
			panel.add(compare);
			this.setViewportView(panel);
		}
		
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == print) {
				currentClass.currentMethod().addCommand(new Command(new PresetValue("Print", 1)));
				updateCommands();
			}
		}
	}
	
	
}

abstract class Value {
	JPanel panel;
	
	public Value() {
		panel = new JPanel();
		panel.setBorder(BorderFactory.createLineBorder(Color.black));
		panel.setBackground(Color.white);
	}
	
	public JPanel getPanel() {
		return panel;
	}
}

class PresetValue extends Value {
	JLabel label;
	LinkedList<Value> params;
	
	public PresetValue(String value, int numParams) {
		label = new JLabel(value);
		params = new LinkedList<Value>();
		panel.add(label);
		
		for (int i = 0; i < numParams; i++) {
			TextValue temp = new TextValue("");
			params.add(temp);
			panel.add(temp.getPanel());
		}
		
	}
}

class CombinedValue extends Value {
	static final String[] OPERANDS = new String[] {
		"+",
		"-",
		"*",
		"/",
		"%",
	};
	Value left;
	Value right;
	JComboBox middle;
	JLabel label;
	
	public CombinedValue(Value left, Value right) {
		super();
		this.left = left;
		this.right = right;
		this.middle = new JComboBox<String>(OPERANDS);
		panel.add(left.getPanel());
		panel.add(middle);
		panel.add(right.getPanel());
	}
}

class TextValue extends Value {
	JTextField textField;
	/*
	public TextValue() {
		super();
		TextValue("");
	}*/
	
	public TextValue(String value) {
		super();
		this.textField = new JTextField(value);
		panel.add(textField);
	}
}

class MethodValue extends Value {
	JTextField method;
	Value param;
	
	public MethodValue(String method, Value param) {
		super();
		this.method = new JTextField(method);
		this.param = param;
		panel.add(this.method);
		panel.add(this.param.getPanel());
	}
}
/*
class Conditional extends Value {
	
}*/

/** Represents one line of code in a method.
*/
class Command {
	Value value;
	JPanel panel;
	
	public Command(Value value) {
		this.value = value;
		
		// Add components
		panel = new JPanel();
		panel.setBorder(BorderFactory.createLineBorder(Color.black));
		panel.setBackground(Color.lightGray);
		panel.add(this.value.getPanel());
	}
	
	public JPanel getPanel() {
		return panel;
	}
}

/** Represents one line of code in a method.
*/
// class Wrapping extends Command {
// 	
// }

/** Represents classes
*/
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
	
	public void addMethod(MyMethod method) {
		this.methods.add(method);
	}
	
	public String toString() {
		return this.name;
	}
	
	public MyMethod currentMethod() {
		Iterator<MyMethod> methodIterator = methods.iterator();
		MyMethod result = null;
		for (int i = 0; i < tabbedPane.getSelectedIndex(); i++) {
			methodIterator.next();
		}
		return methodIterator.next();
	}
}

/** Represents arguments to feed into commands.
*/
class Argument {
	String name;
	MyClass type;
	
	public boolean equals(Argument operand) {
		if (this.type.name.equals(operand.type.name)) {
			return true;
		}
		return false;
	}
	
	public String toString() {
		return name;
	}
}

/** Represents methods in a class
*/
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
			springLayout.putConstraint(SpringLayout.NORTH, commandPanel, 10, SpringLayout.SOUTH, script.get(index - 1).getPanel());
		}
		panel.add(commandPanel);
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
	
	public String toString() {
		return name;
	}
}

/** Represents classes.
*/

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