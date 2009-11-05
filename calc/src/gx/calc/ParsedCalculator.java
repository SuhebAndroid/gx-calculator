// TODO : expression remains in label, need to decide when to clear it

package gx.calc;
 
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JRadioButtonMenuItem;

public class ParsedCalculator extends javax.swing.JFrame implements CalculatorActionHandler { 
	private static final long serialVersionUID = 1L;

	final int MAX_INPUT_LENGTH = 30;

	private javax.swing.JMenu jmenuFile, jmenuEdit, jmenuView, jmenuHelp;
	private javax.swing.JMenuItem jmenuitemPlugins, jmenuitemSaveExpression, jmenuitemExit, jmenuitemAbout, jmenuitemCopy, jmenuitemPaste;
	private javax.swing.JRadioButtonMenuItem jmenuitemSmall, jmenuitemMedium, jmenuitemLarge, jmenuitemLoud;
	private javax.swing.JCheckBoxMenuItem jmenuitemShowPlugins, jmenuitemShowExpressions;
	private javax.swing.JLabel jlbOutput;
	private javax.swing.JLabel jlblExpression;
	private javax.swing.JPanel jplMaster, jplControl;
//	private final javax.swing.JPanel jplCustomButtons;
	private javax.swing.JPanel jplPluginButtons;
	private javax.swing.JPanel jplExpressionButtons;

	private CalculatorKey keys[];
	private java.util.ArrayList<CalculatorKey> customKeys;
	private CalculatorActionHandler actionHandler = this; 

	static final class DISPLAY_SIZE {
		static final int SMALL 			= 10; 
		static final int MEDIUM			= 12; 
		static final int LARGE 			= 16; 
		static final int LOUD 			= 20; 
	}

	static final class KEYS { 
		static final int ZERO 					= 0; 
		static final int ONE 					= 1; 
		static final int TWO					= 2; 
		static final int THREE 					= 3; 
		static final int FOUR 					= 4; 
		static final int FIVE 					= 5; 
		static final int SIX 					= 6; 
		static final int SEVEN	 				= 7; 
		static final int EIGHT				 	= 8; 
		static final int NINE 					= 9; 
		static final int DECIMAL_POINT			= 10; 
		static final int EQUALS 				= 11; 
		static final int DIVIDE 				= 12; 
		static final int MULTIPLY				= 13; 
		static final int MINUS 					= 14; 
		static final int PLUS 					= 15; 
		static final int PERCENT 				= 16; 
		static final int BACKSPACE 				= 17; 
		static final int CLEAR_EXISTING 		= 18; 
		static final int CLEAR_ALL 				= 19; 
		static final int EXPRESSION_BRACE_OPEN 	= 20; 
		static final int EXPRESSION_BRACE_CLOSE = 21; 
		static final int EXPRESSION_X 			= 22; 
		static final int length					= 23; 
	}

	public ParsedCalculator() {
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
		jmenuitemSaveExpression = getMenuItem("Save Expression", java.awt.event.KeyEvent.VK_S, jmenuFile);
		jmenuitemExit = getMenuItem("Exit", java.awt.event.KeyEvent.VK_X, jmenuFile); 

		jmenuitemCopy = getMenuItem("Copy", java.awt.event.KeyEvent.VK_C, jmenuEdit);
		jmenuitemPaste = getMenuItem("Paste", java.awt.event.KeyEvent.VK_V, jmenuEdit);

		jmenuView = new javax.swing.JMenu("View");
		jmenuView.setMnemonic(java.awt.event.KeyEvent.VK_V);
		jmenuitemShowPlugins = (JCheckBoxMenuItem) getMenuItem("Show Plugin keys", java.awt.event.KeyEvent.VK_G, jmenuView, true);
		jmenuitemShowExpressions = (JCheckBoxMenuItem) getMenuItem("Show Expression keys", java.awt.event.KeyEvent.VK_R, jmenuView, true);

		jmenuView.addSeparator();
		jmenuitemSmall = (JRadioButtonMenuItem) getMenuItem("Small Text", java.awt.event.KeyEvent.VK_S, jmenuView, DISPLAY_SIZE.SMALL);
		jmenuitemMedium = (JRadioButtonMenuItem) getMenuItem("Medium Text", java.awt.event.KeyEvent.VK_M, jmenuView, DISPLAY_SIZE.MEDIUM);
		jmenuitemLarge = (JRadioButtonMenuItem) getMenuItem("Large Text", java.awt.event.KeyEvent.VK_L, jmenuView, DISPLAY_SIZE.LARGE);
		jmenuitemLoud = (JRadioButtonMenuItem) getMenuItem("LoudText", java.awt.event.KeyEvent.VK_O, jmenuView, DISPLAY_SIZE.LOUD);

		jmenuHelp = new javax.swing.JMenu("Help");
		jmenuHelp.setMnemonic(java.awt.event.KeyEvent.VK_H);

		jmenuitemAbout = new javax.swing.JMenuItem("About Calculator");
		jmenuitemAbout.addActionListener(this);
		jmenuHelp.add(jmenuitemAbout);

		javax.swing.JMenuBar menuBar = new javax.swing.JMenuBar();
		menuBar.add(jmenuFile);
		menuBar.add(jmenuEdit);
		menuBar.add(jmenuView);
		menuBar.add(jmenuHelp);
		setJMenuBar(menuBar);

		jplMaster = new javax.swing.JPanel();

		javax.swing.JPanel jplInOut= new javax.swing.JPanel(new java.awt.BorderLayout());
		jlblExpression = new javax.swing.JLabel(" ", javax.swing.JLabel.RIGHT);
		jplInOut.add(jlblExpression, java.awt.BorderLayout.NORTH);

		jlbOutput = new javax.swing.JLabel("0", javax.swing.JLabel.RIGHT);
		jlbOutput.setBackground(java.awt.Color.white);
		jlbOutput.setOpaque(true);

		jplInOut.add(jlbOutput, java.awt.BorderLayout.SOUTH);
		getContentPane().add(jplInOut, java.awt.BorderLayout.NORTH);

		keys = new CalculatorKey[KEYS.length];
		customKeys = new java.util.ArrayList<CalculatorKey>();

		for (int i = 0; i <= 9; i++) {
			final Integer j = new Integer(i);
			keys[i] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { addDigitToDisplay(j.intValue()); } }, actionHandler, j.toString());
		}

		keys[KEYS.DECIMAL_POINT] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { addDecimalPoint(); } }, actionHandler, ".", "Decimal point");
		keys[KEYS.EQUALS] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { processEquals(); } }, actionHandler, "=", "Equals");
		keys[KEYS.DIVIDE] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { processOperator("/"); } }, actionHandler, "/", "Dvide", '/', "operators");
		keys[KEYS.MULTIPLY] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { processOperator("*"); } }, actionHandler, "*", "Multiply", '*', "operators"); 
		keys[KEYS.MINUS] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { processOperator("-"); } }, actionHandler, "-", "Minus", '-', "operators");
		keys[KEYS.PLUS] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { processOperator("+"); } }, actionHandler, "+", "Plus", '+', "operators"); 
		keys[KEYS.PERCENT] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { processPercent(); } }, actionHandler, "%", "Percent");
		keys[KEYS.EXPRESSION_BRACE_CLOSE] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { processOperator(")"); } }, actionHandler, ")", "Close brace");
		keys[KEYS.EXPRESSION_BRACE_OPEN] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { processOperator("("); } }, actionHandler, "(", "Open brace");
		keys[KEYS.EXPRESSION_X] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { processOperator("x"); } }, actionHandler, "x", "Numeric place hoder");

		keys[KEYS.BACKSPACE] = new CalculatorKey(new CalculatorAction() {
			public void ActionHandler() {
				if(jlblExpression.getText().length() > 1)
					jlblExpression.setText(jlblExpression.getText().substring(0,jlblExpression.getText().length() - 1));
			}
		}, actionHandler, "<-", "Backspace", ' ');

		keys[KEYS.CLEAR_EXISTING] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { setDisplayString("0"); } }, actionHandler, "CE", "Clear existing", ' '); 
		keys[KEYS.CLEAR_ALL] = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { jlblExpression.setText(" "); setDisplayString("0"); } }, actionHandler, "C", "Clear all", ' ');

		javax.swing.JPanel jplCustomButtons = new javax.swing.JPanel();

		jplControl = new javax.swing.JPanel();
		jplControl.add(keys[KEYS.BACKSPACE].getButton()); 
		jplControl.add(keys[KEYS.CLEAR_EXISTING].getButton());
		jplControl.add(keys[KEYS.CLEAR_ALL].getButton());

		javax.swing.JPanel jplButtonsWrapper = new javax.swing.JPanel(); // for visual spacing
		javax.swing.JPanel jplButtons = new javax.swing.JPanel(new java.awt.BorderLayout());
		javax.swing.JPanel jplNumbers = new javax.swing.JPanel(new java.awt.GridLayout(3, 3));
		javax.swing.JPanel jplSouth = new javax.swing.JPanel(new java.awt.GridLayout(1, 4));
		javax.swing.JPanel jplNorth = new javax.swing.JPanel(new java.awt.GridLayout(1, 4));
		javax.swing.JPanel jplOperators = new javax.swing.JPanel(new java.awt.GridLayout(3, 1));

		jplButtonsWrapper.add(jplButtons);

		jplNumbers.add(keys[KEYS.SEVEN].getButton());
		jplNumbers.add(keys[KEYS.EIGHT].getButton());
		jplNumbers.add(keys[KEYS.NINE].getButton());
		jplNumbers.add(keys[KEYS.FOUR].getButton());
		jplNumbers.add(keys[KEYS.FIVE].getButton());
		jplNumbers.add(keys[KEYS.SIX].getButton());
		jplNumbers.add(keys[KEYS.ONE].getButton());
		jplNumbers.add(keys[KEYS.TWO].getButton());
		jplNumbers.add(keys[KEYS.THREE].getButton());
		jplButtons.add(jplNumbers, java.awt.BorderLayout.CENTER);

		jplNorth.add(keys[KEYS.EXPRESSION_BRACE_OPEN].getButton());
		jplNorth.add(keys[KEYS.EXPRESSION_BRACE_CLOSE].getButton());
		jplNorth.add(keys[KEYS.EXPRESSION_X].getButton());
		jplNorth.add(keys[KEYS.EQUALS].getButton());
		jplButtons.add(jplNorth, java.awt.BorderLayout.NORTH);

		jplOperators.add(keys[KEYS.MULTIPLY].getButton());
		jplOperators.add(keys[KEYS.MINUS].getButton());
		jplOperators.add(keys[KEYS.PLUS].getButton());
		jplButtons.add(jplOperators, java.awt.BorderLayout.EAST);

		jplSouth.add(keys[KEYS.PERCENT].getButton());
		jplSouth.add(keys[KEYS.ZERO].getButton());
		jplSouth.add(keys[KEYS.DECIMAL_POINT].getButton());
		jplSouth.add(keys[KEYS.DIVIDE].getButton());
		jplButtons.add(jplSouth, java.awt.BorderLayout.SOUTH);


		jplMaster.setLayout(new java.awt.BorderLayout());
		jplMaster.add(jplControl, java.awt.BorderLayout.WEST);
		javax.swing.JPanel btns = new javax.swing.JPanel();	
		btns.setLayout(new java.awt.BorderLayout(2,2));
		btns.add(jplButtonsWrapper, java.awt.BorderLayout.WEST);
		btns.add(jplCustomButtons, java.awt.BorderLayout.EAST);
		jplMaster.add(btns, java.awt.BorderLayout.SOUTH);

		// Add components to frame
		getContentPane().add(jplMaster, java.awt.BorderLayout.SOUTH);

		setTitle(Configuration.APPLICATION_NAME);

		if(Configuration.getWindowLocation() != null)
			setLocation(Configuration.getWindowLocation());
		else
			setLocationRelativeTo(null);

		jplPluginButtons = new javax.swing.JPanel();	
		jplExpressionButtons = new javax.swing.JPanel();
		jplPluginButtons.setVisible(Configuration.showPluginkeys());
		jplExpressionButtons.setVisible(Configuration.showExpressionkeys());
		loadExtensionKeys();
//		initPlugins(jplPluginButtons);
//		initExpressions(jplExpressionButtons);
		
		java.awt.GridBagLayout gbl = new java.awt.GridBagLayout();
		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.ipadx = 0;
		gbc.ipady = 0;
		jplCustomButtons.setLayout(gbl);
		jplCustomButtons.add(jplPluginButtons, gbc);
		jplCustomButtons.add(jplExpressionButtons, gbc);

		setResizable(false);
		setVisible(true);
		setTextSize(this);
		addWindowListener(this);
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
		/*javax.swing.JMenuItem mi = new javax.swing.JMenuItem(text);
		mi.setAccelerator(javax.swing.KeyStroke.getKeyStroke(keyEvent, java.awt.event.ActionEvent.CTRL_MASK));
		mi.addActionListener(this);
		if(parentMenu != null)
			parentMenu.add(mi);*/
		return getMenuItem(text, keyEvent, parentMenu, -1, false);
	}
	
	private javax.swing.JMenuItem getMenuItem(String text, int keyEvent, javax.swing.JMenu parentMenu, int size) {
		return getMenuItem(text, keyEvent, parentMenu, size, false);
	}
	
	private javax.swing.JMenuItem getMenuItem(String text, int keyEvent, javax.swing.JMenu parentMenu, boolean isCheckbox) {
		return getMenuItem(text, keyEvent, parentMenu, -1, isCheckbox);
	}

	private javax.swing.JMenuItem getMenuItem(String text, int keyEvent, javax.swing.JMenu parentMenu, int size, boolean isCheckbox) {
		javax.swing.JMenuItem mi = size > 0 ? new javax.swing.JRadioButtonMenuItem(text) : 
			isCheckbox ? new javax.swing.JCheckBoxMenuItem(text) : new javax.swing.JMenuItem(text);
		if(size > 0) {
			mi.setFont(new java.awt.Font(mi.getFont().getName(), mi.getFont().getStyle(), size));
			mi.setAccelerator(javax.swing.KeyStroke.getKeyStroke(keyEvent, java.awt.event.ActionEvent.CTRL_MASK));
			mi.setSelected(Configuration.getDisplaySize() == size);
		}		
		mi.addActionListener(this);
		if(parentMenu != null)
			parentMenu.add(mi);
		return mi;
	}
	
	public void loadExtensionKeys(){
		jplPluginButtons.removeAll();
		jplExpressionButtons.removeAll();
		initPlugins(jplPluginButtons);
		initExpressions(jplExpressionButtons);
		pack();
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
						customKeys.add(c);
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
		if(addedButtons > 0) 
			jmenuitemShowPlugins.setSelected(Configuration.showPluginkeys());
		else
			jmenuitemShowPlugins.setEnabled(false);
		/*
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
		 */
	}
	
	private void initExpressions(javax.swing.JPanel p){
		int addedButtons = 0;
		javax.swing.JPanel innerP = null;
		java.awt.GridBagLayout gbl = new java.awt.GridBagLayout();
		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.ipadx = 0;
		gbc.ipady = 0;
		p.setLayout(gbl);

		for(final CalculatorExpressionDescriptor d : Configuration.getExpressions()) {
			if(d.enabled) {
				CalculatorKey c = new CalculatorKey(new CalculatorAction() { public void ActionHandler() { jlblExpression.setText(d.expression); processEquals(); } } , 
						actionHandler, d.displayValue, d.descriptionValue, d.keyBinding, java.awt.Color.magenta);
				if(addedButtons % 5 == 0) {
					innerP = new javax.swing.JPanel(new java.awt.GridLayout(5, 1));
					p.add(innerP, gbc);
				}
				innerP.add(c.getButton());
				customKeys.add(c);
				addedButtons++;
			}
		}
		if(addedButtons > 0) 
			jmenuitemShowExpressions.setSelected(Configuration.showExpressionkeys());
		else
			jmenuitemShowExpressions.setEnabled(false);

	}

	private void doCalculation(CalculatorPlugin p) {
		double result = getNumberInDisplay();
		result = p.process(result);
		setDisplayString(String.valueOf(result));
	}

	private void setDisplayString(String s){

		if(s.endsWith(".0"))
			s = s.substring(0, s.length() - 2);
		if (s.length() < 1)
			s = "0";
		jlbOutput.setText(s);
	}

	private String getDisplayString () {
		return jlbOutput.getText();
	}

	private void addDigitToDisplay(int digit) {
		processOperator(digit + "");
	}

	private void addDecimalPoint() {
		// TODO check that the decimal is valid
		String ex = jlblExpression.getText();
		if(ex.length() > 1 && ex.substring(ex.length() - 1).matches("\\d")) 
			jlblExpression.setText(ex + ".");
	}

	private double getNumberInDisplay()	{
		String input = jlbOutput.getText();
		return Double.parseDouble(input);
	}

	private void processOperator(String op) {

		String ex = jlblExpression.getText();
		if(ex.length() > MAX_INPUT_LENGTH) return;

		// "(x-" beginning of expression after an operator after an open brace
		if(op.matches("[\\(\\-xX]") && ex.substring(ex.length() - 1).matches("[ \\(\\*\\/\\+\\-]")) 
			jlblExpression.setText(jlblExpression.getText() + op);

		// "*+/)" after a number after x after close brace
		if(op.matches("[\\*\\/\\+\\)\\-]") && ex.substring(ex.length() - 1).matches("[\\dxX\\)]")) 
			jlblExpression.setText(jlblExpression.getText() + op);

		// "0-9"				
		if(op.matches("[0-9]") && ex.substring(ex.length() - 1).matches("[ 0-9\\.\\-\\*\\+\\/\\(]")) 
			jlblExpression.setText(ex + op);
	}

	private void processPercent(){
		double result = 0;

		try	{
			result = getNumberInDisplay() / 100;
			displayResult(result);
		}
		catch(Exception ex)	{
			displayError("Invalid input for function!");
		}
	}

	private void processEquals(){
		for(int i = 0; i < keys.length; i++) 
			if(keys[i].isToggleGroup("operators")) 
				keys[i].getButton().setSelected(false);

		try  {
			String exp = jlblExpression.getText();
			if(exp.length() > 0)
				setDisplayString(String.valueOf(MathParser.processEquation(exp.replaceAll("[xX]", jlbOutput.getText()))));
		}
		catch (NumberFormatException e)	{
			displayError("Invalid expression!");
		}
	}

	private void displayResult(double result) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("###.########");
		setDisplayString(df.format(result));
	}

	private void displayError(String errorMessage) {
		setDisplayString(errorMessage);
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
		}else if(e.getSource() == jmenuitemSaveExpression){
			CalculatorExpressionDescriptor p = new CalculatorExpressionDescriptor();
			p.expression = jlblExpression.getText().trim();
			new PluginsDialog(this, p);
		}else if(e.getSource() == jmenuitemExit){
			System.exit(0);
		}else if(e.getSource() == jmenuitemCopy || e.getActionCommand() == "Copy"){
			writeToClipboard(getDisplayString());
		}else if(e.getSource() == jmenuitemPaste || e.getActionCommand() == "Paste"){
			String s = copyFromClipboard();
			if(s != null)
				setDisplayString(s);
		}else if(e.getSource() == jmenuitemShowPlugins){
			Configuration.setShowPluginkeys(!Configuration.showPluginkeys());
			jmenuitemShowPlugins.setSelected(Configuration.showPluginkeys());
			jplPluginButtons.setVisible(Configuration.showPluginkeys());
			pack();
		}else if(e.getSource() == jmenuitemShowExpressions){
			Configuration.setShowExpressionkeys(!Configuration.showExpressionkeys());
			jmenuitemShowExpressions.setSelected(Configuration.showExpressionkeys());
			jplExpressionButtons.setVisible(Configuration.showExpressionkeys());
			pack();
		}else if(e.getSource() == jmenuitemSmall){
			setTextSize(DISPLAY_SIZE.SMALL, this);
			clearMenuSizeSelection();
			jmenuitemSmall.setSelected(true);
			pack();
/*
			// for some strange reason packing doesent seem to work as expected, hiding then showing panel helps 
			jplCustomButtons.setVisible(!jplCustomButtons.isVisible()); 
			jplCustomButtons.setVisible(!jplCustomButtons.isVisible());
			pack();
 */
		}else if(e.getSource() == jmenuitemMedium){
			setTextSize(DISPLAY_SIZE.MEDIUM, this);
			clearMenuSizeSelection();
			jmenuitemMedium.setSelected(true);
			pack();
/*
			// for some strange reason packing doesent seem to work as expected, hiding then showing panel helps 
			jplCustomButtons.setVisible(!jplCustomButtons.isVisible()); 
			jplCustomButtons.setVisible(!jplCustomButtons.isVisible());
 */
			pack();
		}else if(e.getSource() == jmenuitemLarge){
			setTextSize(DISPLAY_SIZE.LARGE, this);
			clearMenuSizeSelection();
			jmenuitemLarge.setSelected(true);
			pack();
/*
			// for some strange reason packing doesent seem to work as expected, hiding then showing panel helps 
			jplCustomButtons.setVisible(!jplCustomButtons.isVisible()); 
			jplCustomButtons.setVisible(!jplCustomButtons.isVisible());
			pack();
 */
		}else if(e.getSource() == jmenuitemLoud){
			setTextSize(DISPLAY_SIZE.LOUD, this);
			clearMenuSizeSelection();
			jmenuitemLoud.setSelected(true);
			pack();
/*
			// for some strange reason packing doesent seem to work as expected, hiding then showing panel helps 
			jplCustomButtons.setVisible(!jplCustomButtons.isVisible()); 
			jplCustomButtons.setVisible(!jplCustomButtons.isVisible());
			pack();
 */
		}	

		for(int i = 0; i < keys.length; i++)
			if(e.getSource() == keys[i].getButton()) {
				keys[i].doAction();
				break;
			}

		for(int i = 0; i < customKeys.size(); i++)
			if(e.getSource() == customKeys.get(i).getButton()) {
				customKeys.get(i).doAction();
				break;
			}
	}

	private void clearMenuSizeSelection() {
		jmenuitemSmall.setSelected(false);
		jmenuitemMedium.setSelected(false);
		jmenuitemLarge.setSelected(false);
		jmenuitemLoud.setSelected(false);
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
					for(int i = 0; i < customKeys.size(); i++)
						if(c == customKeys.get(i).getKeyChar()) {
							customKeys.get(i).doAction();
							actionTaken = true;
							break;
						}
			}
		break;
		}

		return actionTaken;
	}

	public static void main(String args[]) {
		new ParsedCalculator();
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

//interface CalculatorActionHandler extends java.awt.event.ActionListener, java.awt.event.KeyListener , java.awt.event.WindowListener { }
//interface CalculatorAction { void ActionHandler(); }
