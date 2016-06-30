package musiclibrary;
import java.util.Comparator;

import org.json.simple.JSONObject;

/**
 * 
 * Sort by Alpha on artists
 *
 */
class ComparatorByAlpha implements Comparator<String> {
    public int compare(String paramT1, String paramT2) {
    	
    	String oneSongArtist = (String) paramT1;
    	String twoSongArtist = (String) paramT2;
    	
    	return oneSongArtist.compareTo(twoSongArtist);       
    }
}