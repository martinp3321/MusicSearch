package musiclibrary;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Search {


	/**
	 * Read/Parse/Add Queries Method
	 * @param lib
	 * @param input
	 * @param output
	 */

	public static void search(SongLibrary lib, String input, String output) {
		//1. read the input file -> build Map<searchType, JSONArray> map
		//2. if (map.contains("searchByTag")) {do search by tag}
		
		//LinkedHashMap<String, JSONObject> map = new LinkedHashMap<>();
		
		JSONObject map = new JSONObject();
		
		
		
		//1. read the input file --> { "searchByArtist": ["Queen", "Busta Rhymes"], "searchByTitle": ["Wishlist", "Ode To Billie Joe  (Live @ Fillmore West)"], "searchByTag": ["50s rockabilly"]
		//map <searchType->search result>
	
		try (BufferedReader reader = Files.newBufferedReader(Paths.get(input), Charset.forName("UTF-8"))) {
			JSONParser parser = new JSONParser();
			//2. parse the input into necessary data format, like array, map...
			JSONObject contents = (JSONObject) parser.parse(reader);
			
			

			//3. HashMap<String, ArrayList<Song>> titleResult = lib.searchByTitle(parsed_data);
			if (contents.containsKey("searchByArtist")) {
				JSONArray artistArray = (JSONArray) contents.get("searchByArtist");
				String[] artists = new String[artistArray.size()];

				for (int i = 0; i < artistArray.size(); i++) {
					artists[i] = (String)artistArray.get(i);
				}
				map.put("searchByArtist", lib.searchByArtist(artists));
				//System.out.println(lib.searchByArtist(artists).toJSONString());
			}
			
			
//
			if (contents.containsKey("searchByTitle")) {

				JSONArray titleArray = (JSONArray) contents.get("searchByTitle");
				String[] titles = new String[titleArray.size()];

				for (int i = 0; i < titleArray.size(); i++) {
					titles[i] = (String)titleArray.get(i);			
				}				
				map.put("searchByTitle", lib.searchByTitle(titles));
				//System.out.println(lib.searchByArtist(artists).toJSONString());

			}


			if (contents.containsKey("searchByTag")) {
				JSONArray tagArray = (JSONArray) contents.get("searchByTag");
				String[] tags = new String[tagArray.size()];

				for (int i = 0; i < tagArray.size(); i++) {
					tags[i] = (String)tagArray.get(i);			
				}				
				map.put("searchByTag", lib.searchByTag(tags));
			}
			
	//		outputSearch(map, output);
			outputSearch(map, output);

		} catch (IOException e) {
			e.printStackTrace();

		} catch (ParseException e) {
			e.printStackTrace();

		}

	}
	/**
	 * Write Query Results
	 * @param map
	 * @param output
	 */

		public static void outputSearch(JSONObject map,String output) {
		Path path = Paths.get(output);
		System.out.println(map.size());

		try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(path, Charset.forName("UTF-8")))) {	
		
			writer.println(map.toJSONString());
		
		}


		catch (IOException e) {
			e.printStackTrace();

		} 
	}
}







