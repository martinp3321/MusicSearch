package musiclibrary;
import java.util.Comparator;

/**
 * 
 * Sort by Title
 *
 */

public class ComparatorByTitle implements Comparator<Song> {
    public int compare(Song paramT1, Song paramT2) {
    	
    	if(!paramT1.getTitle().equals(paramT2.getTitle())){
        	return paramT1.getTitle().compareTo(paramT2.getTitle());
        	
        }
    	else if(!paramT1.getArtist().equals(paramT2.getArtist())){
        	return paramT1.getArtist().compareTo(paramT2.getArtist());       	
        }
        
        else{
        	
        	return paramT1.getTrackId().compareTo(paramT2.getTrackId());
        }           
    }
}