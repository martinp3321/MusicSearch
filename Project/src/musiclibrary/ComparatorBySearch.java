package musiclibrary;
import java.util.Comparator;

import org.json.simple.JSONObject;

/**
 * 
 * Sort by trackId
 *
 */
class ComparatorBySearch implements Comparator<JSONObject> {
    public int compare(JSONObject paramT1, JSONObject paramT2) {
    	
    	String oneSongID = (String) paramT1.get("trackId");
    	String twoSongID = (String) paramT2.get("trackId");
    	
    	return oneSongID.compareTo(twoSongID);       
    }
}

