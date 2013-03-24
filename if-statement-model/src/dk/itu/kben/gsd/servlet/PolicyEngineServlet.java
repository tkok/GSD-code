package dk.itu.kben.gsd.servlet;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class PolicyEngineServlet extends HttpServlet {
	
	Thread thread = null;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		thread = new Thread(new Runnable() {
			Date getNext() {
				Calendar calendar = new GregorianCalendar();
				
				calendar.add(Calendar.SECOND, 3);
				
				return calendar.getTime();
			}
			
			@Override
			public void run() {
				while (true) {
				
					Date nextTime = getNext();
					
					while (new Date().getTime() < nextTime.getTime()) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
		
					System.out.println("3 seconds passed.");
				}
			}
		});
		
		System.out.println("PolicyEngineServlet is starting...");
		thread.start();
		System.out.println("PolicyEngineServlet was started.");
	}
}
