package musiclibrary;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Maintains a music library of several songs.
 * @author srollins
 *
 */
public class SongLibrary {
	/*  
	 * 
	 * TreeMaps
	 * 
	 */
	protected TreeMap<String, TreeSet<Song>> titleMap;
	protected TreeMap<String, TreeSet<Song>> artistMap;
	protected TreeMap<String, TreeSet<Song>> tagMap;
	protected HashMap<String, Song> idMap;

	/**
	 * Constructor
	 * @param string 
	 * @param string 
	 */
	public SongLibrary() {
		this.titleMap = new TreeMap<>();
		this.artistMap = new TreeMap<>();
		this.tagMap = new TreeMap<>();
		this.idMap = new HashMap<>();
		
	}

	/**
	 * Add a song to the library.
	 * 
	 * appropriate data structures.
	 * @param song
	 */

	public void addSong(Song song) {

		///add titles
		String title = song.getTitle();
		if(!this.titleMap.containsKey(title)) {
			this.titleMap.put(title, new TreeSet<>(new ComparatorByTitle()));
		}				
		this.titleMap.get(title).add(song);

		//add artists
		String artist = song.getArtist();
		if(!this.artistMap.containsKey(artist)) {
			this.artistMap.put(artist, new TreeSet<>(new ComparatorByArtist()));
		}				
		this.artistMap.get(artist).add(song);
		
		

		//add tags
		for(int i=0; i<song.getTags().size(); i++){
			String tag = song.getTags().get(i);
			if(!this.tagMap.containsKey(tag)) {
				this.tagMap.put(tag, new TreeSet<>(new ComparatorByTrackId()));					
			}	
			this.tagMap.get(tag).add(song);			
		}
		//System.out.println(tagMap);



		// add ids
		String trackId = song.getTrackId();
		this.idMap.put(trackId,song);




	}

	/**
	 * Return the song associated with a unique track ID.
	 * @param trackId
	 * @return
	 */

	public TreeSet<Song> getTitle(String title) {
		if(!this.titleMap.containsKey(title)) {
			return null;
		}

		return this.titleMap.get(title);

	}

	/**
	 * Get tag
	 * @param tag
	 * @return
	 */
	public TreeSet<Song> getTag(String tag) {

		if(!this.tagMap.containsKey(tag)) {
			return null;			
		}		
		return this.tagMap.get(tag);
	}


	/**
	 * Return a sorted set of all songs by a given artist.
	 * @param artist
	 * @return
	 */
	public TreeSet<Song> getSongsByArtist(String artist) {

		if(!this.artistMap.containsKey(artist)) {
			return null;			
		}
		return this.artistMap.get(artist);
	}
	
	
	
	
	
	/**
	 * Get IDmap for favorites/trackids
	 * @return
	 */
	public HashMap<String,Song> getIdMap(){
		return idMap;		
	}
	
	/**
	 * search all artists by alpha
	 * @return
	 */
	public JSONArray searchByArtistAlpha() {

		JSONArray searchResults = new JSONArray();

		for (String artist : artistMap.keySet()) {			
			searchResults.add(artist);												
		}
		searchResults.sort(new ComparatorByAlpha());
		return searchResults;
	}

	/**
	 * artist search
	 * @param artists
	 * @return
	 */
	public JSONArray searchByArtist(String[] artists){



		JSONObject result = new JSONObject();
		JSONArray searchResults = new JSONArray();

		result.put("searchByArtist", searchResults);


		for (String artist : artists) {
			JSONObject search = new JSONObject();
			search.put("artist", artist);
			JSONArray searchResult = new JSONArray();

			if (artistMap.containsKey(artist)) {
				for (Song song : artistMap.get(artist)) {
					for (String similar : song.getSimilars()) {
						//					System.out.println(similar);
						if (idMap.get(similar) != null) {
							Song similarSong = idMap.get(similar);
							JSONObject songObject = new JSONObject();
							songObject.put("artist",  similarSong.getArtist());
							songObject.put("trackId", similarSong.getTrackId());
							songObject.put("title", similarSong.getTitle());
							//			songObject.put("tags", result.getTags());
							if(!searchResult.contains(songObject)){
								searchResult.add(songObject);
							}
						}	
					}
				}
			}

			searchResults.add(search);
			searchResult.sort(new ComparatorBySearch());
			search.put("similars", searchResult);						
		}

		return searchResults;
	}

	/**
	 * tag search
	 * @param tags
	 * @return
	 */
	public JSONArray searchByTag(String[] tags) {

		JSONObject result = new JSONObject();
		JSONArray searchResults = new JSONArray();
		result.put("searchByTag", searchResults);
		for (String tag : tags) {
			JSONObject search = new JSONObject();
			search.put("tag", tag);
			JSONArray searchResult = new JSONArray();

			if (tagMap.containsKey(tag)) {
				for (Song song : tagMap.get(tag)) {

					JSONObject songObject = new JSONObject();
					songObject.put("artist",  song.getArtist());
					songObject.put("trackId", song.getTrackId());
					songObject.put("title", song.getTitle());
					//			songObject.put("tags", result.getTags());
					if(!searchResult.contains(songObject)){
						searchResult.add(songObject);
					}
				}
			}

			searchResults.add(search);
			searchResult.sort(new ComparatorBySearch());
			search.put("similars", searchResult);
		}
		return searchResults;
	}


	/**
	 * title search
	 * @param titles
	 * @return
	 */
	public JSONArray  searchByTitle(String[] titles){






		JSONObject result = new JSONObject();
		JSONArray searchResults = new JSONArray();
		result.put("searchByTitle", searchResults);
		for (String title : titles) {
			JSONObject search = new JSONObject();
			search.put("title", title);
			JSONArray searchResult = new JSONArray();

			if (titleMap.containsKey(title)) {
				for (Song song : titleMap.get(title)) {
					for (String similar : song.getSimilars()) {
						//						System.out.println(similar);
						if (idMap.get(similar) != null) {
							Song similarSong = idMap.get(similar);
							JSONObject songObject = new JSONObject();
							songObject.put("artist",  similarSong.getArtist());
							songObject.put("trackId", similarSong.getTrackId());
							songObject.put("title", similarSong.getTitle());
							//			songObject.put("tags", result.getTags());
							if(!searchResult.contains(songObject)){
								searchResult.add(songObject);
							}
						}	
					}
				}
			}

			searchResults.add(search);
			searchResult.sort(new ComparatorBySearch());
			search.put("similars", searchResult);
		}

		return searchResults;

	}

	/**
	 * Write out Lib Fucntion
	 * @param output
	 * @param orderVal
	 * @throws IOException
	 */
	public void outputTree(String output, String orderVal) {
		Path path = Paths.get(output);

		try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(path, Charset.forName("UTF-8")))) {	

			if (orderVal.equals("artist")) {
				for (String artist : artistMap.navigableKeySet()) {
					for (Song song : artistMap.get(artist)) {
						writer.println(artist + " - " + song.getTitle());
					}
				}
			}
			else if (orderVal.equals("title")) {
				for (String title : titleMap.navigableKeySet()) {
					for (Song song : titleMap.get(title)) {
						writer.println(song.getArtist() + " - " + title);

					}
				}
			}
			else if (orderVal.equals("tag")) {
				for (String tag : tagMap.navigableKeySet()) {
					writer.print(tag + ": ");

					for (Song song : tagMap.get(tag)) {
						writer.print(song.getTrackId() + " ");
					}
					writer.println();
				}
			}
		}	

		catch (IOException e) {
			System.out.println("Bad output file");
		}
	}
}







