package dk.itu.kben.gsd.servlet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.jasper.tagplugins.jstl.core.Url;

import dk.itu.kben.gsd.BuildingDAO;
import dk.itu.scas.gsd.net.Connection;

public class PolicyEngineServlet extends HttpServlet {
	
	Thread thread = null;
	static boolean shouldRun = true;
	static boolean wasStopped = false;
	private final static String QUERY_STRING = "http://gsd.itu.dk/api/user/measurement/?uuid=";
	private final static String QUERY_ARGUMENTS = "&limit=1&order_by=-timestamp&format=json";
	static int HEARTBEAT_TIMER_SECONDS = 3; 

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		thread = new Thread(new Runnable() {
			Date getNext() {
				Calendar calendar = new GregorianCalendar();
				
				calendar.add(Calendar.SECOND, HEARTBEAT_TIMER_SECONDS);
				
				return calendar.getTime();
			}
			
			@Override
			public void run() {
				while (shouldRun) {
				
					Date nextTime = getNext();
					
					while (new Date().getTime() < nextTime.getTime()) {
						try {
							if (!shouldRun) break;
							Thread.sleep(1);
							List<String> sensors = dk.itu.scas.gsd.net.Connection.getSensorIds();
							// select all policies WHERE policy.activationFromTime >= currentTime AND policy.activationToTime <= currentTime AND policy.active = TRUE ORDER BY timestamp DESC LIMIT 1
							
							// for each policy
							
								// for each IfStatement
							
									// for each expression
										
										// sensors.add(expression.getSensorId());
							
									// end for
							
								// end for
							
							// end for
							
							for (String sensorId : sensors) {

								// fetch the value of sensorId and put it into BuildingDAO's hashtable
								Object object = Connection.querySimulator(QUERY_STRING+"sensorId"+QUERY_ARGUMENTS);
								
								
							}
							
							// for each policy
							
								// for each Statement
							
									// execute
						
								// end for
						
							// end for
						
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
		
					System.out.println("3 seconds passed.");
				}
				
				System.out.println("PolicyEngineServlet is stopping...");
				System.out.println("PolicyEngineServlet was stopped.");
				
				wasStopped = true;
			}
		});
		
		System.out.println("PolicyEngineServlet is starting...");
		thread.start();
		System.out.println("PolicyEngineServlet was started.");
	}
	
	@Override
	public void destroy() {
		shouldRun = false;
		
		while (!wasStopped) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		thread = null;
		System.out.println("PolicyEngineServlet internal thread was stopped and available to garbage collector.");
		
		
		super.destroy();
	}	
	
}
