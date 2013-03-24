package dk.itu.kben.gsd.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class PolicyEngineServlet extends HttpServlet {

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		System.out.println("INIT WAS CALLED!");
	}
	
}
