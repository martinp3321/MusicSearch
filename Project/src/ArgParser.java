import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.Map.Entry;

public class ArgParser {
	private HashMap<String, String> argMap;
	String[] args;

	private static final int DEFAULT_VALUE = 10;
	
	/**
	 * Parse args
	 * 
	 * @param args
	 * @throws Exception
	 */

	public ArgParser(String[] args)  {
		
		argMap = new HashMap<>();
		argMap.put("-input", null);
		argMap.put("-output", null);
		argMap.put("-order", null);
		argMap.put("-threads", null);
		argMap.put("-searchInput", null);
		argMap.put("-searchOutput", null);

		for (int i = 0; i < args.length-1; i += 1) {
			if (argMap.containsKey(args[i])) {
				argMap.put(args[i], args[i + 1]);
			}
		}

		if (argMap.containsKey("-threads")) {
			int numThreads = DEFAULT_VALUE;
			try {
				numThreads = Integer.parseInt(argMap.get("-threads"));

				if (numThreads < 0 || numThreads > 1000) {
					numThreads = DEFAULT_VALUE;
				}
			} catch (NumberFormatException nfe) {

			}
			argMap.put("-threads", String.valueOf(numThreads));
		}
	}

	/**
	 * Get argparser hashmap value
	 * 
	 * @param key
	 * @return
	 */

	public String getValue(String key) {
		return argMap.get(key);
	}

	/**
	 * Checks args from main
	 * 
	 * @param args
	 * @return
	 */
	public boolean checkBadArg(String[] args) {
		if (args.length < 6) {
			System.out.println("Args less 6");
			return false;
		} else{
			return true;
		}

	}
	/**
	 * Path Check
	 * @param path
	 * @return
	 */
	public boolean validPath(String path) {
		try {
			Path p = Paths.get(path);
			return true;
		}
		catch(Exception e) {
			return false;
		}
		
	}
}
