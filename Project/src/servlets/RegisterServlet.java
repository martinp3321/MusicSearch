package servlets;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.DBstate;


@SuppressWarnings("serial")
public class RegisterServlet extends BaseServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		prepareResponse(response);
		
		String user = getUsername(request);

		PrintWriter writer = response.getWriter();
		
		String error = request.getParameter("error");
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
		
		

		printForm(writer,user);
		navBar(user);
		
		
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		prepareResponse(response);
		String user = getUsername(request);
		String newfullname = request.getParameter("fullname");
		String newuser = request.getParameter("user");
		String newpass = request.getParameter("pass");
		DBstate state = dbhelper.registerUser(newfullname,newuser, newpass);

		if(state == DBstate.NOERROR) {
			response.sendRedirect(response.encodeRedirectURL("/login?newuser=true"));
		}
		else {
			String url = "/register?error=" + state.ordinal();
			url = response.encodeRedirectURL(url);
			response.sendRedirect(url);
		}
	}

	private void printForm(PrintWriter writer,String user) {
		assert writer != null;
		
		String responseHtml = headerLoad() + navBar(user);										
		writer.println(responseHtml);
		writer.println("<br>");
		writer.println("<br>");
		writer.println("<br>");
		writer.println("<br>");
		writer.println("<main>");
		writer.println("<div class='container'>");
		writer.println("<h3 class='THIN'>Create Account</h3>");
		writer.println("<form action=\"/register\" method=\"post\">");
		writer.println("<input placeholder='Fullname' type=\"text\" name=\"fullname\" size=\"30\">");		
		writer.println("<input placeholder='Username' type=\"text\" name=\"user\" size=\"30\">");
		writer.println("<input placeholder='Password' type=\"password\" name=\"pass\" size=\"30\">");
		writer.println("<br>");
		writer.println("<br>");
		writer.println("<br>");
		writer.println("<button class='waves-effect waves-light btn-large blue center-align' type='submit'><i class='material-icons right'>send</i>Submit</button>");
		writer.println("</form>");
		writer.println("</div>");
		writer.println("</main>");
	
	}
	
	

}
