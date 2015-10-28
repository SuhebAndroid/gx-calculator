# How to make a plugin #

Creating a plugin is easy enough, create class that implements the interface gx.calc.CalculatorPlugin.


# Details #

Create class that implements the interface gx.calc.CalculatorPlugin. This interface requires you to implement the method "process", this method receives a double value and returns a double value.

Here is an example of the source code for the "Half the value" plugin.

```

package gx.calc.plugin;

public class HalfValue implements gx.calc.CalculatorPlugin {

	@Override
	public double process(double currentValue) {
		return currentValue / 2;
	}

}
```

Compile the class and package it in a jar file that is all there is to it.