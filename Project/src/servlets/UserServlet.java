package servlets;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.DBstate;


@SuppressWarnings("serial")
public class UserServlet extends BaseServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		prepareResponse(response);

		PrintWriter writer = response.getWriter();
		String error = request.getParameter("error");
		String users = getUsername(request);
		int code = 0;
		
		if (error != null) {
			try {
				code = Integer.parseInt(error);
			}
			catch (Exception ex) {
				code = -1;
			}

			String errorMessage = getStateMessage(code);
			writer.println("<p>" + errorMessage + "</p>");
		}

		if (request.getParameter("newuser") != null) {
			writer.println("<p> Account created. You can now sign in below.</p>");
		}

		if (request.getParameter("logout") != null) {
			clearCookies(request, response);
			writer.println("<p>Logout completed.</p>");
		}		
		printForm(writer,users);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String user = request.getParameter("user");
		String pass = request.getParameter("pass");

		DBstate state = dbhelper.authenticateUser(user, pass);

		try {
			if (state == DBstate.NOERROR) {
				response.addCookie(new Cookie("login", "true"));
				response.addCookie(new Cookie("name", user));
				response.sendRedirect(response.encodeRedirectURL("/search"));
			}
			else {
				response.addCookie(new Cookie("login", "false"));
				response.addCookie(new Cookie("name", ""));
				response.sendRedirect(response.encodeRedirectURL("/login?error=" + state.ordinal()));
			}
		}
		catch (Exception ex) {

		}
	}

	private void printForm(PrintWriter writer, String users) {
		assert writer != null;
		
		String responseHtml = headerLoad()+navBar(users);					
		writer.println(responseHtml);
		writer.println("<br>");
		writer.println("<br>");
		writer.println("<br>");
		writer.println("<br>");
		writer.println("<main>");
		writer.println("<div class='container'>");
		writer.println("<h3 class='THIN'>Sign In</h3>");
		writer.println("<form action=\"/login\" method=\"post\">");
		writer.println("<input placeholder='Username' type=\"text\" name=\"user\" size=\"30\">");
		writer.println("<input placeholder='Password' type=\"password\" name=\"pass\" size=\"30\">");	
		writer.println("<br>");
		writer.println("<br>");
		writer.println("<br>");	
		writer.println("<button class='waves-effect waves-light btn-large blue center font-size: 60px' type='submit'><i class='material-icons right'>send</i>LOGIN</button>");
		writer.println("</form>");
		writer.println("</div>");		
		writer.println("<a  href='/register' <i class='waves-effect waves-light btn-large blue center font-size: 60px'><i class='material-icons right'>send</i>Create Account</i> </a>");		
		writer.println("</main>");
	
	}
}
