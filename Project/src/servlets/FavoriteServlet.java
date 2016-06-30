package servlets;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import musiclibrary.MultiThreadLibrary;
import musiclibrary.Song;


public class FavoriteServlet extends BaseServlet {
	

	private String responseHtmlHead = "<html> <head class='center-align'> "
			+ "<script type='text/javascript' src='https://code.jquery.com/jquery-2.1.1.min.js'></script>"
			+ "<script src='https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.6/js/materialize.min.js'></script>"
			+ "<link rel='stylesheet\' href='https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.6/css/materialize.min.css\'>"
			+ "<link href='https://fonts.googleapis.com/icon?family=Material+Icons' rel='stylesheet'>"
			+ "</head><body class='center-align'>" 
			+ "</body></html>";
	
	
	private String tableFormat = "<h3 class='THIN'>Favorites</h3>"+
			"<table class='centered' border=\"2px\" width=\"100%\">" +
			"<table class='highlight centered'>" +
			"<tr bgcolor='#2196f3'><td><font color='#fff'>Artist</font></td><td><font color='#fff'>Song Title</font></td></tr>";
	
	public HashMap<String,String> favList = null;
	private HashMap<String,Song> favMap = new HashMap<String,Song>();

	/**
	 * GET /returns header
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = getUsername(request);		
		String trackid = request.getParameter("trackId");
		String responseHtmlContent = tableFormat;
		PrintWriter writer = prepareResponse(response);
		
	
		MultiThreadLibrary libby = (MultiThreadLibrary) request.getServletContext().getAttribute("MusicLibrary");
					
		
		try {
			favList = dbhelper.getFavTable(username);
			for (String trackids: favList.keySet()){
				System.out.println(favList.keySet().size());								
				favMap = libby.getIdMap();			
				Song newFavSong = favMap.get(trackids);			
				String favArtist = newFavSong.getArtist();
				String favTitle = newFavSong.getTitle();	
				
				responseHtmlContent += "<tr><td>" + favArtist  + "</td><td>" + favTitle + "</td></tr>";
					}
			responseHtmlContent += "</table>";			
			writer.println(responseHtmlHead + this.navBar(username) + responseHtmlContent);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}				
		} 

	/**
	 * POST /search song library
	 * Returns a web page containing similar songs
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = getUsername(request);		
		String trackid = request.getParameter("trackId");
		PrintWriter writer = prepareResponse(response);
		if(username!=null){
		dbhelper.addFavorite(username, trackid);
		writer.println("<p>Success! Song added to your favorites.</p>");		
		doGet(request,response);
		}
		else{				
		response.sendRedirect("/favoritedirect");
		}
}

}










