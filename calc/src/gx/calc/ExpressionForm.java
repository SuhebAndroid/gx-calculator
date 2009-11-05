package gx.calc;

public class ExpressionForm extends FormDialog {
	private static final long serialVersionUID = 1L;
	javax.swing.JComponent[] fields;
	private CalculatorExpressionDescriptor calculatorExpression ;
	private PluginsDialog caller;
	 
	public ExpressionForm(PluginsDialog parent, CalculatorKeyDescriptor d) {
		super(parent, "Add new expression formula", true);
		this.caller = parent;
		this.calculatorExpression = (CalculatorExpressionDescriptor)d;
		javax.swing.JButton save = new javax.swing.JButton("Save");

		save.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if(save())
					dispose();
		}});
		
		addButton(save);

		String v = calculatorExpression == null ? "Expression\tButton label\tDescription\tKey character\tfalse" : calculatorExpression.toString();
		String[] values = v.split("\t");
		String[] labels = "Expression\tLabel\tDescription\tKey".split("\t");
		fields = new javax.swing.JComponent[labels.length];

		javax.swing.JPanel form = BuildForm(labels, values, fields);
		add(form);
		getContentPane().add(form, java.awt.BorderLayout.CENTER);
		pack();
		setVisible(true);
	}

	private boolean save() {
		for(CalculatorExpressionDescriptor ce : Configuration.getExpressions())
			if(ce.expression.equals(((javax.swing.JTextField)fields[0]).getText()) && ce.displayValue.equals((((javax.swing.JTextField)fields[1]).getText()))) {
				javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(),
						"At least the expression and label combination must be unique", "Fields must be unique", javax.swing.JOptionPane.OK_OPTION);
				return false;
			}
		if(fields != null) {
			if(calculatorExpression == null) {
				calculatorExpression = new CalculatorExpressionDescriptor();
				Configuration.getExpressions().add(calculatorExpression);
			}
			if(!Configuration.getExpressions().contains(calculatorExpression))
				Configuration.getExpressions().add(calculatorExpression);

			calculatorExpression.expression = ((javax.swing.JTextField)fields[0]).getText();
			calculatorExpression.displayValue = ((javax.swing.JTextField)fields[1]).getText();
			calculatorExpression.descriptionValue = ((javax.swing.JTextField)fields[2]).getText();
			if(((javax.swing.JTextField)fields[3]).getText().length() > 0)
				calculatorExpression.keyBinding = ((javax.swing.JTextField)fields[3]).getText().charAt(0);
		}
		caller.loadTables();
		Configuration.store();
		return true;
	}
}
