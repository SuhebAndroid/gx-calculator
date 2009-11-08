package gx.calc;

public class Configuration
{
	public static final String APPLICATION_NAME = "GX Calculator";
	public static final String VERSION = "1.2";
	private static boolean loaded; 

	private static java.awt.Point WINDOW_PREVIOUS_LOCATION;
	//	private static boolean WINDOW_EXTENDED; 
	private static boolean SHOW_PLUGINKEYS; 
	private static boolean SHOW_EXPRESSIONKEYS; 
	private static int DISPLAY_SIZE = Calculator.DISPLAY_SIZE.MEDIUM;
	private static java.util.ArrayList<CalculatorPluginDescriptor> PLUGINS = new java.util.ArrayList<CalculatorPluginDescriptor>();
	private static java.util.ArrayList<CalculatorExpressionDescriptor> EXPRESSIONS = new java.util.ArrayList<CalculatorExpressionDescriptor>();
	
	public static final String CONFIGURATION_FILE = System.getProperty("user.home") + "/.gx.Cacl/calculator.config";

	public static void load() {
    	java.util.Properties config = new java.util.Properties();
    	try {
    		java.io.File cf = new java.io.File(CONFIGURATION_FILE);

    		if(cf.exists())
    			config.load(new java.io.FileInputStream(cf));
    		
		} catch (Exception e) { e.printStackTrace(); }
		String wpl = config.getProperty("WINDOW_PREVIOUS_LOCATION", null);
		if(wpl != null) {
			try {
				String[] p = wpl.split("x");
				if(p.length == 2) {
					WINDOW_PREVIOUS_LOCATION = new java.awt.Point(Integer.parseInt(p[0]) , Integer.parseInt(p[1]));
				}
			}
			catch(Exception e) { e.printStackTrace(); }
		}
//		WINDOW_EXTENDED = Boolean.parseBoolean(config.getProperty("WINDOW_EXTENDED", "false").trim());     
		SHOW_PLUGINKEYS = Boolean.parseBoolean(config.getProperty("SHOW_PLUGINKEYS", "false").trim());     
		SHOW_EXPRESSIONKEYS = Boolean.parseBoolean(config.getProperty("SHOW_EXPRESSIONKEYS", "false").trim());     
		DISPLAY_SIZE = Integer.parseInt(config.getProperty("DISPLAY_SIZE", String.valueOf(Calculator.DISPLAY_SIZE.MEDIUM)).trim());     

		String pl = config.getProperty("PLUGINS", null);
		if(pl != null && pl.length() > 0) {
			try {
				String[] p = pl.split("\n");
				for(int i = 0; i < p.length; i++) 
					PLUGINS.add(new CalculatorPluginDescriptor(p[i]));
			} catch(Exception e) { e.printStackTrace(); }
		}
		
		String el = config.getProperty("EXPRESSIONS", null);
		if(el != null && el.length() > 0) {
			try {
				String[] e = el.split("\n");
				for(int i = 0; i < e.length; i++) 
					EXPRESSIONS.add(new CalculatorExpressionDescriptor(e[i]));
			} catch(Exception e) { e.printStackTrace(); }
		}
		loaded = true;
    }
	
	public static void store() {
		if(!loaded)
			return;
    	try {
    		java.util.Properties config = new java.util.Properties();
    		
    		if(WINDOW_PREVIOUS_LOCATION != null)
    			config.setProperty("WINDOW_PREVIOUS_LOCATION", String.valueOf(WINDOW_PREVIOUS_LOCATION.x) + 'x' + String.valueOf(WINDOW_PREVIOUS_LOCATION.y));
//    		config.setProperty("WINDOW_EXTENDED", String.valueOf(WINDOW_EXTENDED));
    		config.setProperty("SHOW_PLUGINKEYS", String.valueOf(SHOW_PLUGINKEYS));
    		config.setProperty("SHOW_EXPRESSIONKEYS", String.valueOf(SHOW_EXPRESSIONKEYS));
    		config.setProperty("DISPLAY_SIZE", String.valueOf(DISPLAY_SIZE));
    		
    		StringBuffer sb = new StringBuffer();
    		for(CalculatorPluginDescriptor p : PLUGINS)
    			sb.append(p.toString() + "\n");
    		
    		config.setProperty("PLUGINS", sb.toString());    		
    		
    		sb = new StringBuffer();
    		for(CalculatorExpressionDescriptor e : EXPRESSIONS)
				sb.append(e.toString() + "\n");

    		config.setProperty("EXPRESSIONS", sb.toString());    		

    		java.io.File cf = new java.io.File(CONFIGURATION_FILE);

    		if(!cf.exists()) {
    			cf.getParentFile().mkdirs();
    			cf.createNewFile();
    		}
    		config.store(new java.io.FileOutputStream(cf), "");
		} catch (Exception e) { e.printStackTrace(); }

    }

	public static void setWindowLocation(java.awt.Point position) {
		WINDOW_PREVIOUS_LOCATION = position;
	}

	public static java.awt.Point getWindowLocation() {
		return WINDOW_PREVIOUS_LOCATION;
	}
/*
	public static void setWindowExtended(boolean extended) {
		WINDOW_EXTENDED = extended;
	}

	public static boolean isWindowExtended() {
		return WINDOW_EXTENDED;
	}
 */

	public static boolean showPluginkeys() {
		return SHOW_PLUGINKEYS;
	}

	public static void setShowPluginkeys(boolean show_pluginkeys) {
		SHOW_PLUGINKEYS = show_pluginkeys;
	}

	public static boolean showExpressionkeys() {
		return SHOW_EXPRESSIONKEYS;
	}

	public static void setShowExpressionkeys(boolean show_expressionkeys) {
		SHOW_EXPRESSIONKEYS = show_expressionkeys;
	}

	public static java.util.ArrayList<CalculatorExpressionDescriptor> getExpressions() {
		return EXPRESSIONS;
	}
	
	public static java.util.ArrayList<CalculatorPluginDescriptor> getPlugins() {
		return PLUGINS;
	}

	public static void setDisplaySize(int displaySize) {
		DISPLAY_SIZE = displaySize;
	}

	public static int getDisplaySize() {
		return DISPLAY_SIZE;
	}

}
