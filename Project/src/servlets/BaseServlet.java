package servlets;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import database.DBHelper;
import database.DBstate;




@SuppressWarnings("serial")
public class BaseServlet extends HttpServlet {

	protected static final DBHelper dbhelper = DBHelper.getInstance();	

	/**
	 * Search Box
	 * @return
	 */
	protected String searchBar() {

		return  "<br>" +
				"<br>" +
				"<br>" +
				"<br>" +				
				"<h2 class='THIN'>Search</h2>"+
				"<form action='search' method='post'>" +
				"<br>" +
				"<style>"+
				"@media only screen and (min-width: 601px)"+
				".container {"+
				" width: 40%;"+
				"}</style>"+				
				"<div class='container'>" + 								   
				"<label>Search By</label>" +
				"<select class='icons' name='type'>" + 								
				"<option value='Artist'white-text>Artist</option>" + 
				"<option value='Title'>Title</option>" +
				"<option value='Tag'>Tag</option>" +
				"<option value='Alphabetical'>All Artists(Alphabetical)</option>" + 
//				"<option value='Count'>Count</option>" +
//				"<option value='All'>All</option>" +
				"</select>" +
				"</div><br>" +
				"<br>" +
				"<br>" +
				"<br>" +
				"<br>" +
				"<div class='container'>" + 
				"<input type='text' class='form-control' name='query'>" +
				"<label>Please Enter Input Here</label>" +
				"</div>"+
				"<br>" +
				"<br>" +
				"<br>" +				
				"<button type='submit' class='waves-effect waves-light btn-large blue'>" +
				"<i class='material-icons right'>search</i>" +
				"<span></span> Submit" +
				"</button>"+
				"</form>" +
				"<script>$(document).ready(function() { $('select').material_select(); });</script>";
	}



	/**
	 * Top Menu
	 * @param user
	 * @return
	 */
	protected String navBar(String user){

					if(user==null){
			
					return 
					"<div>"+
					"<nav class='blue'>"+
					"<div class='nav-wrapper'>"+
					"<style> nav i, nav [class^='mdi-'], nav [class*='mdi-'], nav i.material-icons {"+
					"  display: block;"+
					" font-size: 4rem;"+
					"  height: 56px;"+
					"   line-height: 56px;"+
					"}"+
					"</style>"+
					" <a href='/search' class='brand-logo center'><i class='large material-icons font-size: 60px'>library_music</i></a>"+
					"<ul class='right hide-on-med-and-down'>"+
					"  <li><a href='/search'>Search</a></li>"+
					"  <li><a method='post' href='/favoritedirect'>Favorites</a></li>"+
					"  <li><a href='/register'>Sign up</a></li>"+
					"  <li><a href='/sign'>Sign in</a></li>"+	     	     
					"  <li>"+
					" </li>"+
					" </ul>"+
					"</div>"+
					"</nav>"+
					"</div>";
					}
					else{

					return
					"<nav class='blue'>"+
					"<div class='nav-wrapper'>"+
					"<style> nav i, nav [class^='mdi-'], nav [class*='mdi-'], nav i.material-icons {"+
					"  display: block;"+
					" font-size: 4rem;"+
					"  height: 56px;"+
					"   line-height: 56px;"+
					"}"+ 
					"</style>"+
					" <a href='/search' class='brand-logo center'><i class='large material-icons font-size: 60px'>library_music</i></a>"+
					"<ul class='right hide-on-med-and-down'>"+
					"  <li><a href='/search'>Search</a></li>"+
					"  <li><a method='post' href='/favoritedirect'>Favorites</a></li>"+
					"  <li><a href='/registerdirect'>Sign up</a></li>"+
					"  <li><a href='/logindirect'>Sign in</a></li>"+
					"<li><a>" +"Hello,  "+ user + "</a></li>"+				
					"<li><a href=\"/login?logout\">Logout</a></li>"+		
					"  <li>"+					
					" </li>"+					
					" </ul>"+
					"</div>"+
					"</nav>";
					}
	}


	/**
	 * Login Form
	 * @return
	 */
	protected String signIn(){
		
				return	
				"<main>"+
				"<div class='container'>"+
				"<h3 class='THIN'>Sign In</h3>"+
				"<form action=\"/login\" method=\"post\">"+
				"<input placeholder='Username' type=\"text\" name=\"user\" size=\"30\">"+
				"<input placeholder='Password' type=\"password\" name=\"pass\" size=\"30\">"	+
				"<button class='waves-effect waves-light btn-large blue center font-size: 60px' type='submit'>LOGIN</button>"+
				"</form>"+
				"</div>"	+
				"<a  href='/register' <i class='waves-effect waves-light btn-large blue center font-size: 60px'>Create Account</i> </a>"+
				"</main>";
	}




	/**
	 * Load frameworks
	 * @return
	 */
	protected String headerLoad() {
		
				return
				"<html> <head class='center-align'>"+
				"<script type='text/javascript' src='https://code.jquery.com/jquery-2.1.1.min.js'></script>"+		
				"<script src='https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.6/js/materialize.min.js'></script>"+
				"<link rel='stylesheet\' href='https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.6/css/materialize.min.css\'>"+
				"<link href='https://fonts.googleapis.com/icon?family=Material+Icons' rel='stylesheet'>"+
				"</head><body class='center-align'>"+
				"</body></html>";
	}


	/**
	 * Get Writer
	 * @param response
	 * @return
	 * @throws IOException
	 */
	protected PrintWriter prepareResponse(HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		return response.getWriter();
	}


	/**
	 * Store cookies
	 * @param request
	 * @return
	 */
	protected Map<String, String> getCookieMap(HttpServletRequest request) {
		HashMap<String, String> map = new HashMap<String, String>();

		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				map.put(cookie.getName(), cookie.getValue());
			}
		}

		return map;
	}
		
	
	
	/**
	 * Logout 
	 * @param request
	 * @param response
	 */
	protected void clearCookies(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();

		if(cookies == null) {
			return;
		}

		for(Cookie cookie : cookies) {
			cookie.setValue("");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
	}
		
	
	/**
	 * Logout
	 * @param cookieName
	 * @param response
	 */
	protected void clearCookie(String cookieName, HttpServletResponse response) {
		Cookie cookie = new Cookie(cookieName, null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}


	/**
	 * State of DB
	 * @param errorName
	 * @return
	 */
	protected String getStateMessage(String errorName) {
		DBstate state = null;

		try {
			state = DBstate.valueOf(errorName);
		}
		catch (Exception ex) {
			state = DBstate.ERROR;
		}

		return state.toString();
	}
	/**
	 * State of DB
	 * @param code
	 * @return
	 */
	protected String getStateMessage(int code) {
		DBstate state = null;

		try {
			state = DBstate.values()[code];
		}
		catch (Exception ex) {

			state = DBstate.ERROR;
		}

		return state.toString();
	}
	/**
	 * Current User Request
	 * @param request
	 * @return
	 */
	protected String getUsername(HttpServletRequest request) {
		Map<String, String> cookies = getCookieMap(request);

		String login = cookies.get("login");
		String user  = cookies.get("name");

		if (login != null && login.equals("true") && user != null) {

			return user;
		}
		return null;
	}

}




