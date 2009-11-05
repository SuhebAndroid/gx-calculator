package gx.calc;

public class CalculatorExpressionDescriptor extends CalculatorKeyDescriptor {
	public String expression;

	public CalculatorExpressionDescriptor() { super(CalculatorKeyDescriptor.TYPE.EXPRESSION); }
	
	public CalculatorExpressionDescriptor(String s) throws Exception {
		super(CalculatorKeyDescriptor.TYPE.EXPRESSION);
		String[] a = s.split("\t");
		if(a.length != 5)
			throw new Exception("Invalid argument");

		this.expression = a[0];
		this.displayValue = a[1];
		this.descriptionValue = a[2];
		this.keyBinding = a[3].charAt(0);
		this.enabled = Boolean.parseBoolean(a[4]);
	}

	public CalculatorExpressionDescriptor(String expression, String displayValue, String descriptionValue, char keyBinding, boolean enabled) {
		super(CalculatorKeyDescriptor.TYPE.EXPRESSION);
		this.expression = expression;
		this.displayValue = displayValue;
		this.descriptionValue = descriptionValue;
		this.keyBinding = keyBinding;
		this.enabled = enabled;
	}

	public String toString() {
		/*
		StringBuilder sb = new StringBuilder();
		if(this.expression != null)
			sb.append(this.expression);
		sb.append("\t");
		if(this.displayValue != null)
			sb.append(this.displayValue);
		sb.append("\t");
		if(this.descriptionValue != null)
			sb.append(this.descriptionValue);
		sb.append("\t");
		sb.append(this.keyBinding);
		sb.append("\t");
		sb.append(String.valueOf(this.enabled));
		return sb.toString();
		 */
		//return this.expression + "\t" + this.displayValue + "\t" + this.descriptionValue + "\t" + this.keyBinding + "\t" + String.valueOf(this.enabled);
		String s = this.expression + "\t" + this.displayValue + "\t" + this.descriptionValue + "\t" + this.keyBinding + "\t" + String.valueOf(this.enabled);
		return s.replaceAll("null", "");
	}
	
	public Object[] toArray() {
		Object o[] = { this.expression, this.displayValue, this.descriptionValue, this.keyBinding, String.valueOf(this.enabled) };
		return o;
	}
	
}

