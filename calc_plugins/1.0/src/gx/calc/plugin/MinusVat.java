package gx.calc.plugin;

public class MinusVat implements gx.calc.CalculatorPlugin {

	@Override
	public double process(double currentValue) {
		return currentValue - (currentValue * 0.15);
	}

}
