package musiclibrary;
import java.util.Comparator;

/**
 * 
 * Sort by trackId
 *
 */
class ComparatorByTrackId implements Comparator<Song> {
    public int compare(Song paramT1, Song paramT2) {
    	
    	return paramT1.getTrackId().compareTo(paramT2.getTrackId())    ;       
    }
}