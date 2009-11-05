package gx.calc;

public class MathParser {
	private static final boolean debugOutput = true;

	/* Using a private constructor to prevent instantiation Using class as a simple static utility class */
	private MathParser() { }	

	private static String evaluate(String expression) {

		double t  = 0;
		String result = expression;
		// this is not bodmas compatible so we need one regex for each operator 
/*
		String exp = "(-?[\\d\\.]+)([\\/*\\-\\+])(-?[\\d\\.]+)";
		java.util.regex.Matcher matcher = java.util.regex.Pattern.compile(exp).matcher(result);

		while(matcher.find()) {
			double l = Double.parseDouble(matcher.group(1));
			char o = matcher.group(2).charAt(0);
			double r = Double.parseDouble(matcher.group(3));

			if(debugOutput) System.out.println("equating equasion: l{" + l + "} o[" + o + "] r{" + r + "}");
			switch (o) {
				case '/': t = l / r; break;
				case '*': t = l * r; break;
				case '+': t = l + r; break;
				case '-': t = l - r; break;
				default: throw new IllegalArgumentException("Unknown operator.");
			}
			result = expression.substring(0, matcher.start()) + t + expression.substring(matcher.end());
			matcher = java.util.regex.Pattern.compile(exp).matcher(result);
		}
 */
		BODMASMatchedExpression bodmas = new BODMASMatchedExpression(result);
		while(bodmas.hasMatch()) {
			double l = bodmas.getLeft();
			char o = bodmas.getOperator();
			double r = bodmas.getRight();

			if(debugOutput) System.out.println("equating equasion: l{" + l + "} o[" + o + "] r{" + r + "}");
			switch (o) {
				case '/': t = l / r; break;
				case '*': t = l * r; break;
				case '+': t = l + r; break;
				case '-': t = l - r; break;
				default: throw new IllegalArgumentException("Unknown operator.");
			}
			result = result.substring(0, bodmas.getStart()) + t + result.substring(bodmas.getEnd());
			bodmas = new BODMASMatchedExpression(result);
		}
		if(debugOutput) System.out.println("Returning a result of: " + result);
		return result;
	}
	
	private static double processBraces(String equation) {
		int posOpen = equation.lastIndexOf('(');
		int posClose = equation.indexOf(')', posOpen);
		String result = "";
		if(posOpen > 0 && posClose > posOpen) {
			String subExp = equation.substring(posOpen + 1, posClose);
			if(debugOutput) System.out.println("evaluating sub expression: " + subExp);
			result = evaluate(subExp);
			return processBraces(equation.substring(0, posOpen) + result + equation.substring(posClose + 1));
		}
		if(debugOutput) System.out.println("processBraces returning : " + equation);
		return Double.parseDouble(evaluate(equation));			
	}

	public static double processEquation(String equation) { 
		equation = equation.replaceAll("\\s", ""); // remove spaces
		if(debugOutput) System.out.println("trimmed equasion: " + equation);
		if(equation.length()== 0) return 0;
		return processBraces(equation);
	}
/*
	public static void main(String[] args) {
		String usage = "Usage: java MathParser equation\nWhere equation is a series of integers separated by valid operators (+,-,/,*)";

		try {
			System.out.println("The result of your equation " + args[0]+ " is: " + MathParser.processEquation(args[0]));
		} catch (Exception e) {
			System.out.println(e.getMessage() + "\n" + usage);
			e.printStackTrace();
		}
	}
*/ 
}

class BODMASMatchedExpression {
	private double l;// = Double.parseDouble(matcher.group(1));
	private char o;// = matcher.group(2).charAt(0);
	private double r;// = Double.parseDouble(matcher.group(3));
	private int s;// matcher.start()) + t + expression.substring(matcher.end());
	private int e;// matcher.start()) + t + expression.substring(matcher.end());
	private boolean f;

	public BODMASMatchedExpression(String text) {
		this.f = getMatchFor(text, '/');
		if(!this.f)
			this.f = getMatchFor(text, '*');
		if(!this.f)
			this.f = getMatchFor(text, '+');
		if(!this.f)
			this.f = getMatchFor(text, '-');		
	}

	private boolean getMatchFor(String text, char operator) {
		String regex = "(-?[\\d\\.]+)(\\x)(-?[\\d\\.]+)";
		java.util.regex.Matcher matcher = java.util.regex.Pattern.compile(regex.replace('x', operator)).matcher(text);
		if(matcher.find()) {
			this.l = Double.parseDouble(matcher.group(1));
			this.o = matcher.group(2).charAt(0);
			this.r = Double.parseDouble(matcher.group(3));
			this.s = matcher.start();
			this.e = matcher.end();
			return true;
		}
		return false;
	}
	
	public double getLeft() {
		return l;
	}

	public char getOperator() {
		return o;
	}

	public double getRight() {
		return r;
	}

	public int getStart() {
		return s;
	}

	public int getEnd() {
		return e;
	}

	public void setF(boolean f) {
		this.f = f;
	}

	public boolean hasMatch() {
		return f;
	}

}