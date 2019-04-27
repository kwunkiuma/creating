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
public class Program extends JFrame {
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
		classExport,
		methodNew,
		methodRemove;
	JSplitPane
		classPropertiesSplit,
		classSplit,
		methodPropertySplit,
		centreSplit;
	JScrollPane
		commandsScrollPane,
		textAreaScrollPane;
	JPanel
		classPanel,
		methodProperties = new JPanel(),
		commands,
		addCommandsPanel,
		textAreaPanel;
	JTextArea
		textArea;
	JLabel label = new JLabel("ssss");
	DefaultMutableTreeNode top = new DefaultMutableTreeNode("Top of tree");
	Classes classes;
	MyClass currentClass;
	
	/** Creates table elements and layout.
	*/
	public Program() {
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
		
		/* Test // Comment and uncomment this to add sample data
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
		
		textArea = new JTextArea();
		textAreaScrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		textAreaScrollPane.setPreferredSize(new Dimension(600, 500));
		
// 		methodPropertySplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, textAreaScrollPane, methodProperties);
		
// 		methodPropertySplit.setResizeWeight(0.5);
		centreSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, classSplit, textAreaScrollPane);
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
		classExport = new JMenuItem("Export");
		
		classNew.addActionListener(menuBarListener);
		classRemove.addActionListener(menuBarListener);
		classExport.addActionListener(menuBarListener);
		
		menuBar.add(classMenu);
		classMenu.add(classNew);
		classMenu.add(classRemove);
		classMenu.add(classExport);
		
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
		Program window = new Program();
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
			if (e.getSource() == classExport) {
				textArea.setText(currentClass.getString(0));
				
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
	
	class AddCommandsScrollPane extends JTabbedPane implements ActionListener {
		
		String[] control = new String[] {
			"For",
			"If",
			"Else",
			"Else if",
			"End"
		};
		
		String[] action = new String[] {
			"Print",
			"Method"
		};
		
		String[] variable = new String[] {
			"Assignment",
			"Return",
		};
		/*
		String[] buttons = new String[] {
			"Print",
			"Method",
			"Assignment",
			"Return",
			"If",
			"Else",
			"Else if",
			"End",
			"For"
		};*/
		JPanel
			controlPanel,
			variablePanel,
			actionPanel;
		
		public AddCommandsScrollPane() {
			this.setPreferredSize(new Dimension(200,400));
			controlPanel = new JPanel();
			for (int i = 0; i < control.length; i++) {
				JButton button = new JButton(control[i]);
				button.addActionListener(this);
				controlPanel.add(button);
			}
			
			variablePanel = new JPanel();
			for (int i = 0; i < variable.length; i++) {
				JButton button = new JButton(variable[i]);
				button.addActionListener(this);
				variablePanel.add(button);
			}
			
			actionPanel = new JPanel();
			for (int i = 0; i < action.length; i++) {
				JButton button = new JButton(action[i]);
				button.addActionListener(this);
				actionPanel.add(button);
			}
			
			this.addTab("Control", controlPanel);
			this.addTab("Variable", variablePanel);
			this.addTab("Action", actionPanel);
		}
		
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			Component[] components;
			
			if (b.getText().equals("Print")) {
				components = new Component[] {
					new JLabel("Print"),
					new TextValue("Message")
				};
				components[1].requestFocusInWindow();
				currentClass.currentMethod().addCommand(new Command(components, "Print"));
				updateCommands();
			}
			
			if (b.getText().equals("Method")) {
				components = new Component[] {
					new TextValue("MethodName"),
					new TextValue("Parameters")
				};
				components[0].requestFocusInWindow();
				currentClass.currentMethod().addCommand(new Command(components, "Print"));
				updateCommands();
			}
			
			if (b.getText().equals("Return")) {
				components = new Component[] {
					new JLabel("Return"),
					new TextValue("Value")
				};
				components[1].requestFocusInWindow();
				currentClass.currentMethod().addCommand(new Command(components, "Return"));
				updateCommands();
			}
			
			if (b.getText().equals("Assignment")) {
				components = new Component[] {
					new JLabel("Set"),
					new TextValue("Variable"),
					new JLabel("To"),
					new TextValue("Value")
				};
				components[1].requestFocusInWindow();
				currentClass.currentMethod().addCommand(new Command(components, "Assignment"));
				updateCommands();
			}
			
			if (b.getText().equals("If")) {
				components = new Component[] {
					new JLabel("If"),
					new TextValue("Condition")
				};
				components[1].requestFocus();
				currentClass.currentMethod().addCommand(new Command(components, "If"));
				updateCommands();
			}
			
			if (b.getText().equals("End")) {
				components = new Component[] {
					new JLabel("End")
				};
				currentClass.currentMethod().addCommand(new Command(components, "End"));
				updateCommands();
			}
			
			if (b.getText().equals("For")) {
				components = new Component[] {
					new JLabel("For"),
					new TextValue("Initialise"),
					new TextValue("Terminate"),
					new TextValue("Increment"),
				};
				components[1].requestFocusInWindow();
				currentClass.currentMethod().addCommand(new Command(components, "For"));
				updateCommands();
			}
			
			if (b.getText().equals("Else")) {
				components = new Component[] {
					new JLabel("Else")
				};
				currentClass.currentMethod().addCommand(new Command(components, "Else"));
				updateCommands();
			}
			
			if (b.getText().equals("Else if")) {
				components = new Component[] {
					new JLabel("Else if"),
					new TextValue("Condition")
				};
				components[1].requestFocusInWindow();
				currentClass.currentMethod().addCommand(new Command(components, "Elseif"));
				updateCommands();
			}
			
			
		}
	}
	
}

interface IValue {
// 	public IValue getParent();
}

// Interface for any class that containes other components
interface IContainer {
	public void changeValue(Component oldComponent, Component newComponent);
	
}

interface IPrimitive {
	
	
}

interface IBlock {
	
}

interface IScript {
	public String getString(int tabs);
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
				newComponent = new TextValue("Hello!");
			} else if (e.getSource() == combined) {
				newComponent = new CombinedValue(new TextValue("Left"), new TextValue("Right"));
			} else if (e.getSource() == method) {
				newComponent = new MethodValue("Method3", 2);
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
			
			while (!(parent instanceof IContainer)) {
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
class MethodValue extends JPanel implements IValue, IContainer, IScript {
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
	
	public String getString(int tabs) {
		String result = new String("");
		result += label.getText();
		
		for (Iterator iterator = params.iterator(); iterator.hasNext(); ) {
			IScript element = (IScript) iterator.next();
			result += element.getString(tabs);
			
			if (iterator.hasNext()) {
				result += ", ";
			}
		}
		
		return result;
	}
	
	/*
	public IValue getParent() {
		return null;
	}*/
}

// Container
class CombinedValue extends JPanel implements IValue, IContainer, IScript {
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
	
	public String getString(int tabs) {
		String result = new String("");
		result += "(";
		
		IScript leftScript = (IScript) left;
		IScript rightScript = (IScript) right;
		String operator = (String) middle.getSelectedItem();
		
		result += leftScript.getString(tabs);
		result += operator;
		result += leftScript.getString(tabs);
		result += ")";
		return result;
	}
}

// Primitive
class TextValue extends JTextField implements IValue, IPrimitive, IScript {
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
	
	public String getString(int tabs) {
		String result = new String("");
		result += this.getText();
		
		return result;
	}
}

// TODO
class VariableValue extends JComboBox implements IValue, IPrimitive {
	
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
class Command extends JPanel implements IContainer, IScript {
	LinkedList<Component> components;
	String type;
	static int moreTabs = 0;
	/*
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
	}*/
	
	public Command(Component[] components, String type) {
		super();
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setBackground(Color.lightGray);
// 		this.setMaximumSize(new Dimension(2000, 46));
		if (type.equals("End") || type.equals("Else")) {
			this.setPreferredSize(new Dimension(50,31));
		}/*
		if (type.equals("Elseif") {
			this.setPreferredSize(new Dimension(,31));
		}*/
		this.type = type;
		this.components = new LinkedList<Component>();
		for (int i = 0; i < components.length; i++) {
			this.components.add(components[i]);
			this.add(components[i]);
		}
	}
	
	public String getType() {
		return type;
	}
	
	public void changeValue(Component oldComponent, Component newComponent) {
		int pos = components.indexOf(oldComponent);
		this.remove(oldComponent);
		this.add(newComponent, pos);
		components.remove(oldComponent);
		components.add(pos, newComponent);
		this.validate();
		this.repaint();
	}
	
	public String getString(int tabs) {
		IScript element;
		
		
		if (type.equals("End")) {
			moreTabs -= 1;
		}
		
		String result = new String("");
		for (int i = 0; i < tabs + moreTabs; i++) {
			result += "\t";
		}
		
		if (type.equals("Print")) {
			result += "System.out.println(";
			element = (IScript) components.get(1);
			result += element.getString(tabs);
			result += ")";
			result += ";";
		}
		
		if (type.equals("Assignment")) {
			element = (IScript) components.get(1);
			result += element.getString(tabs);
			result += " = ";
			element = (IScript) components.get(3);
			result += element.getString(tabs);
			result += ";";
		}
		
		if (type.equals("Return")) {
			result += "return ";
			element = (IScript) components.get(1);
			result += element.getString(tabs);
			result += ";";
		}
		
		if (type.equals("If")) {
			result += "if (";
			element = (IScript) components.get(1);
			result += element.getString(tabs);
			result += ") {";
			moreTabs += 1;
		}
		
		if (type.equals("End")) {
			result += "}";
		}
		
		if (type.equals("For")) {
			result += "for (";
			element = (IScript) components.get(1);
			result += element.getString(tabs);
			result += "; ";
			element = (IScript) components.get(2);
			result += element.getString(tabs);
			result += "; ";
			element = (IScript) components.get(3);
			result += element.getString(tabs);
			result += ")";
			moreTabs += 1;
		}
		
		if (type.equals("Else")) {
			result += "} else {";
			moreTabs += 1;
		}
		
		if (type.equals("Elseif")) {
			result += "} else if (";
			element = (IScript) components.get(1);
			result += element.getString(tabs);
			result += ") {";
			moreTabs += 1;
		}
		
		
		result += "\n";
		return result;
	}
}

class Assignment extends JPanel implements IContainer {
	IValue left, right;
	
	public Assignment(IValue left, IValue right) {
		// Add components
		super();
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setBackground(Color.lightGray);
		this.setMaximumSize(new Dimension(2000, 46));
// 		this.setPreferredSize(new Dimension(200, 100));
// 		this.setAlignmentX(LEFT_ALIGNMENT);
		this.left = left;
		this.right = right;
		this.add(new JLabel("Set"));
		this.add((Component) left);
		this.add(new JLabel("To"));
		this.add((Component) right);
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
	
	public String getString(int tabs) {
		
		String result = new String("");
		for (int i = 0; i < tabs; i++) {
			result += "\t";
		}
		result += "public class ";
		result += name;
		result += " {";
		tabs += 1;
		for (Iterator<MyMethod> iterator = methods.iterator(); iterator.hasNext(); ) {
			result += "\n";
			MyMethod method = iterator.next();
			result += method.getString(tabs);
			
			result += "\n";
		}
		tabs -= 1;
		for (int i = 0; i < tabs; i++) {
			result += "\t";
		}
		result += "}";
		
		return result;
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
	
	public String getString(int tabs) {
		String result = new String("");
		result += type.name;
		result += " ";
		result += name;
		
		return result;
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
	static int padding = 3;
	String protection = "public";
	String name;
	MyClass returnType;
	LinkedList<Component> script;
	LinkedList<Argument> arguments;
	JPanel panel;
	BoxLayout boxLayout;
	SpringLayout layout;
	HashMap<Integer, Variable> variables;
	HashMap<Variable, Integer> varIDs;
	JSeparator sp;
	int pos = 0;
	Component dragTarget;
	static String[] indentsArray = new String[] {
		"If",
		"For",
		"While",
		"Else",
		"Elseif"
	};
	static HashSet<String> indents = new HashSet<String>(Arrays.asList(indentsArray));
// 	Component 
	
	public MyMethod(String name, MyClass returnType) {
		super();
		this.setName(name);
		this.name = name;
		this.returnType = returnType;
		script = new LinkedList<Component>();
		panel = new JPanel();
// 		panel.addMouseListener();
		boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(boxLayout);
		layout = new SpringLayout();
		panel.setLayout(layout);
		sp = new JSeparator();
		sp.setPreferredSize(new Dimension(200, 5));
		sp.setBackground(Color.blue);
// 		sp.setVisible(false);
		panel.add(sp);
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
	
	public void addCommand(Component component) {
		addCommand(component, pos);
		pos = pos + 1;
		putSp(pos);
	}
	
	public void addCommand(Component component, int index) {
		if (component.getMouseListeners().length == 0) {
			component.addMouseListener(this);
		}
		if (component.getMouseMotionListeners().length == 0) {
			component.addMouseMotionListener(this);
		}
		System.out.println(index);
		if (index != 0) {
			Component previous = script.get(index - 1);
			
			setupLayout(previous, component);
			
		} else {
			layout.putConstraint(SpringLayout.NORTH, component, padding, SpringLayout.NORTH, panel);
		}
		
		if (index != script.size()) {
			Component next = script.get(index);
			setupLayout(component, next);
		}
		script.add(index, component);
		
		if (!panel.isAncestorOf(component))
			panel.add(component);
		panel.repaint();
		panel.revalidate();
	}
	
	public void setupLayout(Component above, Component below) {
			int indent = 0;
			
			Command command = (Command) above;
			if (indents.contains(command.type)) {
				indent += 20;
			}
			
			command = (Command) below;
			if (command.type.equals("End") || command.type.equals("Else") || command.type.equals("Elseif")) {
				indent -= 20;
			}
			
			layout.putConstraint(SpringLayout.NORTH, below, padding, SpringLayout.SOUTH, above);
			layout.putConstraint(SpringLayout.WEST, below, indent, SpringLayout.WEST, above);
	}
	
	public void removeCommand(Component component) {
		int index = script.indexOf(component);
		
		
		if (index != script.size() - 1) { // Modify layout if not last command
			Component next = script.get(index + 1);
			if (index == 0) {
				layout.putConstraint(SpringLayout.NORTH, next, padding, SpringLayout.NORTH, panel);
				layout.putConstraint(SpringLayout.WEST, next, 0, SpringLayout.WEST, panel);
			} else {
				Component previous = script.get(index - 1);
				setupLayout(previous, next);
			}
		}
		
		script.remove(component);
		panel.remove(component);
		if (pos > index) {
			pos -= 1;
			putSp(pos);
		}
		panel.validate();
		this.repaint();
	}
	
	public void moveCommand(Component component, int index) {
		if (script.indexOf(component) < index) {
			index -= 1;
		}
		removeCommand(component);
		addCommand(component, index);
		
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
	}
	
	public void mouseExited(MouseEvent e) {
	}
	
	public void mouseEntered(MouseEvent e) {
	}
	
	public void mouseReleased(MouseEvent e) {
		
		if (!SwingUtilities.isLeftMouseButton(e)) {
			return;
		}
		
		int index = getIndex(e);
		Component component = e.getComponent();
		System.out.println(component);
		if (index - 1 == script.indexOf(component)) {
			System.out.println("ASDF");
			return;
		}
		
// 		sp.setVisible(false);
		moveCommand(component, index);
// 		int y = e.getY() + 

		
		
	}
	
	public void mousePressed(MouseEvent e) {
		Component component = (Component) e.getSource();
		if (SwingUtilities.isLeftMouseButton(e)) {
			int index = getIndex(e);
			putSp(index);
			pos = index;
		}
		if (SwingUtilities.isMiddleMouseButton(e)) {
			removeCommand(component);
		}
	}
	
	public void mouseDragged(MouseEvent e) {
		int index = getIndex(e);
		putSp(index);
// 		System.out.println(e.getPoint());
// 		Component component = e.getComponent();
// 		int x = component.getX();
// 		int y = component.getY();
// 		Point loc = e.getPoint();
// 		loc.translate(x, y);
// 		System.out.println(panel.getComponentAt(loc));
	}
	
	public void putSp(int index) {
		int length = 31 + padding;
		layout.putConstraint(SpringLayout.NORTH, sp, length*index, SpringLayout.NORTH, panel);
// 		sp.setVisible(true);
		panel.revalidate();
	}
	
	public void putSp(Component component) {
		layout.putConstraint(SpringLayout.NORTH, sp, 1, SpringLayout.NORTH, panel);
// 		sp.setVisible(true);
		panel.revalidate();
	}
	
	public int getIndex(MouseEvent e) {
		Component component = e.getComponent();
		int length = component.getHeight() + padding;
		
		int y = e.getY() + component.getY() + 15;
		
		int index = y / length;
		if (index > script.size()) {
			index = script.size();
		}
		
		return index;
	}
	
	public void mouseMoved(MouseEvent e) {
	}
	
	public String getString(int tabs) {
		String result = new String("");
		for (int i = 0; i < tabs; i++) {
			result += "\t";
		}
		result += protection;
		result += " static ";
		if (returnType == null) {
			result += "void";
		} else {
			result += returnType.name;
		}
		result += " ";
		result += name;
		result += "(";
		
		result += ") {\n";
		tabs += 1;
		for (Iterator<Component> iterator = script.iterator(); iterator.hasNext(); ) {
			Component next = iterator.next();
			Command element = (Command) next;
			result += element.getString(tabs);
		}
		tabs -= 1;
		for (int i = 0; i < tabs; i++) {
			result += "\t";
		}
		result += "}";
		
		return result;
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