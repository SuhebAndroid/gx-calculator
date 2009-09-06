// TODO : modes plugin decimal, octal, binary, hex, (miles, km etc)
// TODO : samples plugin (vat 15%, dimentions 1/2 1/4, quick conversions e.g km->miles) 
// TODO : setup website
// TODO : automate plugins, all jars in ./plugins to check manifest for descriptor

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
	private javax.swing.JPanel jplMaster, jplControl;
	
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
		static final int length			= 34; 
// for future reference
		static final int BINARY 		= 34; 
		static final int OCTAL			= 35; 
		static final int DECIMAL		= 36; 
		static final int HEXADECIMAL	= 37; 
//		static final int length			= 38; 
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

		jmenuitemPlugins = new javax.swing.JMenuItem("Plugins");
		jmenuitemPlugins.setAccelerator(javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_P, java.awt.event.ActionEvent.CTRL_MASK));
		jmenuitemPlugins.addActionListener(this);
		jmenuFile.add(jmenuitemPlugins);

		jmenuitemExit = new javax.swing.JMenuItem("Exit");
		jmenuitemExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_X, java.awt.event.ActionEvent.CTRL_MASK));
		jmenuitemExit.addActionListener(this);
		jmenuFile.add(jmenuitemExit);


		jmenuEdit = new javax.swing.JMenu("Edit");
		jmenuEdit.setMnemonic(java.awt.event.KeyEvent.VK_E);

		jmenuitemCopy = new javax.swing.JMenuItem("Copy");
		jmenuitemCopy.setAccelerator(javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_C, java.awt.event.ActionEvent.CTRL_MASK));
		jmenuitemCopy.addActionListener(this);
		jmenuEdit.add(jmenuitemCopy);

		jmenuitemPaste = new javax.swing.JMenuItem("Paste");
		jmenuitemPaste.setAccelerator(javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_V, java.awt.event.ActionEvent.CTRL_MASK));
		jmenuitemPaste.addActionListener(this);
		jmenuEdit.add(jmenuitemPaste);
		
		jmenuEdit.addSeparator();

		jmenuitemSmall = new javax.swing.JRadioButtonMenuItem("Small Text");
		jmenuitemSmall.setAccelerator(javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_S, java.awt.event.ActionEvent.CTRL_MASK));
		jmenuitemSmall.setFont(new java.awt.Font(jmenuitemSmall.getFont().getName(), jmenuitemSmall.getFont().getStyle(), DISPLAY_SIZE.SMALL));
		jmenuitemSmall.addActionListener(this);
		jmenuitemSmall.setSelected(Configuration.getDisplaySize() == DISPLAY_SIZE.SMALL);
		jmenuEdit.add(jmenuitemSmall);
		
		jmenuitemMedium = new javax.swing.JRadioButtonMenuItem("Medium Text");
		jmenuitemMedium.setAccelerator(javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_M, java.awt.event.ActionEvent.CTRL_MASK));
		jmenuitemMedium.setFont(new java.awt.Font(jmenuitemMedium.getFont().getName(), jmenuitemMedium.getFont().getStyle(), DISPLAY_SIZE.MEDIUM));
		jmenuitemMedium.setSelected(Configuration.getDisplaySize() == DISPLAY_SIZE.MEDIUM);
		jmenuitemMedium.addActionListener(this);
		jmenuEdit.add(jmenuitemMedium);
		
		jmenuitemLarge = new javax.swing.JRadioButtonMenuItem("Large Text");
		jmenuitemLarge.setAccelerator(javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_L, java.awt.event.ActionEvent.CTRL_MASK));
		jmenuitemLarge.setFont(new java.awt.Font(jmenuitemLarge.getFont().getName(), jmenuitemLarge.getFont().getStyle(), DISPLAY_SIZE.LARGE));
		jmenuitemLarge.setSelected(Configuration.getDisplaySize() == DISPLAY_SIZE.LARGE);
		jmenuitemLarge.addActionListener(this);
		jmenuEdit.add(jmenuitemLarge);
		
		jmenuitemLoud = new javax.swing.JRadioButtonMenuItem("Loud Text");
		jmenuitemLoud.setAccelerator(javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_O, java.awt.event.ActionEvent.CTRL_MASK));
		jmenuitemLoud.setFont(new java.awt.Font(jmenuitemLoud.getFont().getName(), jmenuitemLoud.getFont().getStyle(), DISPLAY_SIZE.LOUD));
		jmenuitemLoud.setSelected(Configuration.getDisplaySize() == DISPLAY_SIZE.LOUD);
		jmenuitemLoud.addActionListener(this);
		jmenuEdit.add(jmenuitemLoud);
		
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

		jlbOutput = new javax.swing.JLabel("0", javax.swing.JLabel.RIGHT);
		jlbOutput.setBackground(java.awt.Color.white);
		jlbOutput.setOpaque(true);
		
		getContentPane().add(jlbOutput, java.awt.BorderLayout.NORTH);

		keys= new CalculatorKey[KEYS.length];
		pluginKeys = new java.util.ArrayList<CalculatorKey>();
		
		for (int i = 0; i <= 9; i++) {
			final Integer j = new Integer(i);
			keys[i] = new CalculatorKey(j.toString(), new CalculatorAction() { public void ActionHandler() { addDigitToDisplay(j.intValue()); } }, actionHandler);
		}

		keys[KEYS.CHANGE_SIGN] = new CalculatorKey("+/-", "Change sign", '!', new CalculatorAction() { public void ActionHandler() { processSignChange(); } }, actionHandler);
		keys[KEYS.DECIMAL_POINT] = new CalculatorKey(".", "Decimal point", new CalculatorAction() { public void ActionHandler() { addDecimalPoint(); } }, actionHandler);
		keys[KEYS.EQUALS] = new CalculatorKey("=", "Equals", new CalculatorAction() { public void ActionHandler() { processEquals(); } }, actionHandler);
		keys[KEYS.DIVIDE] = new CalculatorKey("/", "Dvide", '/', new CalculatorAction() { public void ActionHandler() { processOperator("/"); } }, actionHandler, "operators");
		keys[KEYS.MULTIPLY] = new CalculatorKey("*", "Multiply", '*', new CalculatorAction() { public void ActionHandler() { processOperator("*"); } }, actionHandler, "operators"); 
		keys[KEYS.MINUS] = new CalculatorKey("-", "Minus", '-', new CalculatorAction() { public void ActionHandler() { processOperator("-"); } }, actionHandler, "operators");
		keys[KEYS.PLUS] = new CalculatorKey("+", "Plus", '+', new CalculatorAction() { public void ActionHandler() { processOperator("+"); } }, actionHandler, "operators"); 
		keys[KEYS.SQUARE_ROOT] = new CalculatorKey("sqrt", "Square root", ' ', new CalculatorAction() { public void ActionHandler() {
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
		}, actionHandler);

		keys[KEYS.POWER] = new CalculatorKey("1/x", "Power", ' ', new CalculatorAction() {
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
		}, actionHandler);

		keys[KEYS.PERCENT] = new CalculatorKey("%", "Percent", new CalculatorAction() { public void ActionHandler() { processPercent(); } }, actionHandler);

		keys[KEYS.BACKSPACE] = new CalculatorKey("Backspace", "Backspace", ' ', new CalculatorAction() {
			public void ActionHandler() {
				if (displayMode != ERROR_MODE){
					setDisplayString(getDisplayString().substring(0,
							  getDisplayString().length() - 1));
					
					if (getDisplayString().length() < 1)
						setDisplayString("0");
				}
			}
		}, actionHandler);

		keys[KEYS.CLEAR_EXISTING] = new CalculatorKey("CE", "Clear existing", ' ', new CalculatorAction() { public void ActionHandler() { clearExisting(); } }, actionHandler); 
		keys[KEYS.CLEAR_ALL] = new CalculatorKey("C", "Clear all", ' ', new CalculatorAction() { public void ActionHandler() { clearAll(); } }, actionHandler);

		final javax.swing.JPanel jplMoreButtons = new javax.swing.JPanel(new javax.swing.SpringLayout());

		keys[KEYS.MORE_FUNCTIONS] = new CalculatorKey(">>", "More functions", '>', new CalculatorAction() { public void ActionHandler() { jplMoreButtons.setVisible(!jplMoreButtons.isVisible()); Configuration.setWindowExtended(jplMoreButtons.isVisible()); pack(); } }, actionHandler, "more");
		keys[KEYS.BIT] = new CalculatorKey("i", "Bits", 'b', new CalculatorAction() { public void ActionHandler() { setNumericMode(KEYS.BIT); } }, actionHandler, "dataSizes");
		keys[KEYS.KILOBIT] = new CalculatorKey("kB", "Kilobits", 'k', new CalculatorAction() { public void ActionHandler() { setNumericMode(KEYS.KILOBIT); } }, actionHandler, "dataSizes");
		keys[KEYS.MEGABIT] = new CalculatorKey("mB", "Megabits", 'm', new CalculatorAction() { public void ActionHandler() { setNumericMode(KEYS.MEGABIT); } }, actionHandler, "dataSizes");
		keys[KEYS.GIGABIT] = new CalculatorKey("gB", "Gigabits", 'g', new CalculatorAction() { public void ActionHandler() { setNumericMode(KEYS.GIGABIT); } }, actionHandler, "dataSizes");
		keys[KEYS.TERABIT] = new CalculatorKey("tB", "Terabits", 't', new CalculatorAction() { public void ActionHandler() { setNumericMode(KEYS.TERABIT); } }, actionHandler, "dataSizes");
		keys[KEYS.BYTE] = new CalculatorKey("B", "Bytes", 'B', new CalculatorAction() { public void ActionHandler() { setNumericMode(KEYS.BYTE); } }, actionHandler, "dataSizes");
		keys[KEYS.KILOBYTE] = new CalculatorKey("KB", "Kilobytes", 'K', new CalculatorAction() { public void ActionHandler() { setNumericMode(KEYS.KILOBYTE); } }, actionHandler, "dataSizes");
		keys[KEYS.MEGABYTE] = new CalculatorKey("MB", "Megabytes", 'M', new CalculatorAction() { public void ActionHandler() { setNumericMode(KEYS.MEGABYTE); } }, actionHandler, "dataSizes");
		keys[KEYS.GIGABYTE] = new CalculatorKey("GB", "Gigabytes", 'G', new CalculatorAction() { public void ActionHandler() { setNumericMode(KEYS.GIGABYTE); } }, actionHandler, "dataSizes");
		keys[KEYS.TERABYTE] = new CalculatorKey("TB", "Terabytes", 'T', new CalculatorAction() { public void ActionHandler() { setNumericMode(KEYS.TERABYTE); } }, actionHandler, "dataSizes");

		jplControl = new javax.swing.JPanel();
		jplControl.setLayout(new java.awt.FlowLayout());

		jplControl.add(keys[KEYS.BACKSPACE].getButton());
		jplControl.add(keys[KEYS.CLEAR_EXISTING].getButton());
		jplControl.add(keys[KEYS.CLEAR_ALL].getButton());
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

		keys[KEYS.MORE_FUNCTIONS].getButton().setSelected(Configuration.isWindowExtended());
		jplMoreButtons.setVisible(Configuration.isWindowExtended());
		
		initPlugins(jplPluginButtons);

		jplMoreButtons.add(jplDataButtons);
		jplMoreButtons.add(jplPluginButtons);
		SpringUtilities.makeCompactGrid(jplMoreButtons, 1, 2, 2, 2, 2, 2);
		setResizable(false);
		setVisible(true);
		setTextSize(this);
		pack();
		addWindowListener(this);
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
						CalculatorKey c = new CalculatorKey(d.displayValue , d.descriptionValue, d.keyBinding, 
								new CalculatorAction() { public void ActionHandler() { 
									doCalculation((CalculatorPlugin)o); 
									} }, actionHandler );
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
		for(int i = 0; i < keys.length; i++) 
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

	private void addDecimalPoint() {
		displayMode = INPUT_MODE;

		if (clearOnNextDigit)
			setDisplayString("");

		String inputString = getDisplayString();
	
		// If the input string already contains a decimal point, don't do anything to it.
		if (inputString.indexOf(".") < 0)
			setDisplayString(inputString + ".");
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
		for(int i = 0; i < keys.length; i++) {
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

		for(int i = 0; i < keys.length; i++) {
			if(keys[i].isToggleGroup("operators")) 
				keys[i].getButton().setSelected(false);
		}

		if (displayMode != ERROR_MODE){
			try  {
				result = processLastOperator();
				displayResult(result);
			}
			catch (DivideByZeroException e)	{
				displayError("Cannot divide by zero!");
			}

			lastOperator = "0";
		}
	}
	
	private double processLastOperator() throws DivideByZeroException {
		double result = 0;
		double numberInDisplay = getNumberInDisplay();

		if (lastOperator.equals("/")) {
			if (numberInDisplay == 0)
				throw (new DivideByZeroException());

			result = lastNumber / numberInDisplay;
		}
			
		if (lastOperator.equals("*"))
			result = lastNumber * numberInDisplay;

		if (lastOperator.equals("-"))
			result = lastNumber - numberInDisplay;

		if (lastOperator.equals("+"))
			result = lastNumber + numberInDisplay;

		return result;
	}

	private void displayResult(double result) {
		setDisplayString(Double.toString(result));
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
				new AboutDialog(this, "About GX Calculator", true);
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
		}else if(e.getSource() == jmenuitemMedium){
			setTextSize(DISPLAY_SIZE.MEDIUM, this);
			jmenuitemSmall.setSelected(false);
			jmenuitemMedium.setSelected(true);
			jmenuitemLarge.setSelected(false);
			jmenuitemLoud.setSelected(false);
			pack();
		}else if(e.getSource() == jmenuitemLarge){
			setTextSize(DISPLAY_SIZE.LARGE, this);
			jmenuitemSmall.setSelected(false);
			jmenuitemMedium.setSelected(false);
			jmenuitemLarge.setSelected(true);
			jmenuitemLoud.setSelected(false);
			pack();
		}else if(e.getSource() == jmenuitemLoud){
			setTextSize(DISPLAY_SIZE.LOUD, this);
			jmenuitemSmall.setSelected(false);
			jmenuitemMedium.setSelected(false);
			jmenuitemLarge.setSelected(false);
			jmenuitemLoud.setSelected(true);
			pack();
		}	

		for(int i = 0; i < keys.length; i++)
			if(e.getSource() == keys[i].getButton()) {
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
				for(int i = 0; i < keys.length; i++)
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
	public static void main(String args[]) {
		new Calculator();
	}

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
