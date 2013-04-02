package dk.itu.nicl.gsd.postdata;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.DriverManager;


/**
 * Servlet to handle data from GUI and save to database
 */
@WebServlet("/PostData")
public class PostData extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// TODO: config file
	private static final String serverUrl = "jdbc:mysql://mysql2.gigahost.dk:3306/";
	private static final String dbName = "webaholic_gsd";
	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String userName = "webaholic";
	private static final String password = "Gh2kZuCwlpU5ZfpHQN4i";
	private static Connection connection = null;
	//

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String title = "Reading Request Parameters (Needs to be saved to DTB)";
		out.println("<BODY>\n" + "<H1 ALIGN=CENTER>" + title + "</H1>\n"
				+ "<UL>\n" + "  <LI>param1: " + request.getParameter("param1")
				+ "\n" + "  <LI>param2: " + request.getParameter("param2")
				+ "\n" + "  <LI>param3: " + request.getParameter("param3")
				+ "\n" + "</UL>\n" + "</BODY></HTML>");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);

		response.setContentType("text/html");  
	    PrintWriter pw = response.getWriter();  
 
	    try{  
	      String Param1 = request.getParameter("param1");  
	      String Param2 = request.getParameter("param2");  
	      String Param3 = request.getParameter("param3");  
	        
	      Class.forName(driver);  
	      connection = DriverManager.getConnection(serverUrl + dbName, userName, password);

	      java.sql.PreparedStatement pst = connection.prepareStatement("INSERT into Whatever values(?,?,?)");
	      
	      pst.setString(1,Param1);  
	      pst.setString(2,Param2);        
	      pst.setString(3,Param3);  
	  
	      int i = pst.executeUpdate();  
	      if(i!=0){  
	        pw.println("<br>Record has been inserted!");  
	          
	  
	      }  
	      else{  
	        pw.println("Failed to insert the data!");  
	       }  
	    }  
	    catch (Exception e){  
	      pw.println(e);  
	    }  
	  }
		
		
	}


