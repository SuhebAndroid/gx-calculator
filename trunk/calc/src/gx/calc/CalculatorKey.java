package gx.calc;

public class CalculatorKey {

	private char keyBinding;
	private CalculatorAction action;
	private boolean isDigit = false;
	private String toggleGroup;
	private javax.swing.AbstractButton button;
	private static final String[] numNames = { "Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine" };
	
	public CalculatorKey(CalculatorAction action, CalculatorActionHandler actionHandler, String displayValue) {
		this(action, actionHandler, displayValue, null, displayValue.charAt(0), null, null);
	}
	
	public CalculatorKey(CalculatorAction action, CalculatorActionHandler actionHandler, String displayValue, String descriptionValue) {
		this(action, actionHandler, displayValue, descriptionValue, displayValue.charAt(0), null, null);
	}
	
	public CalculatorKey(CalculatorAction action, CalculatorActionHandler actionHandler, String displayValue, String descriptionValue, char keyBinding) {
		this(action, actionHandler, displayValue, descriptionValue, keyBinding, null, null);
	}
	
	public CalculatorKey(CalculatorAction action, CalculatorActionHandler actionHandler, String displayValue, String descriptionValue, char keyBinding, String toggleGroup) {
		this(action, actionHandler, displayValue, descriptionValue, keyBinding, toggleGroup, null);
	}
	
	public CalculatorKey(CalculatorAction action, CalculatorActionHandler actionHandler, String displayValue, String descriptionValue, char keyBinding, java.awt.Color color) {
		this(action, actionHandler, displayValue, descriptionValue, keyBinding, null, color);
	}
	
	public CalculatorKey(CalculatorAction action, CalculatorActionHandler actionHandler, String displayValue, String descriptionValue, char keyBinding, String toggleGroup, java.awt.Color color) {
		this.keyBinding = keyBinding;
		this.action = action;
		this.toggleGroup = toggleGroup;

		try {
			int n = Integer.parseInt(displayValue);
			if(n >=0 && n <= 9)
				isDigit = true;
		}
		catch(NumberFormatException e) { }
		
		if(isDigit && descriptionValue == null)
			descriptionValue = getDescriptionValue(displayValue);

		button = toggleGroup != null ? new javax.swing.JToggleButton(displayValue) : new javax.swing.JButton(displayValue);
//		button.setForeground(isDigit ? java.awt.Color.blue : toggleGroup != null ? java.awt.Color.magenta : java.awt.Color.red);
		button.setForeground(color == null ? isDigit ? java.awt.Color.blue : toggleGroup != null ? java.awt.Color.magenta : java.awt.Color.red : color);
		button.addActionListener(actionHandler);
		button.addKeyListener(actionHandler);
		button.setToolTipText(descriptionValue + (keyBinding != ' ' ? " [" + keyBinding + "]" : ""));
		button.setFocusable(false);		
	}
	
	public javax.swing.AbstractButton getButton() {
		return button;
	}

	public void doAction() {
		if(action != null)
			action.ActionHandler();
	}
	
	private String getDescriptionValue(String displayValue) {
		return numNames[Integer.valueOf(displayValue)];
	}

	public char getKeyChar() {
		return keyBinding;
	}

	public boolean isToggleGroup(String group) {
		return toggleGroup == group;
	}

}
