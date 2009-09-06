package gx.calc;

public class CalculatorPluginDescriptor {
	public String location;
	public String className;
	public String displayValue;
	public String descriptionValue;
	public char keyBinding;
	public boolean enabled;
	public boolean loaded;

	public CalculatorPluginDescriptor() { }
	
	public CalculatorPluginDescriptor(String s) throws Exception {
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
	
}

