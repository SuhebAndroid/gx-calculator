package gx.calc.plugin;

public class HalfValue implements gx.calc.CalculatorPlugin {

	@Override
	public double process(double currentValue) {
		return currentValue / 2;
	}

}
