import java.nio.file.Paths;

import musiclibrary.MultiThreadLibrary;
import musiclibrary.Search;
import musiclibrary.SongBuilder;

/**
 * 
 * @author Martin Murphy CS212 S16 Music Library
 */
public class Driver {
	public static void main(String[] args) {

		ArgParser argStart = new ArgParser(args);

		if (!argStart.checkBadArg(args)) {
			return;
		}

		// build
		SongBuilder songBuild = new SongBuilder(Paths.get(argStart.getValue("-input")),Integer.parseInt(argStart.getValue("-threads")));

		MultiThreadLibrary lib = songBuild.getLibrary();

		// search
		if (argStart.getValue("-searchInput") != null && argStart.getValue("-searchOutput") != null) {
			Search.search(lib, argStart.getValue("-searchInput"), argStart.getValue("-searchOutput"));
		}
		
		// Write
		lib.outputTree(argStart.getValue("-output"), argStart.getValue("-order"));

	}
}
