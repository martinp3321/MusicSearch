package musiclibrary;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MultiThreadLibrary extends SongLibrary {
	private ReentrantLock lock;
	
	public MultiThreadLibrary(){
		super();
		lock = new ReentrantLock();
		
	}

	/**
	 * Add a song to the library.
	 * 
	 * appropriate data structures.
	 * 
	 * @param song
	 */

	public void addSong(Song song) {
		
		/// add titles
		lock.lockWrite();
		try {
			super.addSong(song);
		} finally {
			lock.unlockWrite();
		}
	}
	
	

	/**
	 * Return the song associated with a unique track ID.
	 * 
	 * @param trackId
	 * @return
	 */

	public TreeSet<Song> getTitle(String title) {
		lock.lockRead();
		
		try{
			TreeSet<Song> curr = super.getTitle(title);
			TreeSet<Song> copy =new TreeSet<Song>();
			for (Song s : curr){
				copy.add(s.deepCopy());
			}
			return copy;
		}
		finally{
			lock.unlockRead();
		}
		
	}

	/**
	 * Get tag
	 * 
	 * @param tag
	 * @return
	 */
	public TreeSet<Song> getTag(String tag) {

		lock.lockRead();
		
		try{
			TreeSet<Song> curr = super.getTag(tag);
			TreeSet<Song> copy =new TreeSet<Song>();
			for (Song s : curr){
				copy.add(s.deepCopy());
			}
			return copy;
		}
		finally{
			lock.unlockRead();
		}
		
	}
	/**
	 * search all artists by alpha
	 */
	public JSONArray searchByArtistAlpha() {
		lock.lockRead();
		try{
			
			return super.searchByArtistAlpha();
		}
		finally{
			lock.unlockRead();
		}
		
	}
	
	
	/**
	 * searchByArtist lock
	 */
		
	public JSONArray searchByArtist(String[] artists) {
		lock.lockRead();
		try{
			
			return super.searchByArtist(artists);
		}
		finally{
			lock.unlockRead();
		}		
	}
	
	
	/**
	 * searchByTitle lock
	 */
	
	public JSONArray searchByTitle(String[] titles) {
		lock.lockRead();
		try{
			
			return super.searchByTitle(titles);
		}
		finally{
			lock.unlockRead();
		}
		
	}
	
	/**
	 * searchByTAG lock
	 */
	public JSONArray searchByTag(String[] tags) {
		lock.lockRead();
		try{
			
			return super.searchByTag(tags);
		}
		finally{
			lock.unlockRead();
		}
		
	}

	/**
	 * Return a sorted set of all songs by a given artist.
	 * 
	 * @param artist
	 * @return
	 */
	public TreeSet<Song> getSongsByArtist(String artist) {

		lock.lockRead();
		
		try{
			TreeSet<Song> curr = super.getSongsByArtist(artist);
			TreeSet<Song> copy =new TreeSet<Song>();
			for (Song s : curr){
				copy.add(s.deepCopy());
			}
			return copy;
		}
		finally{
			lock.unlockRead();
		}
		
	}

	/**
	 * Write out Lib Fucntion
	 * 
	 * @param output
	 * @param orderVal
	 * @throws IOException
	 */
	public void outputTree(String output, String orderVal) {
		lock.lockRead();
		try {
			super.outputTree(output, orderVal);
		} finally {
			lock.unlockRead();
		}
	}
	
	
	
}
