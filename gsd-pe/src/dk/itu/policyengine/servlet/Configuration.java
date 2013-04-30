package dk.itu.policyengine.servlet;

import javax.servlet.ServletConfig;

import org.apache.log4j.Logger;

public class Configuration {
	private static String server;
	private static String building;
	private static String setvalue;
	private static String format;
	private static int activationInterval;
	private static int timeout;
	
	public static void setConfiguration(ServletConfig servletConfig) {
		Logger logger = Logger.getLogger(Configuration.class);
		
		server = servletConfig.getInitParameter("server");
		logger.debug("Server: " + server);
		
		building = servletConfig.getInitParameter("building");
		logger.debug("Building: " + building);
		
		format = servletConfig.getInitParameter("format");
		logger.debug("Format: " + format);
		
		setvalue = servletConfig.getInitParameter("setvalue");
		logger.debug("SetValue: " + setvalue);
		
		activationInterval = new Integer(servletConfig.getInitParameter("activationInterval")).intValue();
		logger.debug("ActivationInterval: " + activationInterval);
		
		timeout = new Integer(servletConfig.getInitParameter("timeout")).intValue();
		logger.debug("Timeout: " + timeout);
	}

	public static String getServer() {
		return server;
	}

	public static String getBuilding() {
		return building;
	}

	public static String getSetvalue() {
		return setvalue;
	}

	public static String getFormat() {
		return format;
	}
	
	public static int getActivationInterval() {
		return activationInterval;
	}
	public static int getTimeout(){
		return timeout;
	}
}
