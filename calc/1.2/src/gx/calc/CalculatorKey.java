package gx.calc;

public class CalculatorKey {

	private char keyBinding;
	private CalculatorAction action;
	private boolean isDigit = false;
	private String toggleGroup;
	private javax.swing.AbstractButton button;
	private static final String[] numNames = { "Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine" };
	
	public CalculatorKey(String displayValue, CalculatorAction action, CalculatorActionHandler actionHandler) {
		this(displayValue, null, displayValue.charAt(0), action, actionHandler, null);
	}
	
	public CalculatorKey(String displayValue, String descriptionValue, CalculatorAction action, CalculatorActionHandler actionHandler) {
		this(displayValue, descriptionValue, displayValue.charAt(0), action, actionHandler, null);
	}
	
	public CalculatorKey(String displayValue, String descriptionValue, char keyBinding, CalculatorAction action, CalculatorActionHandler actionHandler) {
		this(displayValue, descriptionValue, keyBinding, action, actionHandler, null);
	}
	
	public CalculatorKey(String displayValue, String descriptionValue, char keyBinding, CalculatorAction action, CalculatorActionHandler actionHandler, String toggleGroup) {
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
		button.setForeground(isDigit ? java.awt.Color.blue : toggleGroup != null ? java.awt.Color.magenta : java.awt.Color.red);
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
