package gx.calc;

public class Configuration
{
	public static final String APPLICATION_NAME = "GX Calculator";
	public static final String VERSION = "1.2";

	private static java.awt.Point WINDOW_PREVIOUS_LOCATION;
	private static boolean WINDOW_EXTENDED; 
	private static int DISPLAY_SIZE = Calculator.DISPLAY_SIZE.MEDIUM;
	private static java.util.ArrayList<CalculatorPluginDescriptor> PLUGINS = new java.util.ArrayList<CalculatorPluginDescriptor>();
	
	public static final String CONFIGURATION_FILE = System.getProperty("user.home") + "/.gx.Cacl/calculator.config";

	public static void load() {
    	java.util.Properties config = new java.util.Properties();
    	try {
    		java.io.File cf = new java.io.File(Configuration.CONFIGURATION_FILE);

    		if(cf.exists())
    			config.load(new java.io.FileInputStream(cf));
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
		String wpl = config.getProperty("WINDOW_PREVIOUS_LOCATION", null);
		if(wpl != null) {
			try {
				String[] p = wpl.split("x");
				if(p.length == 2) {
					WINDOW_PREVIOUS_LOCATION = new java.awt.Point(Integer.parseInt(p[0]) , Integer.parseInt(p[1]));
				}
			}
			catch(Exception e) { }
		}
		WINDOW_EXTENDED = Boolean.parseBoolean(config.getProperty("WINDOW_EXTENDED", "false").trim());     
		DISPLAY_SIZE = Integer.parseInt(config.getProperty("DISPLAY_SIZE", String.valueOf(Calculator.DISPLAY_SIZE.MEDIUM)).trim());     

		String pl = config.getProperty("PLUGINS", null);
		if(pl != null) {
			try {
				String[] p = pl.split("\n");
				for(int i = 0; i < p.length; i++) 
					PLUGINS.add(new CalculatorPluginDescriptor(p[i]));
			} catch(Exception e) {}
		}

    }
	
	public static void store() {
    	try {
    		java.util.Properties config = new java.util.Properties();
    		
    		if(WINDOW_PREVIOUS_LOCATION != null)
    			config.setProperty("WINDOW_PREVIOUS_LOCATION", String.valueOf(WINDOW_PREVIOUS_LOCATION.x) + 'x' + String.valueOf(WINDOW_PREVIOUS_LOCATION.y));
    		config.setProperty("WINDOW_EXTENDED", String.valueOf(WINDOW_EXTENDED));
    		config.setProperty("DISPLAY_SIZE", String.valueOf(DISPLAY_SIZE));
    		StringBuffer sb = new StringBuffer();
    		for(CalculatorPluginDescriptor p : PLUGINS)
				sb.append(p.toString() + "\n");

    		config.setProperty("PLUGINS", sb.toString());    		
    		java.io.File cf = new java.io.File(Configuration.CONFIGURATION_FILE);

    		if(!cf.exists()) {
    			cf.getParentFile().mkdirs();
    			cf.createNewFile();
    		}
    		config.store(new java.io.FileOutputStream(cf), "");
		} catch (Exception e) {
			e.printStackTrace();
		}

    }

	public static void setWindowLocation(java.awt.Point position) {
		WINDOW_PREVIOUS_LOCATION = position;
	}

	public static java.awt.Point getWindowLocation() {
		return WINDOW_PREVIOUS_LOCATION;
	}

	public static void setWindowExtended(boolean extended) {
		WINDOW_EXTENDED = extended;
	}

	public static boolean isWindowExtended() {
		return WINDOW_EXTENDED;
	}

	public static java.util.ArrayList<CalculatorPluginDescriptor> getPlugins() {
		return PLUGINS;
	}

	public static void setDisplaySize(int displaySize) {
		Configuration.DISPLAY_SIZE = displaySize;
	}

	public static int getDisplaySize() {
		return DISPLAY_SIZE;
	}

}
