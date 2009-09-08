package gx.calc.plugin;

public class PlusVat implements gx.calc.CalculatorPlugin {

	@Override
	public double process(double currentValue) {
		return currentValue + (currentValue * 0.15);
	}

}
