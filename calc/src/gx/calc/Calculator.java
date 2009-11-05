// TODO : modes plugin decimal, octal, binary, hex, (miles, km etc)
// TODO : more sample plugins project (dimentions, quick conversions e.g km->miles)
// TODO : setup website + documentation
// TODO : automate plugins, all jars in ./plugins to check manifest for descriptor
// TODO : add googlecode info to about box
// TODO : screenshots for site + usage/sample documentation
// TODO : size / layout needs to be fixed if text size = large/loud
// TODO : plugin dialogue does not persist enabled status properly
// TODO : activate plugin without restart
// TODO : jtxtExpression need to be a label and handled accordingly via global key event handler or with a dialogue
// CHANGES:
//	Math parser added with equations

package gx.calc;

public class Calculator extends javax.swing.JFrame implements CalculatorActionHandler { 
	private static final long serialVersionUID = 1L;

	final int MAX_INPUT_LENGTH = 30;
	final int INPUT_MODE = 0;
	final int RESULT_MODE = 1;
	final int ERROR_MODE = 2;
	int displayMode;
	int numericMode = KEYS.DECIMAL; 

	boolean clearOnNextDigit;
	double lastNumber;
	String lastOperator;
	
	private javax.swing.JMenu jmenuFile, jmenuEdit, jmenuHelp;
	private javax.swing.JMenuItem jmenuitemPlugins, jmenuitemExit, jmenuitemAbout, jmenuitemCopy, jmenuitemPaste;
	private javax.swing.JRadioButtonMenuItem jmenuitemSmall, jmenuitemMedium, jmenuitemLarge, jmenuitemLoud;
	private javax.swing.JLabel jlbOutput;
//private javax.swing.JTextField jtxtExpression;
private javax.swing.JLabel jlblExpression;
	private javax.swing.JPanel jplMaster, jplControl;
	private final javax.swing.JPanel jplMoreButtons;
	
	private CalculatorKey keys[];
	private java.util.ArrayList<CalculatorKey> pluginKeys;
	private CalculatorActionHandler actionHandler = this; 
	
	static final class DISPLAY_SIZE {
		static final int SMALL 			= 10; 
		static final int MEDIUM			= 12; 
		static final int LARGE 			= 16; 
		static final int LOUD 			= 20; 
	}

	static final class KEYS { 
		static final int ZERO 			= 0; 
		static final int ONE 			= 1; 
		static final int TWO			= 2; 
		static final int THREE 			= 3; 
		static final int FOUR 			= 4; 
		static final int FIVE 			= 5; 
		static final int SIX 			= 6; 
		static final int SEVEN	 		= 7; 
		static final int EIGHT		 	= 8; 
		static final int NINE 			= 9; 
		static final int CHANGE_SIGN	= 10; 
		static final int DECIMAL_POINT	= 11; 
		static final int EQUALS 		= 12; 
		static final int DIVIDE 		= 13; 
		static final int MULTIPLY		= 14; 
		static final int MINUS 			= 15; 
		static final int PLUS 			= 16; 
		static final int SQUARE_ROOT 	= 17; 
		static final int POWER 			= 18; 
		static final int PERCENT 		= 19; 
		static final int BACKSPACE 		= 20; 
		static final int CLEAR_EXISTING = 21; 
		static final int CLEAR_ALL 		= 22; 
		static final int MORE_FUNCTIONS	= 23; 
		static final int BIT 			= 24; 
		static final int KILOBIT 		= 25; 
		static final int MEGABIT		= 26; 
		static final int GIGABIT 		= 27; 
		static final int TERABIT 		= 28; 
		static final int BYTE 			= 29; 
		static final int KILOBYTE 		= 30; 
		static final int MEGABYTE		= 31; 
		static final int GIGABYTE 		= 32; 
		static final int TERABYTE 		= 33; 
//		static final int length			= 34; 
static final int EXPRESSION 	= 34; 
static final int EXPRESSION_BRACE_OPEN 	= 35; 
static final int EXPRESSION_BRACE_CLOSE 	= 36; 
static final int EXPRESSION_X 	= 37; 
static final int length			= 38; 
// for future reference
		static final int BINARY 		= 39; 
		static final int OCTAL			= 40; 
		static final int DECIMAL		= 41; 
		static final int HEXADECIMAL	= 42; 
//		static final int length			= 43; 
	}

	public Calculator() {
		Configuration.load();
		try {
			setIconImage(new javax.swing.ImageIcon(getClass().getResource("/gx/calc/calculator.png")).getImage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		jmenuFile = new javax.swing.JMenu("File");
		jmenuFile.setMnemonic(java.awt.event.KeyEvent.VK_F);

		jmenuEdit = new javax.swing.JMenu("Edit");
		jmenuEdit.setMnemonic(java.awt.event.KeyEvent.VK_E);

		jmenuitemPlugins = getMenuItem("Plugins", java.awt.event.KeyEvent.VK_P, jmenuFile);
		jmenuitemExit = getMenuItem("Exit", java.awt.event.KeyEvent.VK_X, jmenuFile); 

		jmenuitemCopy = getMenuItem("Copy", java.awt.event.KeyEvent.VK_C, jmenuEdit);
		jmenuitemPaste = getMenuItem("Paste", java.awt.event.KeyEvent.VK_V, jmenuEdit);
		jmenuEdit.addSeparator();
		jmenuitemSmall = getRdioButtonMenuItem("Small Text", java.awt.event.KeyEvent.VK_S, DISPLAY_SIZE.SMALL, jmenuEdit);
		jmenuitemMedium = getRdioButtonMenuItem("Medium Text", java.awt.event.KeyEvent.VK_M, DISPLAY_SIZE.MEDIUM, jmenuEdit);
		jmenuitemLarge = getRdioButtonMenuItem("Large Text", java.awt.event.KeyEvent.VK_L, DISPLAY_SIZE.LARGE, jmenuEdit);
		jmenuitemLoud = getRdioButtonMenuItem("LoudText", java.awt.event.KeyEvent.VK_O, DISPLAY_SIZE.LOUD, jmenuEdit);
		
		jmenuHelp = new javax.swing.JMenu("Help");
		jmenuHelp.setMnemonic(java.awt.event.KeyEvent.VK_H);

		jmenuitemAbout = new javax.swing.JMenuItem("About Calculator");
		jmenuitemAbout.addActionListener(this);
		jmenuHelp.add(jmenuitemAbout);
			
		javax.swing.JMenuBar menuBar = new javax.swing.JMenuBar();
		menuBar.add(jmenuFile);
		menuBar.add(jmenuEdit);
		menuBar.add(jmenuHelp);
		setJMenuBar(menuBar);
		
		jplMaster = new javax.swing.JPanel();

javax.swing.JPanel jplInOut= new javax.swing.JPanel(new java.awt.BorderLayout());
jlblExpression = new javax.swing.JLabel("", javax.swing.JLabel.RIGHT);
jplInOut.add(jlblExpression, java.awt.BorderLayout.NORTH);
jlblExpression.setVisible(false);
//jtxtExpression = new javax.swing.JTextField("");
//jtxtExpression.setHorizontalAlignment(javax.swing.JLabel.RIGHT);
//jplInOut.add(jtxtExpression, java.awt.BorderLayout.NORTH);
//jtxtExpression.setVisible(false);

		jlbOutput = new javax.swing.JLabel("0", javax.swing.JLabel.RIGHT);
		jlbOutput.setBackground(java.awt.Color.white);
		jlbOutput.setOpaque(true);
		
jplInOut.add(jlbOutput, java.awt.BorderLayout.SOUTH);
getContentPane().add(jplInOut, java.awt.BorderLayout.NORTH);
//		getContentPane().add(jlbOutput, java.awt.BorderLayout.NORTH);

		keys= new CalculatorKey[KEYS.length];
		pluginKeys = new java.util.ArrayList<CalculatorKey>();
		
		for (int i = 0; i <= 9; i++) {
			final Integer j = new Integer(i);
			keys[i] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { addDigitToDisplay(j.intValue()); } }, actionHandler, j.toString());
		}

		keys[KEYS.CHANGE_SIGN] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { processSignChange(); } }, actionHandler, "+/-", "Change sign", '!');
		keys[KEYS.DECIMAL_POINT] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { addDecimalPoint(); } }, actionHandler, ".", "Decimal point");
		keys[KEYS.EQUALS] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { processEquals(); } }, actionHandler, "=", "Equals");
		keys[KEYS.DIVIDE] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { processOperator("/"); } }, actionHandler, "/", "Dvide", '/', "operators");
		keys[KEYS.MULTIPLY] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { processOperator("*"); } }, actionHandler, "*", "Multiply", '*', "operators"); 
		keys[KEYS.MINUS] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { processOperator("-"); } }, actionHandler, "-", "Minus", '-', "operators");
		keys[KEYS.PLUS] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { processOperator("+"); } }, actionHandler, "+", "Plus", '+', "operators"); 
		keys[KEYS.SQUARE_ROOT] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() {
				if (displayMode != ERROR_MODE) {
				   try {
						if (getDisplayString().indexOf("-") == 0)
						    displayError("Invalid input for function!");
						displayResult(Math.sqrt(getNumberInDisplay()));
					}
					catch(Exception ex) {
						displayError("Invalid input for function!");
						displayMode = ERROR_MODE;
					}
				}
			}
		}, actionHandler, "sqrt", "Square root", ' ');

		keys[KEYS.POWER] = new CalculatorKey(new CalculatorAction() {
			public void ActionHandler() {
				if (displayMode != ERROR_MODE){
					try {
						if (getNumberInDisplay() == 0)
							displayError("Cannot divide by zero!");

						displayResult(1 / getNumberInDisplay());
					}
					catch(Exception ex)	{
						displayError("Cannot divide by zero!");
						displayMode = ERROR_MODE;
					}
				}
			}
		}, actionHandler, "1/x", "Power", ' ');

		keys[KEYS.PERCENT] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { processPercent(); } }, actionHandler, "%", "Percent");

		keys[KEYS.BACKSPACE] = new CalculatorKey(new CalculatorAction() {
			public void ActionHandler() {
				if (displayMode != ERROR_MODE){
					setDisplayString(getDisplayString().substring(0,
							  getDisplayString().length() - 1));
					
					if (getDisplayString().length() < 1)
						setDisplayString("0");
				}
			}
		}, actionHandler, "Backspace", "Backspace", ' ');

		keys[KEYS.CLEAR_EXISTING] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { clearExisting(); } }, actionHandler, "CE", "Clear existing", ' '); 
		keys[KEYS.CLEAR_ALL] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { clearAll(); } }, actionHandler, "C", "Clear all", ' ');
keys[KEYS.EXPRESSION] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { 
	jlblExpression.setVisible(!jlblExpression.isVisible());
	jlblExpression.setText(jlblExpression.isVisible() ? " " : "");
	pack();
} }, actionHandler, "Ex", "Expression", ' ', "expression");
		jplMoreButtons = new javax.swing.JPanel(new javax.swing.SpringLayout());

		keys[KEYS.MORE_FUNCTIONS] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { jplMoreButtons.setVisible(!jplMoreButtons.isVisible()); /*Configuration.setWindowExtended(jplMoreButtons.isVisible());*/ pack(); } }, actionHandler, ">>", "More functions", '>', "more");
		keys[KEYS.BIT] = 	  new CalculatorKey(new CalculatorAction() { public void ActionHandler() { setNumericMode(KEYS.BIT); } },  actionHandler, 	   "i", "Bits", 'b', "dataSizes");
		keys[KEYS.KILOBIT] =  new CalculatorKey(new CalculatorAction() { public void ActionHandler() { setNumericMode(KEYS.KILOBIT); } }, actionHandler,  "kB", "Kilobits", 'k', "dataSizes");
		keys[KEYS.MEGABIT] =  new CalculatorKey(new CalculatorAction() { public void ActionHandler() { setNumericMode(KEYS.MEGABIT); } }, actionHandler,  "mB", "Megabits", 'm', "dataSizes");
		keys[KEYS.GIGABIT] =  new CalculatorKey(new CalculatorAction() { public void ActionHandler() { setNumericMode(KEYS.GIGABIT); } }, actionHandler,  "gB", "Gigabits", 'g', "dataSizes");
		keys[KEYS.TERABIT] =  new CalculatorKey(new CalculatorAction() { public void ActionHandler() { setNumericMode(KEYS.TERABIT); } }, actionHandler,  "tB", "Terabits", 't', "dataSizes");
		keys[KEYS.BYTE] = 	  new CalculatorKey(new CalculatorAction() { public void ActionHandler() { setNumericMode(KEYS.BYTE); } },  actionHandler, 	   "B", "Bytes", 'B', "dataSizes");
		keys[KEYS.KILOBYTE] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { setNumericMode(KEYS.KILOBYTE); } }, actionHandler, "KB", "Kilobytes", 'K', "dataSizes");
		keys[KEYS.MEGABYTE] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { setNumericMode(KEYS.MEGABYTE); } }, actionHandler, "MB", "Megabytes", 'M', "dataSizes");
		keys[KEYS.GIGABYTE] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { setNumericMode(KEYS.GIGABYTE); } }, actionHandler, "GB", "Gigabytes", 'G', "dataSizes");
		keys[KEYS.TERABYTE] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { setNumericMode(KEYS.TERABYTE); } }, actionHandler, "TB", "Terabytes", 'T', "dataSizes");

		jplControl = new javax.swing.JPanel();
		jplControl.setLayout(new java.awt.FlowLayout());

		jplControl.add(keys[KEYS.BACKSPACE].getButton());
		jplControl.add(keys[KEYS.CLEAR_EXISTING].getButton());
		jplControl.add(keys[KEYS.CLEAR_ALL].getButton());
jplControl.add(keys[KEYS.EXPRESSION].getButton());
		jplControl.add(keys[KEYS.MORE_FUNCTIONS].getButton());

		javax.swing.JPanel jplButtons = new javax.swing.JPanel();
		javax.swing.JPanel jplPluginButtons = new javax.swing.JPanel();
		javax.swing.JPanel jplDataButtons = new javax.swing.JPanel();
		
		jplButtons.setLayout(new java.awt.GridLayout(5, 4, 2, 2));
		
		jplButtons.add(keys[KEYS.SQUARE_ROOT].getButton());
		jplButtons.add(keys[KEYS.POWER].getButton());
		jplButtons.add(keys[KEYS.PERCENT].getButton());
		jplButtons.add(keys[KEYS.EQUALS].getButton());
		jplButtons.add(keys[KEYS.SEVEN].getButton());
		jplButtons.add(keys[KEYS.EIGHT].getButton());
		jplButtons.add(keys[KEYS.NINE].getButton());
		jplButtons.add(keys[KEYS.DIVIDE].getButton());
		jplButtons.add(keys[KEYS.FOUR].getButton());
		jplButtons.add(keys[KEYS.FIVE].getButton());
		jplButtons.add(keys[KEYS.SIX].getButton());
		jplButtons.add(keys[KEYS.MULTIPLY].getButton());
		jplButtons.add(keys[KEYS.ONE].getButton());
		jplButtons.add(keys[KEYS.TWO].getButton());
		jplButtons.add(keys[KEYS.THREE].getButton());
		jplButtons.add(keys[KEYS.MINUS].getButton());
		jplButtons.add(keys[KEYS.CHANGE_SIGN].getButton());
		jplButtons.add(keys[KEYS.ZERO].getButton());
		jplButtons.add(keys[KEYS.DECIMAL_POINT].getButton());
		jplButtons.add(keys[KEYS.PLUS].getButton());
		
		jplDataButtons.setLayout(new java.awt.GridLayout(5, 2, 2, 2));
		jplDataButtons.add(keys[KEYS.BIT].getButton());
		jplDataButtons.add(keys[KEYS.BYTE].getButton());
		jplDataButtons.add(keys[KEYS.KILOBIT].getButton());
		jplDataButtons.add(keys[KEYS.KILOBYTE].getButton());
		jplDataButtons.add(keys[KEYS.MEGABIT].getButton());
		jplDataButtons.add(keys[KEYS.MEGABYTE].getButton());
		jplDataButtons.add(keys[KEYS.GIGABIT].getButton());
		jplDataButtons.add(keys[KEYS.GIGABYTE].getButton());
		jplDataButtons.add(keys[KEYS.TERABIT].getButton());
		jplDataButtons.add(keys[KEYS.TERABYTE].getButton());

		// init toggle state of bit mode
		keys[KEYS.BIT].getButton().setSelected(true);

		jplMaster.setLayout(new java.awt.BorderLayout());
		jplMaster.add(jplControl, java.awt.BorderLayout.WEST);
		javax.swing.JPanel btns = new javax.swing.JPanel();	
		btns.setLayout(new java.awt.BorderLayout(2,2));
		btns.add(jplButtons, java.awt.BorderLayout.WEST);
		btns.add(jplMoreButtons, java.awt.BorderLayout.EAST);
		jplMaster.add(btns, java.awt.BorderLayout.SOUTH);

		// Add components to frame
		getContentPane().add(jplMaster, java.awt.BorderLayout.SOUTH);
		
		clearAll();
		setTitle(Configuration.APPLICATION_NAME);
		
		if(Configuration.getWindowLocation() != null)
			setLocation(Configuration.getWindowLocation());
		else
			setLocationRelativeTo(null);

//		keys[KEYS.MORE_FUNCTIONS].getButton().setSelected(Configuration.isWindowExtended());
//		jplMoreButtons.setVisible(Configuration.isWindowExtended());
		
		initPlugins(jplPluginButtons);

		jplMoreButtons.add(jplDataButtons);
		jplMoreButtons.add(jplPluginButtons);
		SpringUtilities.makeCompactGrid(jplMoreButtons, 1, 2, 2, 2, 2, 2);
		setResizable(false);
		setVisible(true);
		setTextSize(this);
		addWindowListener(this);
		pack(); 
		// TODO : this has no effect, works for menu actions but not here :( buttons still half hidden at startup
		// for some strange reason packing doesent seem to work as expected, hiding then showing panel helps 
		jplMoreButtons.setVisible(!jplMoreButtons.isVisible()); 
		jplMoreButtons.setVisible(!jplMoreButtons.isVisible());
		pack();
		setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(
		        new java.awt.KeyEventDispatcher() {
		            public boolean dispatchKeyEvent(java.awt.event.KeyEvent e) {
		            	if(hasFocus() && e.getID() == java.awt.event.KeyEvent.KEY_PRESSED)
		            		return doKeyPressed(e);
		            	return false;
		            }
		        });
		

	}	//End of Constructor Calculator
	
	private javax.swing.JMenuItem getMenuItem(String text, int keyEvent, javax.swing.JMenu parentMenu) {
		javax.swing.JMenuItem mi = new javax.swing.JMenuItem(text);
		mi.setAccelerator(javax.swing.KeyStroke.getKeyStroke(keyEvent, java.awt.event.ActionEvent.CTRL_MASK));
		mi.addActionListener(this);
		if(parentMenu != null)
			parentMenu.add(mi);
		return mi;
	}

	private javax.swing.JRadioButtonMenuItem getRdioButtonMenuItem(String text, int keyEvent, int size, javax.swing.JMenu parentMenu) {
		javax.swing.JRadioButtonMenuItem mi = new javax.swing.JRadioButtonMenuItem(text);
		mi.setFont(new java.awt.Font(mi.getFont().getName(), mi.getFont().getStyle(), size));
		mi.setAccelerator(javax.swing.KeyStroke.getKeyStroke(keyEvent, java.awt.event.ActionEvent.CTRL_MASK));
		mi.setSelected(Configuration.getDisplaySize() == size);
		mi.addActionListener(this);
		if(parentMenu != null)
			parentMenu.add(mi);
		return mi;
	}

	private void initPlugins(javax.swing.JPanel p){
		String failures = "";
		int addedButtons = 0;
		for(CalculatorPluginDescriptor d : Configuration.getPlugins()) {
			if(d.enabled)
				try {
					java.io.File f = new java.io.File(d.location);
					if(!f.exists())
						throw new java.io.FileNotFoundException();
					java.net.URLClassLoader clazzLoader = new java.net.URLClassLoader(new java.net.URL[]{f.toURI().toURL()});
					final Object o = clazzLoader.loadClass(d.className).newInstance();
					if(o instanceof CalculatorPlugin) {
						CalculatorKey c = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { 
							doCalculation((CalculatorPlugin)o); 
							} } , actionHandler, d.displayValue, 
								d.descriptionValue, d.keyBinding );
						p.add(c.getButton());
						pluginKeys.add(c);
						d.loaded = true;
						addedButtons++;
					}
			    } catch (Exception e) {
			    	failures+= "\n";
			    	failures += d.descriptionValue + " - " + e.getClass().getName() + " " + e.getMessage();
			        e.printStackTrace();
			    }
		}
    	if(failures.length() > 0)
    		javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(),
					"Some plugins failed to load: '" + failures, "Load plugin failed",
					javax.swing.JOptionPane.OK_OPTION);
    	if(addedButtons > 0) {
    		int rows = 5;
    		int cols = 1;
    		if(addedButtons > 5) {
    			cols = (int)Math.ceil((double)addedButtons / 5);
    		}
    		if(addedButtons % 5 != 0) 
	    		for(int i = 0; i < addedButtons - (cols * 5); i++)
	    			p.add(new javax.swing.JLabel()); // dummy components
    		
    		p.setLayout(new java.awt.GridLayout(rows, cols, 2, 2));
    	}
	}

	private void setNumericMode(int numericMode){
		for(int i = 0; i < KEYS.length; i++) 
			if(keys[i].isToggleGroup("dataSizes")) 
				keys[i].getButton().setSelected(i == numericMode);
		
		double result = getNumberInDisplay();
		result = toBits(result, this.numericMode);
		this.numericMode = numericMode;
		result = fromBits(result, this.numericMode);
		setDisplayString(String.valueOf(result));
	}
	
	private void doCalculation(CalculatorPlugin p) {
		double result = getNumberInDisplay();
		result = p.process(result);
		setDisplayString(String.valueOf(result));
	}

	private double toBits(double value, int currentNumericMode){
		double result = 0;
		switch(currentNumericMode) {
		case KEYS.BIT :
			result = value;
			break;
		case KEYS.KILOBIT :
			result = value * 1000;
			break;
		case KEYS.MEGABIT :
			result = toBits(value, KEYS.KILOBIT) * 1000;
			break;
		case KEYS.GIGABIT :
			result = toBits(value, KEYS.MEGABIT) * 1000;
			break;
		case KEYS.TERABIT :
			result = toBits(value, KEYS.GIGABIT) * 1000;
			break;
		case KEYS.BYTE :
			result = value * 8;
			break;
		case KEYS.KILOBYTE :
			result = toBits(value, KEYS.BYTE) * 1024;
			break;
		case KEYS.MEGABYTE :
			result = toBits(value, KEYS.KILOBYTE) * 1024;
			break;
		case KEYS.GIGABYTE :
			result = toBits(value, KEYS.MEGABYTE) * 1024;
			break;
		case KEYS.TERABYTE :
			result = toBits(value, KEYS.GIGABYTE) * 1024;
			break;
		}
		
		return result;
	}

	private double fromBits(double value, int currentNumericMode){
		double result = 0;
		switch(currentNumericMode) {
		case KEYS.BIT :
			result = value;
			break;
		case KEYS.KILOBIT :
			result = value / 1000;
			break;
		case KEYS.MEGABIT :
			result = fromBits(value, KEYS.KILOBIT) / 1000;
			break;
		case KEYS.GIGABIT :
			result = fromBits(value, KEYS.MEGABIT) / 1000;
			break;
		case KEYS.TERABIT :
			result = fromBits(value, KEYS.GIGABIT) / 1000;
			break;
		case KEYS.BYTE :
			result = value / 8;
			break;
		case KEYS.KILOBYTE :
			result = fromBits(value, KEYS.BYTE) / 1024;
			break;
		case KEYS.MEGABYTE :
			result = fromBits(value, KEYS.KILOBYTE) / 1024;
			break;
		case KEYS.GIGABYTE :
			result = fromBits(value, KEYS.MEGABYTE) / 1024;
			break;
		case KEYS.TERABYTE :
			result = fromBits(value, KEYS.GIGABYTE) / 1024;
			break;
		}
		
		return result;
	}

	private void setDisplayString(String s){
		
		if(s.endsWith(".0"))
			s = s.substring(0, s.length() - 2);
		jlbOutput.setText(s);
	}

	private String getDisplayString () {
		return jlbOutput.getText();
	}

	private void addDigitToDisplay(int digit) {
		if(jlblExpression.isVisible()){
			jlblExpression.setText(jlblExpression.getText() + digit);
		}
		else {
			if (clearOnNextDigit)
				setDisplayString("");
			
			String inputString = getDisplayString();
			
			if (inputString.indexOf("0") == 0) 
				inputString = inputString.substring(1);
			
			if ((!inputString.equals("0") || digit > 0) && inputString.length() < MAX_INPUT_LENGTH)
				setDisplayString(inputString + digit);
			
			displayMode = INPUT_MODE;
			clearOnNextDigit = false;
		}
	}

	private void addDecimalPoint() {
		if(jlblExpression.isVisible()){
			jlblExpression.setText(jlblExpression.getText() + ".");
		}
		else {			
		displayMode = INPUT_MODE;

		if (clearOnNextDigit)
			setDisplayString("");

		String inputString = getDisplayString();
	
		// If the input string already contains a decimal point, don't do anything to it.
		if (inputString.indexOf(".") < 0)
			setDisplayString(inputString + ".");
		}
	}
	
	private void processSignChange() {
		if (displayMode == INPUT_MODE) {
			String input = getDisplayString();

			if (input.length() > 0 && !input.equals("0")) {
				if (input.indexOf("-") == 0)
					setDisplayString(input.substring(1));
				else
					setDisplayString("-" + input);
			}
		}
		else if (displayMode == RESULT_MODE && getNumberInDisplay() != 0)
			displayResult(-getNumberInDisplay());
	}

	private void clearAll()	{
		setDisplayString("0");
		lastOperator = "0";
		lastNumber = 0;
		displayMode = INPUT_MODE;
		clearOnNextDigit = true;
	}

	private void clearExisting(){
		setDisplayString("0");
		clearOnNextDigit = true;
		displayMode = INPUT_MODE;
	}

	private double getNumberInDisplay()	{
		String input = jlbOutput.getText();
		return Double.parseDouble(input);
	}

	private void processOperator(String op) {
		if(jlblExpression.isVisible()){
			jlblExpression.setText(jlblExpression.getText() + op);
		}
		else {			

		for(int i = 0; i < KEYS.length; i++) {
			if(keys[i].isToggleGroup("operators")) 
				keys[i].getButton().setSelected(keys[i].getButton().getText() == op);
		}

		if (displayMode != ERROR_MODE) {
			double numberInDisplay = getNumberInDisplay();

			if (!lastOperator.equals("0"))	 {
				try {
					double result = processLastOperator();
					displayResult(result);
					lastNumber = result;
				}
				catch (DivideByZeroException e) { }
			}
			else {
				lastNumber = numberInDisplay;
			}
			
			clearOnNextDigit = true;
			lastOperator = op;
		}
		}
	}

	private void processPercent(){
		double result = 0;

		if (displayMode != ERROR_MODE){
			try	{
				result = getNumberInDisplay() / 100;
				displayResult(result);
			}
			catch(Exception ex)	{
				displayError("Invalid input for function!");
				displayMode = ERROR_MODE;
			}
		}
	}

	private void processEquals(){
		double result = 0;

		for(int i = 0; i < KEYS.length; i++) {
			if(keys[i].isToggleGroup("operators")) 
				keys[i].getButton().setSelected(false);
		}

		if (displayMode != ERROR_MODE){
			try  {
				result = processLastOperator();
				displayResult(result);
				String exp = jlblExpression.getText();
				if(exp.length() > 0)
					setDisplayString(String.valueOf(MathParser.processEquation(exp.replaceAll("[xX]", jlbOutput.getText()))));
			}
			catch (DivideByZeroException e)	{
				displayError("Cannot divide by zero!");
			}
			catch (NumberFormatException e)	{
				displayError("Invalid expression!");
			}

			lastOperator = "0";
		}
	}
	
	private double processLastOperator() throws DivideByZeroException {
		if(!lastOperator.equals("0"))
			return Double.valueOf(MathParser.processEquation(lastNumber + lastOperator + getNumberInDisplay()));
		else return getNumberInDisplay();		
	}

	private void displayResult(double result) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("###.########");
		setDisplayString(df.format(result));
		lastNumber = result;
		displayMode = RESULT_MODE;
		clearOnNextDigit = true;
	}

	private void displayError(String errorMessage) {
		setDisplayString(errorMessage);
		lastNumber = 0;
		displayMode = ERROR_MODE;
		clearOnNextDigit = true;
	}
	
	private void setTextSize(java.awt.Container container) {
		java.awt.Font f = container.getFont();
		container.setFont(new java.awt.Font(f.getName(), f.getStyle(), Configuration.getDisplaySize()));
		for(java.awt.Component c : container.getComponents()) 
			setTextSize((java.awt.Container)c);
	}
	
	private void setTextSize(int size, java.awt.Container container) {
		if(Configuration.getDisplaySize() != size)
			Configuration.setDisplaySize(size);
		setTextSize(container);
	}

	public void writeToClipboard(String s) {
	  java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new java.awt.datatransfer.StringSelection(s), null);
	}

	public String copyFromClipboard() {
		String tempString = null;
		java.awt.datatransfer.Transferable clipboardContent = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this);
		if ((clipboardContent != null) &&  (clipboardContent.isDataFlavorSupported (java.awt.datatransfer.DataFlavor.stringFlavor))) {
			try {
				tempString = (String) clipboardContent.getTransferData(java.awt.datatransfer.DataFlavor.stringFlavor);
				// ensure it is numeric
				Double.valueOf(tempString);
           }
           catch (Exception e) {
        	   tempString = null;
           }
		}
		return tempString;
	}

	public void actionPerformed(java.awt.event.ActionEvent e) {
		if(e.getSource() == jmenuitemAbout){
				new AboutDialog(this);
		}else if(e.getSource() == jmenuitemPlugins){
			new PluginsDialog(this);
		}else if(e.getSource() == jmenuitemExit){
			System.exit(0);
		}else if(e.getSource() == jmenuitemCopy || e.getActionCommand() == "Copy"){
			writeToClipboard(getDisplayString());
		}else if(e.getSource() == jmenuitemPaste || e.getActionCommand() == "Paste"){
			String s = copyFromClipboard();
			if(s != null)
				setDisplayString(s);
		}else if(e.getSource() == jmenuitemSmall){
			setTextSize(DISPLAY_SIZE.SMALL, this);
			jmenuitemSmall.setSelected(true);
			jmenuitemMedium.setSelected(false);
			jmenuitemLarge.setSelected(false);
			jmenuitemLoud.setSelected(false);
			pack();
			// for some strange reason packing doesent seem to work as expected, hiding then showing panel helps 
			jplMoreButtons.setVisible(!jplMoreButtons.isVisible()); 
			jplMoreButtons.setVisible(!jplMoreButtons.isVisible());
			pack();
		}else if(e.getSource() == jmenuitemMedium){
			setTextSize(DISPLAY_SIZE.MEDIUM, this);
			jmenuitemSmall.setSelected(false);
			jmenuitemMedium.setSelected(true);
			jmenuitemLarge.setSelected(false);
			jmenuitemLoud.setSelected(false);
			pack();
			// for some strange reason packing doesent seem to work as expected, hiding then showing panel helps 
			jplMoreButtons.setVisible(!jplMoreButtons.isVisible()); 
			jplMoreButtons.setVisible(!jplMoreButtons.isVisible());
			pack();
		}else if(e.getSource() == jmenuitemLarge){
			setTextSize(DISPLAY_SIZE.LARGE, this);
			jmenuitemSmall.setSelected(false);
			jmenuitemMedium.setSelected(false);
			jmenuitemLarge.setSelected(true);
			jmenuitemLoud.setSelected(false);
			pack();
			// for some strange reason packing doesent seem to work as expected, hiding then showing panel helps 
			jplMoreButtons.setVisible(!jplMoreButtons.isVisible()); 
			jplMoreButtons.setVisible(!jplMoreButtons.isVisible());
			pack();
		}else if(e.getSource() == jmenuitemLoud){
			setTextSize(DISPLAY_SIZE.LOUD, this);
			jmenuitemSmall.setSelected(false);
			jmenuitemMedium.setSelected(false);
			jmenuitemLarge.setSelected(false);
			jmenuitemLoud.setSelected(true);
			pack();
			// for some strange reason packing doesent seem to work as expected, hiding then showing panel helps 
			jplMoreButtons.setVisible(!jplMoreButtons.isVisible()); 
			jplMoreButtons.setVisible(!jplMoreButtons.isVisible());
			pack();
		}	

		for(int i = 0; i < KEYS.length; i++)
			if(keys[i] != null && e.getSource() == keys[i].getButton()) {
				keys[i].doAction();
				break;
			}
		
		for(int i = 0; i < pluginKeys.size(); i++)
			if(e.getSource() == pluginKeys.get(i).getButton()) {
				pluginKeys.get(i).doAction();
				break;
			}
	}

	private boolean doKeyPressed(java.awt.event.KeyEvent e) {
		boolean actionTaken = false;
		char c = e.getKeyChar(); 
		switch(e.getKeyCode()) {
		case java.awt.event.KeyEvent.VK_ENTER :
			keys[KEYS.EQUALS].doAction();
			actionTaken = true;
			break;
		case java.awt.event.KeyEvent.VK_BACK_SPACE :
			keys[KEYS.BACKSPACE].doAction();
			actionTaken = true;
			break;
		case java.awt.event.KeyEvent.VK_ESCAPE :
			keys[KEYS.CLEAR_ALL].doAction();
			actionTaken = true;
			break;
		default:
			if(c != ' ') {
				for(int i = 0; i < KEYS.length; i++)
					if(c == keys[i].getKeyChar()) {
						keys[i].doAction();
						actionTaken = true;
						break;
					}
				if(!actionTaken)
					for(int i = 0; i < pluginKeys.size(); i++)
						if(c == pluginKeys.get(i).getKeyChar()) {
							pluginKeys.get(i).doAction();
							actionTaken = true;
							break;
						}
			}
			break;
		}
		
		return actionTaken;
	}
/*
	public static void main(String args[]) {
		new Calculator();
	}
 */

	@Override
	public void windowClosing(java.awt.event.WindowEvent e) {
		Configuration.setWindowLocation(getLocation());
		Configuration.store();
		System.exit(0);
	}
	
	public void keyPressed(java.awt.event.KeyEvent e) { doKeyPressed(e); }
	public void keyReleased(java.awt.event.KeyEvent e) { }
	public void keyTyped(java.awt.event.KeyEvent e) { }
	@Override
	public void windowClosed(java.awt.event.WindowEvent e) { }
	@Override
	public void windowActivated(java.awt.event.WindowEvent e) { }
	@Override
	public void windowDeactivated(java.awt.event.WindowEvent e) { }
	@Override
	public void windowDeiconified(java.awt.event.WindowEvent e) { }
	@Override
	public void windowIconified(java.awt.event.WindowEvent e) { }
	@Override
	public void windowOpened(java.awt.event.WindowEvent e) { }

}		//End of Swing Calculator Class.

class DivideByZeroException extends Exception{
	private static final long serialVersionUID = 1L;
	public DivideByZeroException() { super(); }
	public DivideByZeroException(String s) { super(s); }
}

interface CalculatorActionHandler extends java.awt.event.ActionListener, java.awt.event.KeyListener , java.awt.event.WindowListener { }
interface CalculatorAction { void ActionHandler(); }
 