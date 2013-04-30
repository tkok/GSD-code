package dk.itu.policyengine.servlet;

import javax.servlet.ServletConfig;

public class Configuration {
	
	private static String server;
	private static String building;
	private static String setvalue;
	private static String format;
	private static int activationInterval;
	private static int timeout;
	
	public static void setConfiguration(ServletConfig servletConfig) {
		server = servletConfig.getInitParameter("server");
		System.out.println("Server: " + server);
		
		building = servletConfig.getInitParameter("building");
		System.out.println("Building: " + building);
		
		format = servletConfig.getInitParameter("format");
		System.out.println("Format: " + format);
		
		setvalue = servletConfig.getInitParameter("setvalue");
		System.out.println("SetValue: " + setvalue);
		
		activationInterval = new Integer(servletConfig.getInitParameter("activationInterval")).intValue();
		System.out.println("ActivationInterval: " + activationInterval);
		
		timeout = new Integer(servletConfig.getInitParameter("timeout")).intValue();
		System.out.println("Timeout: " + timeout);
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
