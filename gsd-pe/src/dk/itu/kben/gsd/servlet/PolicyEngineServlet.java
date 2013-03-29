package dk.itu.kben.gsd.servlet;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

import dk.itu.scas.gsd.net.Connection;
import dk.itu.scas.gsd.net.ServiceProperties;

public class PolicyEngineServlet extends HttpServlet {
	
	Thread thread = null;
	static boolean shouldRun = true;
	static boolean wasStopped = false;
	static int HEARTBEAT_TIMER_SECONDS = 10; 
	private final static String QUERY_ARGUMENTS = "&limit=1&order_by=-timestamp&format=json";
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		System.out.println(config.getInitParameter("server"));
		Connection.setServer(config.getInitParameter("server"));
		Connection.setBuilding(config.getInitParameter("building"));
		Connection.setFormat(config.getInitParameter("format"));
		Connection.setSetvalue(config.getInitParameter("setvalue"));
		final List<String> sensors = dk.itu.scas.gsd.net.Connection.getSensorIds();
		
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
								//System.out.println(sensorId);
								
								
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
	@Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException{
        doPost(request,response);
    }
	 public void doPost(HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException{
	        response.setContentType("text/html");
	        PrintWriter out = response.getWriter();
	        String userPath = request.getServletPath();
	        HttpSession session = request.getSession();
	        if(userPath.equals("/ListSensors")){
	        	List<String> sensors = Connection.getSensorIds();
	        	session.setAttribute("sensors", (List<String>) sensors);
	        }
	        if(userPath.equals("/ListProperties")){
	        	String id = request.getParameter("element");
	        	out.println("<br>id = "+id);
	        	//session.setAttribute("sensorProperties", ServiceProperties.allSensorsWithProperties(id));
	        	List<String> sens = ServiceProperties.allSensorsWithProperties(id);
	        	for(String s : sens)
	        		out.println("<br>"+s);	        
	        	}
	 }	
}
