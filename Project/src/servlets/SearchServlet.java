package servlets;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import musiclibrary.MultiThreadLibrary;
import musiclibrary.Song;


public class SearchServlet extends BaseServlet {



	private HashMap<String,String> favCheck = new HashMap<String,String>();

	private String responseHtmlHead = "<html> <head class='center-align'> "
			+ "<script type='text/javascript' src='https://code.jquery.com/jquery-2.1.1.min.js'></script>"
			+ "<script src='https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.6/js/materialize.min.js'></script>"
			+ "<link rel='stylesheet\' href='https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.6/css/materialize.min.css\'>"
			+ "<link href='https://fonts.googleapis.com/icon?family=Material+Icons' rel='stylesheet'>"
			+ "</head><body class='center-align'>" 
			+ "</body></html>";


	private String tableFormat = "<table class='centered' border=\"2px\" width=\"100%\">" +
			"<table class='highlight centered'>" +
			"<tr bgcolor='#2196f3'><td><font color='#fff'>Artist</font></td><td><font color='#fff'>Song Title</font></td><td><font color='#fff'>Favorite</font></td></tr>";


	private String tableFormatAlpha = "<table class='centered' border=\"2px\" width=\"100%\">" +
			"<table class='highlight centered'>" +
			"<tr bgcolor='#2196f3'><td><font color='#fff'>Artist</font></td></tr>";
	/**
	 * GET /returns header
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String user = getUsername(request);

		String responseHtml = responseHtmlHead + this.navBar(user) + "<main>"+this.searchBar()+"</main>";

		PrintWriter writer = prepareResponse(response);
		writer.println(responseHtml);

	}

	/**
	 * POST /search song library
	 * Returns a web page containing similar songs
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		MultiThreadLibrary lib = (MultiThreadLibrary) request.getServletContext().getAttribute("MusicLibrary");
		PrintWriter writer = prepareResponse(response);
		String user = getUsername(request);
		
		JSONArray result = null;
		JSONArray results = null;


		try {
			favCheck = dbhelper.getFavTable(user);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}


		//get the parameter from he search box
		String type = request.getParameter("type");


		//get the JSON representation song
		if(type.equals("Artist")){
			String[] artists = request.getParameterValues("query");
			result = lib.searchByArtist(artists);
		}


		if(type.equals("Title")){
			String[] titles = request.getParameterValues("query");
			result = lib.searchByTitle(titles);

		}
		if(type.equals("Tag")){
			String[] tags= request.getParameterValues("query");
			for (int i = 0; i < tags.length; i++) {
				tags[i] = tags[i].toLowerCase();
			}
			result = lib.searchByTag(tags);
		}

		if(type.equals("Alphabetical")){
			String responseHtmlContentAlpha=tableFormatAlpha;
			results = lib.searchByArtistAlpha();
			for(int i=0; i<results.size(); i++){

				responseHtmlContentAlpha += "<tr><td>" + (String)results.get(i) + "</td></tr>" ;


			}
			responseHtmlContentAlpha += "</table>";
			writer.println(responseHtmlHead + this.navBar(user) + "<main>"+this.searchBar() + responseHtmlContentAlpha);
		}

		String responseHtmlContent;
	

		if(result != null) {
			responseHtmlContent = tableFormat;

					
			for (int i=0; i<result.size(); i++) {

				JSONArray similarSongs = ((JSONArray)((JSONObject)result.get(i)).get("similars"));

				for (int j=0; j<similarSongs.size(); j++) {

					responseHtmlContent += "<tr><td>" + ((JSONObject)similarSongs.get(j)).get("artist") + "</td><td>" 
							+(String) ((JSONObject)similarSongs.get(j)).get("title") + "</td>"; 	

					if(favoriteCheck(favCheck,j,similarSongs,user)){
						responseHtmlContent +=
										"<td>"+
										"<br>"+
										"<form action='/favorite' method='post'>"+						
										"<button class='btn waves-effect waves-light red accent-3' name='trackId' value="
										+(String) ((JSONObject)similarSongs.get(j)).get("trackId")+" type='button'>Saved"+
										"<i class='material-icons right'>done</i>"+
										"</button>"+
										"</form>"+
										"</td></tr>";
					}
					else{
						responseHtmlContent +=
										"<td>"+
										"<br>"+
										"<form action='/favorite' method='post'>"+						
										"<button class='btn waves-effect waves-light blue' name='trackId' value="
										+(String) ((JSONObject)similarSongs.get(j)).get("trackId")+" type='submit'>Add"+
										"<i class='material-icons right'>add</i>"+
										"</button>"+
										"</form>"+
										"</td></tr>";
					}
				}
			}

			responseHtmlContent += "</table>";
		} else {
			responseHtmlContent = "Search: " + Arrays.toString(request.getParameterValues("query")) + " not found!";
		}


		if(!type.equals("Alphabetical")){
			writer.println(responseHtmlHead + this.navBar(user) + "<main>"+this.searchBar() + responseHtmlContent);
		}

	}

	private Boolean favoriteCheck(HashMap<String, String> favCheck, int j, JSONArray similarSongs, String user){

		if(favCheck.containsKey(((String)((JSONObject)similarSongs.get(j)).get("trackId"))) && favCheck.containsValue(user)){
			return true;
		}
		else{
			return false;
		}				
	}











}

