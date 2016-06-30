package musiclibrary;
import java.nio.file.Path;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.nio.file.DirectoryStream;

/**
 * Martin Murphy
 */

public class SongBuilder {

	private MultiThreadLibrary lib;
	private WorkQueue queue;
	
	

	public SongBuilder(Path path, int numThreads) {
		this.lib = new MultiThreadLibrary();
		this.queue = new WorkQueue(numThreads);
		sdpHelper(path, ".json");
		queue.shutdown();
		queue.waitTermination();
		
	}

	/**
	 * Takes in path from driver and builds paths for parser
	 * 
	 * @param path
	 * @param extension
	 */
	private void sdpHelper(Path path, String extension) {
		if (Files.isDirectory(path)) {
			try (DirectoryStream<Path> dir = Files.newDirectoryStream(path)) {
				for (Path entry : dir) {
					sdpHelper(entry, extension);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (path.toString().toLowerCase().endsWith(extension.trim())) {
			queue.execute(new SongWorker(path));
		}
	}



	/**
	 * Returns multi library to main
	 * 
	 * @return
	 */

	public MultiThreadLibrary getLibrary() {
		return lib;
	}
	
	
	class SongWorker implements Runnable {
		Path myPath;
		
		public SongWorker(Path path) {
			this.myPath = path;
		}

		@Override
		public void run() {
			
			try (BufferedReader reader = Files.newBufferedReader(myPath, Charset.forName("UTF-8"))) {

				JSONParser parser = new JSONParser();
				JSONObject contents = (JSONObject) parser.parse(reader);
				Song song = new Song(contents);
				lib.addSong(song);

			} catch (IOException e) {
				e.printStackTrace();

			} catch (ParseException e) {
				e.printStackTrace();

			}
		}
	}
}
