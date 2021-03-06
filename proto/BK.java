import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.*;
import javax.swing.tree.*;

/** Description.
	@author (classes and interfaces only, required)
	@version (classes and interfaces only, required. See footnote 1)
	@param (methods and constructors only)
	@return (methods only)
	@exception (@throws is a synonym added in Javadoc 1.2)
	@see 
	@since 
	@serial (or @serialField or @serialData)
	@deprecated (see How and When To Deprecate APIs)
*/
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
	JLabel label = new JLabel("");
	DefaultMutableTreeNode top = new DefaultMutableTreeNode("Top of tree");
	Classes classes;
	MyClass currentClass;
	
	/** Creates table elements and layout.
	*/
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
		
		// Initialise commands panel
		addCommandsPanel = new JPanel();
		addCommandsPanel.setLayout(new GridLayout(0,1));
		AddCommandsScrollPane addCommandsScrollPane = new AddCommandsScrollPane();
		addCommandsPanel.add(addCommandsScrollPane);
		
		// Setting up split panes
		classPropertiesSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, classPanel, addCommandsPanel);
		classPropertiesSplit.setResizeWeight(0.5);
		
		// Configure commands
		commandsScrollPane = new JScrollPane(commands, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
// 		/* Test // Comment and uncomment this to add sample data
		MyMethod a = new MyMethod("methodA", null);
		MyMethod b = new MyMethod("methodB", null);
		MyMethod c = new MyMethod("methodC", null);
		MyMethod d = new MyMethod("methodD", null);
		
		MyClass cls = new MyClass("classA", null);
		cls.addMethod(a);
		cls.addMethod(b);
		cls.addMethod(c);
		cls.addMethod(d);
		classes.addObject(cls);
		currentClass = cls;
		commandsScrollPane.setViewportView(currentClass.getPanel());
		
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
		
		methodNew.addActionListener(menuBarListener);
		methodRemove.addActionListener(menuBarListener);
		
		menuBar.add(methodMenu);
		methodMenu.add(methodNew);
		methodMenu.add(methodRemove);
		
		this.setJMenuBar(menuBar);
	}
	
	/** Does something.
		Is this even needed?
	*/
	public void updateCommands() {
		commandsScrollPane.setViewportView(currentClass.getPanel());
	}
	
	/** Main method.
	*/
	public static void main(String[] args) {
		Proto window = new Proto();
		window.parent = window;
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(1280, 720);
		window.pack();
		window.setVisible(true);
	}
	
	/** Manages clicks on all menu bar items.
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
			if (e.getSource() == methodNew) {
				String name = JOptionPane.showInputDialog(parent, "Enter a name for your new method:", "New method..", JOptionPane.QUESTION_MESSAGE);
				if (name == null || name.trim().equals("")) {
					return;
				}
				System.out.println("Adding " + name + " to " + currentClass.toString());
				currentClass.addMethod(new MyMethod(name, null));
				commandsScrollPane.setViewportView(currentClass.getPanel());
				System.out.println(currentClass.toString());
			}
			if (e.getSource() == methodRemove) {
				int response = JOptionPane.showConfirmDialog(parent, "Are you sure you want to remove this method?", "Remove method", JOptionPane.YES_NO_OPTION);
				System.out.println(response);
				if (response == 1) {
					return;
				}
				currentClass.removeMethod(currentClass.currentMethod());
				
			}
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
//				TODO
			}
		}
	}
	
	class AddCommandsScrollPane extends JScrollPane implements ActionListener {
		String[] buttons = new String[] {
			"Print",
			"Method",
			"Assignment"
		};
		JPanel panel;
		
		public AddCommandsScrollPane() {
			panel = new JPanel();
			for (int i = 0; i < buttons.length; i++) {
				JButton button = new JButton(buttons[i]);
				button.addActionListener(this);
				panel.add(button);
			}
			
			this.setViewportView(panel);
		}
		
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			if (b.getText().equals("Print")) {
				currentClass.currentMethod().addCommand(new Command("Print", new TextValue(5)));
				updateCommands();
			}
			
			if (b.getText().equals("Method")) {
				currentClass.currentMethod().addCommand(new Command("Print", new TextValue(5)));
				updateCommands();
			}
		}
	}
	
}

interface IValue {
// 	public IValue getParent();
}

interface IContainer {
	public void changeValue(Component oldComponent, Component newComponent);
	
}

interface IPrimitive {
	
	
}

interface IBlock {
	
}

class ScriptMouseListener implements MouseListener {
	
	public ScriptMouseListener() {
	}
	
	class ScriptPopupMenu extends JPopupMenu implements ActionListener {
		
		JMenu
// 			change = new JMenu("Change to.."),
			preset = new JMenu("Preset");
		JMenuItem
			text = new JMenuItem("Text"),
			combined = new JMenuItem("Combined"),
			method = new JMenuItem("Method");
		
		public ScriptPopupMenu() {
			super();
			
// 			this.add(change);
// 			change.add(preset);
			this.add(text);
			this.add(combined);
			this.add(method);
			
			text.addActionListener(this);
			combined.addActionListener(this);
			method.addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent e) {
			Component newComponent = null;
			if (e.getSource() == text) {
				newComponent = new TextValue(10);
			} else if (e.getSource() == combined) {
				newComponent = new CombinedValue(new TextValue("Left"), new TextValue("Right"));
			} else if (e.getSource() == method) {
				newComponent = new MethodValue("Method3", 3);
			}
			JMenuItem item = (JMenuItem) e.getSource();
			JPopupMenu menu = (JPopupMenu) item.getParent();
			Component invoker = menu.getInvoker();
			
			if (!(invoker instanceof IPrimitive)) {
				invoker = invoker.getParent();
			}
			
			Component parent = (Component) invoker.getParent();
			IContainer container = (IContainer) parent;
			
			System.out.println("Invoker: " + invoker);
			
			container.changeValue(invoker, newComponent);
			System.out.println("Parent: " + parent);
			
			while (!(parent instanceof Command)) {
				parent.revalidate();
				parent.repaint();
				parent = parent.getParent();
			}
		}
	}
	
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			ScriptPopupMenu scriptPopupMenu = new ScriptPopupMenu();
			scriptPopupMenu.show(e.getComponent(), e.getX(), e.getY());
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

// Container
class MethodValue extends JPanel implements IValue, IContainer {
	JLabel label;
	LinkedList<Component> params;
	ScriptMouseListener mouseListener = new ScriptMouseListener();
	
	public MethodValue(String name, int numParams) {
		super();
		label = new JLabel(name);
		label.addMouseListener(mouseListener);
		params = new LinkedList<Component>();
		this.add(label);
		
		for (int i = 0; i < numParams; i++) {
			TextValue temp = new TextValue("");
			temp.addMouseListener(mouseListener);
			params.add(temp);
			this.add(temp);
		}
		
		this.repaint();
		this.validate();
	}
	
// 	public void changeValue(IValue oldValue, IValue newValue) {
	public void changeValue(Component oldComponent, Component newComponent) {
		int pos = params.indexOf(oldComponent);
		if (pos == -1) {
			return;
		}
		
		params.set(pos, newComponent);
		this.remove(oldComponent);
		this.add(newComponent, pos + 1); // #Test
		
		this.repaint();
		this.validate();
		this.getParent().repaint();
		this.getParent().validate();
		return;
	}
	/*
	public IValue getParent() {
		return null;
	}*/
}

// Container
class CombinedValue extends JPanel implements IValue, IContainer {
	static final String[] OPERANDS = new String[] {
		"+",
		"-",
		"*",
		"/",
		"%",
	};
	Component left;
	Component right;
	JComboBox middle;
	JLabel label;
	
	public CombinedValue(Component left, Component right) {
		super();
		this.left = left;
		this.right = right;
		this.middle = new JComboBox<String>(OPERANDS);
		this.middle.addMouseListener(new ScriptMouseListener());
		this.add(left);
		this.add(middle);
		this.add(right);
	}
	
	public void changeValue(Component oldComponent, Component newComponent) {
		if (oldComponent == left) {
			left = newComponent;
			this.remove(oldComponent);
			this.add(newComponent, 0);
			return;
		}
		if (oldComponent == right) {
			this.remove(oldComponent);
			this.add(newComponent, 2);
			right = newComponent;
			return;
		}
		this.validate();
		this.repaint();
		this.getParent().validate();
		this.getParent().repaint();
	}
}

// Primitive
class TextValue extends JTextField implements IValue, IPrimitive {
	MyDocumentListener documentListener = new MyDocumentListener();
	
	public TextValue(String value) {
		super(value);
		this.addMouseListener(new ScriptMouseListener());
		this.getDocument().putProperty("parent", this);
		this.getDocument().addDocumentListener(documentListener);
// 		this.setMinimumSize(new Dimension(200,200));
// 		this.setPreferredSize(new Dimension(200,200));
	}
	public TextValue(int columns) {
		super(columns);
		this.addMouseListener(new ScriptMouseListener());
		this.getDocument().putProperty("parent", this);
		this.getDocument().addDocumentListener(documentListener);
// 		this.setMinimumSize(new Dimension(200,200));
// 		this.setPreferredSize(new Dimension(200,200));
	}
	
	class MyDocumentListener implements DocumentListener {
		public MyDocumentListener() {
		}
		
		public void adjust(DocumentEvent e) {
			JTextField textField = (JTextField) e.getDocument().getProperty("parent");
			textField.setMinimumSize(new Dimension(100,100));
			textField.getParent().getParent().validate();
			textField.getParent().getParent().getParent().validate();
		}
		
		public void changedUpdate(DocumentEvent e) {
			adjust(e);
		}
		
		public void insertUpdate(DocumentEvent e) {
			adjust(e);
		}
		
		public void removeUpdate(DocumentEvent e) {
			adjust(e);
		}
	}
}

class VariableValue extends JComboBox implements IValue, IPrimitive{
	
// 	HashMap<>classVariables
	
	public VariableValue() {
		super();
	}
}

/*
class Conditional extends Value {
	
}*/

/** Represents one line of code in a method.
*/
class Command extends JPanel implements IContainer {
	JLabel label;
	IValue value;
	
	public Command(String name, IValue value) {
		// Add components
		super();
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setBackground(Color.lightGray);
		this.setMaximumSize(new Dimension(2000, 46));
// 		this.setPreferredSize(new Dimension(200, 100));
// 		this.setAlignmentX(LEFT_ALIGNMENT);
		this.label = new JLabel(name);
		this.value = value;
		this.add(label);
		this.add((Component) value);
	}
	
	public void changeValue(Component oldComponent, Component newComponent) {
		this.remove(oldComponent);
		this.add(newComponent);
		this.validate();
		this.repaint();
	}
}

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
		this.tabbedPane = new JTabbedPane();
	}
	
	public JTabbedPane getPanel() {
		return tabbedPane;
	}
	
	public void addMethod(MyMethod method) {
		
		Component current = tabbedPane.getSelectedComponent();
		
		tabbedPane = new JTabbedPane();
		methods.add(method);
		Iterator<MyMethod> methodIterator = methods.iterator();
		MyMethod result = null;
		while (methodIterator.hasNext()) {
			MyMethod next = methodIterator.next();
			tabbedPane.add(next);
		}
		if (current != null) {
			tabbedPane.setSelectedComponent(current);
		}
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
	
	public void removeMethod(MyMethod method) {
		methods.remove(method);
		tabbedPane.remove(method);
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

class Variable {
	MyClass type;
	String name;
	HashSet<JComboBox> uses;
	
	public Variable(MyClass type, String name) {
		this.type = type;
		this.name = name;
		uses = new HashSet<JComboBox>();
	}
	
	public String toString() {
		return name;
	}
}

/** Represents methods in a class
*/
class MyMethod extends JScrollPane implements Comparable, MouseListener, MouseMotionListener {
	String name;
	MyClass returnType;
	LinkedList<Command> script;
	LinkedList<Argument> arguments;
	JPanel panel;
	BoxLayout boxLayout;
	HashMap<Integer, Variable> variables;
	HashMap<Variable, Integer> varIDs;
	
	public MyMethod(String name, MyClass returnType) {
		super();
		this.setName(name);
		this.name = name;
		this.returnType = returnType;
		script = new LinkedList<Command>();
		panel = new JPanel();
		boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(boxLayout);
		this.setViewportView(panel);
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
		command.addMouseListener(this);
		script.add(index, command);
		panel.add(command);
	}
	
	public void removeCommand(Command command) {
		System.out.println(script.remove(command));
		panel.remove(command);
		panel.validate();
		this.repaint(); // 50L as param?
	}
	
	// Changed to return itself instead of returning a panel
	public JScrollPane getPanel() {
		return this;
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
	
	
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isMiddleMouseButton(e)) {
			Command command = (Command) e.getSource();
			removeCommand(command);
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
	
	public void mouseDragged(MouseEvent e) {
	}
	
	public void mouseMoved(MouseEvent e) {
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

//////////// ARCHIVE

// abstract class Value extends JPanel implements MouseListener {
// 	
// 	public Value() {
// 		super();
// 		this.addMouseListener(this);
// 		this.setBorder(new EmptyBorder(0, 0, 0, 0));
// 		/*
// 		this.setBorder(BorderFactory.createLineBorder(Color.black));
// 		this.setBackground(Color.white);
// 		this.addMouseListener(this);
// 		this.setMaximumSize(new Dimension(40, 20);
// 		this.setAlignmentX(Component.LEFT_ALIGNMENT);*/
// 	}
// 	
// 	public abstract boolean modifyValue(Value oldValue, Value newValue);
// 	
// 	class ValuePopupMenu extends JPopupMenu implements ActionListener {
// 		
// 		JMenu
// // 			change = new JMenu("Change to.."),
// 			preset = new JMenu("Preset");
// 		JMenuItem
// 			text = new JMenuItem("Text"),
// 			combined = new JMenuItem("Combined"),
// 			method = new JMenuItem("Method");
// 		
// 		public ValuePopupMenu() {
// 			super();
// 			
// // 			this.add(change);
// // 			change.add(preset);
// 			this.add(text);
// 			this.add(combined);
// 			this.add(method);
// 			
// 			text.addActionListener(this);
// 			combined.addActionListener(this);
// 			method.addActionListener(this);
// 		}
// 		
// 		public void actionPerformed(ActionEvent e) {
// 			
// 			if (e.getSource() == text) {
// 				JMenuItem item = (JMenuItem) e.getSource();
// 				JPopupMenu menu = (JPopupMenu) item.getParent();
// 				Value invoker = (Value) menu.getInvoker().getParent();
// // 				System.out.println("Invoker: " + invoker);
// 				invoker.modifyValue(invoker, new MethodValue("Aya", 1));
// 				System.out.println("Heh");
// 				System.out.println("Parent: " + invoker.getParent());
// 				// Change the value here
// 				// invoker.changeValue(new MethodValue("aaa", new TextValue("Se2")));
// 			} else if (e.getSource() == combined) {
// 			} else if (e.getSource() == method) {
// 				
// 			}
// 		}
// 	}
// 	
// 	public void mouseClicked(MouseEvent e) {
// 		if (SwingUtilities.isRightMouseButton(e)) {
// 			ValuePopupMenu valuePopupMenu = new ValuePopupMenu();
// 			valuePopupMenu.show(e.getComponent(), e.getX(), e.getY());
// 		}
// 	}
// 		
// 	public void mouseExited(MouseEvent e) {
// 	}
// 	
// 	public void mouseEntered(MouseEvent e) {
// 	}
// 	
// 	public void mouseReleased(MouseEvent e) {
// 	}
// 	
// 	public void mousePressed(MouseEvent e) {
// 	}
// }