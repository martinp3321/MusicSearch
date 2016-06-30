package servlets;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class LoginDirectServlet extends BaseServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String user = getUsername(request);
		

		if (user != null) {
			prepareResponse(response);
			PrintWriter writer = response.getWriter();
			writer.println(headerLoad());
			writer.println(navBar(user));
			writer.println("<br>");
			writer.println("<br>");
			writer.println("<br>");
			writer.println("<br>");
			writer.println("<h3 class='THIN'>Sign In</h3>");
			writer.println("<br>");
			writer.println("<br>") ;
			writer.println("<p>Hello " + user + "! You are currently signed in. Would you like to logout?</p>");
			writer.println("<div class='container'>"); 
			writer.println("<br>") ;
			writer.println("<br>") ;
			writer.println("<a  href=\'/login?logout\' <i class='waves-effect waves-light btn-large blue center font-size: 60px'><i class='material-icons right'>power_settings_new</i>Logout</i> </a>");
			writer.println("</div>");			
		}
		else {
			response.sendRedirect("/login");
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		doGet(request, response);
	}
}
