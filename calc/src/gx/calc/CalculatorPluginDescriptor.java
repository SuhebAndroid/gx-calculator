package gx.calc;

public class CalculatorPluginDescriptor extends CalculatorKeyDescriptor {
	public String location;
	public String className;
	public boolean loaded;
	public boolean enabled;

	public CalculatorPluginDescriptor() { super(CalculatorKeyDescriptor.TYPE.INTERFACE);}
	
	public CalculatorPluginDescriptor(String s) throws Exception {
		super(CalculatorKeyDescriptor.TYPE.INTERFACE);
		String[] a = s.split("\t");
		if(a.length != 6)
			throw new Exception("Invalid argument");

		this.location = a[0];
		this.className = a[1];
		this.displayValue = a[2];
		this.descriptionValue = a[3];
		this.keyBinding = a[4].charAt(0);
		this.enabled = Boolean.parseBoolean(a[5]);
	}

	public CalculatorPluginDescriptor(String location, String classPath, String displayValue, String descriptionValue, char keyBinding, boolean enabled) {
		super(CalculatorKeyDescriptor.TYPE.INTERFACE);
		this.location = location;
		this.className = classPath;
		this.displayValue = displayValue;
		this.descriptionValue = descriptionValue;
		this.keyBinding = keyBinding;
		this.enabled = enabled;
	}

	public String toString() {
		return location + "\t" + className + "\t" + displayValue + "\t" + descriptionValue + "\t" + keyBinding + "\t" + String.valueOf(enabled);
	}
	
	public Object[] toArray() {
		Object o[] = { this.location, this.className, this.displayValue, this.descriptionValue, this.keyBinding, String.valueOf(this.enabled) };
		return o;
	}
}

