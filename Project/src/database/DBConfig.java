package database;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;



public class DBConfig {

	
	private final String DataBasePath;
	private final Properties DataBaseLogin;

	/**
	 * Take in DB info file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public DBConfig() throws FileNotFoundException, IOException {
		this("databaseInfo.properties");
	}

	/**
	 * Load/Set Properties
	 * @param configPath
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public DBConfig(String configPath) throws FileNotFoundException,
			IOException {

		Properties config = loadConfig(configPath);

		DataBasePath = String.format("jdbc:mysql://%s/%s",
				config.getProperty("hostname"),
				config.getProperty("database"));

		DataBaseLogin = new Properties();
		DataBaseLogin.put("user", config.getProperty("username"));
		DataBaseLogin.put("password", config.getProperty("password"));
	}

	/**
	 * Load Config
	 * @param configPath
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private Properties loadConfig(String configPath)
			throws FileNotFoundException, IOException {

		List<String> keys = Arrays.asList(new String[] { "username",
				"password", "database", "hostname" });

		Properties config = new Properties();
		config.load(new FileReader(configPath));

		if (!config.keySet().containsAll(keys)) {
			String error = "Must provide the following in properties file: ";
			throw new InvalidPropertiesFormatException(error + keys);
		}

		return config;
	}

	/**
	 * Acquire Connection
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		Connection dbConnection = DriverManager.getConnection(DataBasePath, DataBaseLogin);
		dbConnection.setAutoCommit(true);
		return dbConnection;
	}

	/**
	 * Setup/Execute SQL query
	 * @param db
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public Statement executeSQL(Connection db, String sql) throws SQLException {
		Statement statement = db.createStatement();
		statement.execute(sql);
		return statement;
	}

	/**
	 * Test DB connection
	 * @return
	 */
	public boolean testConnection() {

		Connection db = null;
		Statement sql = null;
		ResultSet results = null;

		boolean okay = false;

		try {
			System.out.println("Connecting to " + DataBasePath);
			db = getConnection();

			System.out.println("Executing SHOW DATABASES...");
			sql = executeSQL(db, "SHOW DATABASES;");

			results = sql.getResultSet();

			while (results.next()) {
				System.out.println("Found: " + results.getString("Database"));
			}

			sql.close();

			okay = true;
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
		finally {
			try {
				db.close();
			} catch (SQLException e) {
				System.err.println(e.getMessage());
				okay = false;
			}
		}

		return okay;
	}

	/**
	 * Connect to DB
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DBConfig test = new DBConfig("databaseInfo.properties");

			if (test.testConnection()) {
				System.out.println("Connection to DB pass");
			}
			else {
				System.err.println("Connection to DB fail");
			}
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
