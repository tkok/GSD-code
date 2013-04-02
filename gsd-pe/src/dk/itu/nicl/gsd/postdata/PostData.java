package dk.itu.nicl.gsd.postdata;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet to handle data from GUI and save to database
 */
@WebServlet("/PostData")
public class PostData extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
	}

}
