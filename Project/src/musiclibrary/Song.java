package musiclibrary;
import java.util.ArrayList;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Song {

	/**
	 * Declare appropriate instance variables.
	 */

	private String artist;
	private String trackId;
	private String title;
	private ArrayList<String> tags;
	private ArrayList<String> similars;

	/**
	 * Constructor.
	 * 
	 * @param artist
	 * @param trackId
	 * @param title
	 * 
	 * @param tags
	 */
	public Song(String artist, String trackId, String title, ArrayList<String> tags, ArrayList<String> similars) {
		this.artist = artist;
		this.trackId = trackId;
		this.title = title;
		this.tags = tags;
		this.similars = similars;
		
	}
	
	public Song(Song song) {
		this.artist = song.artist;
		this.trackId = song.trackId;
		this.title = song.title;
		this.tags = song.tags;
		this.similars = song.similars;
		
		//this = song.deepCopy();
	}

	/**
	 * Constructor that takes as input a JSONObject and constructs a Song object
	 * by extracting the relevant data.
	 * 
	 * @param object
	 */
	public Song(JSONObject contents) {

		this.artist = (String) contents.get("artist");
		this.title = (String) contents.get("title");
		this.trackId = (String) contents.get("track_id");

		this.tags = new ArrayList<String>();
		JSONArray tagsArray = (JSONArray) contents.get("tags");
		for (int i = 0; i < tagsArray.size(); i++) {
			tags.add((String) ((JSONArray) tagsArray.get(i)).get(0));
		}
		
		
		this.similars = new ArrayList<String>();
		JSONArray similarsArray = (JSONArray) contents.get("similars");
		for (int i = 0; i < similarsArray.size(); i++) {
			similars.add((String) ((JSONArray) similarsArray.get(i)).get(0));
		}
	}
	/**
	 * Deep Copy of Song
	 * @return
	 */

	public Song deepCopy(){
	
		ArrayList<String> newTags = new ArrayList<String>();
		for (String tag : this.getTags()){
			newTags.add(tag);
		}
		
		ArrayList<String> newSimilars = new ArrayList<String>();
		for (String similar : this.getSimilars()){
			newTags.add(similar);
		}

		Song dCopy = new Song(this.getArtist(), this.getTrackId(), this.getTitle(), newTags, newSimilars);
		return dCopy;
	}


	/**
	 * Return artist.
	 * 
	 * @return
	 */
	public String getArtist() {
		return artist;
	}

	/**
	 * Return track ID.
	 * 
	 * @return
	 */
	public String getTrackId() {
		return trackId;
	}

	/**
	 * Return title.
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Return a list of all tags for this track.
	 * 
	 * @return
	 */
	public ArrayList<String> getTags() {
		return tags;
	}
	
	
	/**
	 * Return a list of all similars for this track.
	 * 
	 * @return
	 */
	public ArrayList<String> getSimilars() {
		return similars;
	}

	/**
	 * Set artist.
	 * 
	 * @param string
	 * @return
	 */
	public void setArtist(String artist) {
		this.artist = artist;

	}

	/**
	 * Set track ID.
	 *
	 */
	public void setTrackId(String trackId) {
		this.trackId = trackId;

	}
	
	
	
	/**
	 * Set similars.
	 *
	 */
	public void setSimilars(ArrayList<String> similars) {
		this.similars = similars;

	}

	/**
	 * Set title.
	 * 
	 * @return
	 */
	public void setTitle(String title) {
		this.title = title;

	}

	/**
	 * Seta list of all tags for this track.
	 */
	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	
//	public String toString() {
//		return this.title;
//	}
}
