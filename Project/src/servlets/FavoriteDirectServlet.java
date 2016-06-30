package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class FavoriteDirectServlet extends BaseServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String user = getUsername(request);
		
		if (user == null) {
			prepareResponse(response);
			PrintWriter writer = response.getWriter();
			writer.println(headerLoad());
			writer.println(navBar(user));
			writer.println("<br>");
			writer.println("<br>");
			writer.println("<br>");
			writer.println("<br>");
			writer.println("<h3 class='THIN'>Favorites</h3>");
			writer.println("<br>");
			writer.println("<br>") ;
			writer.println("<p>Hello, you are currently not signed in. Please create an account or sign in to add/view favorites.</p>");
			writer.println("<br>") ;
			writer.println("<br>") ;
			writer.println("<div class='container'>"); 
			writer.println("<br>");
			writer.println("<br>");
			writer.println("<a  href='/register' <i class='waves-effect waves-light btn-large blue center font-size: 60px'><i class='material-icons right'>send</i>Create Account</i> </a>");
			writer.println("<br>") ;
			writer.println("<br>") ;
			writer.println("<a  href='/login' <i class='waves-effect waves-light btn-large blue center font-size: 60px'><i class='material-icons right'>send</i>Sign In</i> </a>");
			writer.println("</div>");			
		}
		else {
			response.sendRedirect("/favorite");
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		doGet(request, response);
	}
}