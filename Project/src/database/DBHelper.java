package database;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

//import java.util.Random;
import java.security.SecureRandom;



public class DBHelper {


	private static DBHelper instantHand = new DBHelper();
	private static DBConfig db;
	//Random Salt
	private SecureRandom random;

	/**
	 * Create user_credentials table
	 */
	private static final String CREATE_SQL =
			"CREATE TABLE user_credentials (" +
			"userid INTEGER AUTO_INCREMENT PRIMARY KEY," +
			"userfullname CHAR(64) NOT NULL," +
			"username VARCHAR(32) NOT NULL UNIQUE," +
			"password CHAR(64) NOT NULL," +
			"usersalt CHAR(32) NOT NULL);";
	/**
	 * Create user_favorites table
	 */
	private static final String CREATE_FAVORITES_SQL =
			"CREATE TABLE user_favorites ( " + "username VARCHAR(128) NOT NULL, " + "trackid VARCHAR(128) NOT NULL);";
	/**
	 * Add favorites
	 */
	private static final String INSERT_FAVORITES_SQL = "INSERT INTO user_favorites VALUES (?, ?);";
	
	
	
	/**
	 * Retrieve trackIds for favorite display
	 */
	private static final String GET_FAVORITES_SQL = "SELECT trackid FROM user_favorites WHERE username = ?";


	/**
	 * Add new user
	 */
	private static final String REGISTER_SQL =
			"INSERT INTO user_credentials (userfullname,username, password, usersalt) " +
			"VALUES ( ?, ?, ?, ?);";
	
	/**
	 * Get fullname
	 */
	private static final String FULLNAME_SQL =
			"SELECT userfullname FROM user_credentials WHERE userfullname = ?";
	
	/**
	 * Get username from DB
	 */
	private static final String USER_SQL =
			"SELECT username FROM user_credentials WHERE username = ?";

	
	/**
	 * get salt
	 */
	private static final String SALT_SQL =
			"SELECT usersalt FROM user_credentials WHERE username = ?";

	/**
	 * get credentials
	 */
	private static final String AUTH_SQL =
			"SELECT username FROM user_credentials " +
			"WHERE username = ? AND password = ?";

	/**
	 * Build DBs
	 */
	private DBHelper() {

		DBstate state = DBstate.NOERROR;
		DBstate stateTwo = DBstate.NOERROR;
		random = new SecureRandom();
		byte bytes[] = new byte[16];
	    random.nextBytes(bytes);

		try {
			db = new DBConfig();
			state = db.testConnection() ? setupTables() : DBstate.CONNECTION_FAILUIRE;
			stateTwo = db.testConnection() ? setupFavTables() : DBstate.CONNECTION_FAILUIRE;
		}
		catch (FileNotFoundException e) {
			state = DBstate.MISSING_CONFIG;
			stateTwo = DBstate.MISSING_CONFIG;
		}
		catch (IOException e) {
			state = DBstate.MISSING_VALUES;
			stateTwo = DBstate.MISSING_CONFIG;
		}

		if (state != DBstate.NOERROR) {
			//log.fatal(status.message());
			System.exit(-state.ordinal());
		}
		
		if (stateTwo != DBstate.NOERROR) {
			//log.fatal(status.message());
			System.exit(-state.ordinal());
		}
	}

	/**
	 * Create instance
	 * @return
	 */
	public static DBHelper getInstance() {
		return instantHand;
	}
	
	/**
	 * string check
	 * @param text
	 * @return
	 */
	public static boolean checkString(String text) {
		return text == null || text.trim().isEmpty();
	}


	/**
	 * user_credentials setup
	 * @return
	 */
	private DBstate setupTables() {

		DBstate state = DBstate.ERROR;

		Connection connection = null;
		Statement statement = null;
		ResultSet results = null;

		try {
			connection = db.getConnection();
			statement = connection.createStatement();
			results = statement.executeQuery("SHOW TABLES LIKE 'user_credentials';");

			if (!results.next()) {
				
				statement.executeUpdate(CREATE_SQL);
			
				results = statement.executeQuery("SHOW TABLES LIKE 'user_credentials';");
				state = (results.next()) ? DBstate.NOERROR : DBstate.CREATETABLE_FAILUIRE;
			}
			else {
				
				state = DBstate.NOERROR;
			}

			results.close();
			statement.close();
		}
		catch (Exception ex) {
			state = DBstate.CREATETABLE_FAILUIRE;
			
		}
		finally {
			
			try { connection.close(); }
			catch (Exception ignored) { }
		}

		return state;
	}
	
	
	/**
	 * user_favorites setup
	 * @return
	 */
	private DBstate setupFavTables() {

		DBstate stateTwo = DBstate.ERROR;

		Connection connection = null;
		Statement statement = null;
		ResultSet results = null;

		try {
			connection = db.getConnection();
			statement = connection.createStatement();
			results = statement.executeQuery("SHOW TABLES LIKE 'user_favorites';");

			if (!results.next()) {
				
				
				statement.executeUpdate(CREATE_FAVORITES_SQL);
				results = statement.executeQuery("SHOW TABLES LIKE 'user_favorites';");
				stateTwo = (results.next()) ? DBstate.NOERROR : DBstate.CREATETABLE_FAILUIRE;
			}
			else {
				
				stateTwo = DBstate.NOERROR;
			}

			results.close();
			statement.close();
		}
		catch (Exception ex) {
			stateTwo = DBstate.CREATETABLE_FAILUIRE;
			
		}
		finally {
			
			try { connection.close(); }
			catch (Exception ignored) { }
		}

		return stateTwo;
	}
	
	
	
	
	
	/**
	 * Favorite display from DB
	 * @param username
	 * @param writer
	 * @return
	 * @throws SQLException
	 */
	public HashMap<String,String> getFavTable(String username) throws SQLException {
		
		HashMap<String,String> favList= new HashMap<String,String>();	    
	   
		Connection connection = null;	
			
	    try {
	    	connection = db.getConnection();
			PreparedStatement statement = connection.prepareStatement(GET_FAVORITES_SQL);
			statement.setString(1, username);
			
			ResultSet results = statement.executeQuery();
		
					
			while (results.next()) {
	       
	            String trackid = results.getString("trackid");
	            System.out.println(trackid);
	            	            
	            if(!favList.containsKey(trackid)){
	            	
	            	favList.put(trackid, username);
	            	
	            }	                       	
	            
	            else{
	            	System.out.println("already favorited");	            	
	            }	            
	        }
			
	    } catch (SQLException e) {
	        System.out.println("sql exception");
	    }
	    
	    finally {
			try { connection.close(); }
			catch (Exception ignored) { }
		}
	    
		return favList;
		
	}
	


	/**
	 * Add favorite 
	 * @param username
	 * @param trackid
	 * @return
	 */
	public DBstate addFavorite(String username, String trackid) {
		Connection connection = null;
		
		DBstate stateTwo = DBstate.ERROR;	

		
		if (checkString(username) || checkString(trackid)) {
			stateTwo = DBstate.ERROR;
			
			return stateTwo;
		}

		
		try {
			connection = db.getConnection();
			stateTwo = duplicateFavorite(connection, username,trackid);
			

			
			if (stateTwo == DBstate.NOERROR) {
				stateTwo = addFavorite(connection, username, trackid);
			}
		}
		catch (SQLException ex) {
			stateTwo = DBstate.CONNECTION_FAILUIRE;
		
		}
		finally {
			try { connection.close(); }
			catch (Exception ignored) { }
		}

		return stateTwo;
	}
	/**
	 * Add Favorite
	 * @param connection
	 * @param username
	 * @param trackid
	 * @return
	 */
	public DBstate addFavorite(Connection connection,String username, String trackid){


		DBstate stateTwo = DBstate.ERROR;
		
		try {
			PreparedStatement statement = connection.prepareStatement(INSERT_FAVORITES_SQL);
			statement.setString(1, username);
			statement.setString(2, trackid);		
			statement.executeUpdate();
			statement.close();

			stateTwo = DBstate.NOERROR;
			System.out.println("favorite added");
		}
		catch (SQLException ex) {
			stateTwo = DBstate.SQL_ISSUE;
			
		}
		return stateTwo;

	}
	
	
	/**
	 * Encode for password
	 * @param bytes
	 * @param length
	 * @return
	 */
	public static String encoder(byte[] bytes, int length) {
		BigInteger intB = new BigInteger(1, bytes);
		String hex = String.format("%0" + length + "X", intB);

		assert hex.length() == length;
		return hex;
	}

	/**
	 * Hash password
	 * @param password
	 * @param salt
	 * @return
	 */
	public static String getHash(String password, String salt) {
		String salted = salt + password;
		String hashed = salted;

		try {
			MessageDigest btc = MessageDigest.getInstance("SHA-256");
			btc.update(salted.getBytes());
			hashed = encoder(btc.digest(), 64);
		}
		catch (Exception ex) {
			System.out.println("getHash");
			
		}

		return hashed;
	}
	
	/**
	 * Acuire random salt
	 * @param connection
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	private String getSalt(Connection connection, String user) throws SQLException {
		assert connection != null;
		assert user != null;

		String salt = null;

		PreparedStatement statement = connection.prepareStatement(SALT_SQL);
		statement.setString(1, user);

		ResultSet results = statement.executeQuery();

		if (results.next()) {
			salt = results.getString("usersalt");
		}

		results.close();
		statement.close();

		return salt;
	}


	

	/**
	 * Add user
	 * @param connection
	 * @param newfullname
	 * @param newuser
	 * @param newpass
	 * @return
	 */
	private DBstate registerUser(Connection connection,String newfullname, String newuser, String newpass) {

		DBstate state = DBstate.ERROR;

		byte[] saltBytes = new byte[16];
		random.nextBytes(saltBytes);

		String usersalt = encoder(saltBytes, 32);
		String passhash = getHash(newpass, usersalt);

		try {
			PreparedStatement statement = connection.prepareStatement(REGISTER_SQL);
			statement.setString(1, newfullname);
			statement.setString(2, newuser);
			statement.setString(3, passhash);
			statement.setString(4, usersalt);
			statement.executeUpdate();
			statement.close();

			state = DBstate.NOERROR;
		}
		catch (SQLException ex) {
			state = DBstate.SQL_ISSUE;
			
		}
		return state;
	}

	/**
	 * Add user
	 * @param newfullname
	 * @param newuser
	 * @param newpass
	 * @return
	 */
	public DBstate registerUser(String newfullname,String newuser, String newpass) {
		Connection connection = null;
		DBstate state = DBstate.INVALID_SIGNUP;	

		
		if (checkString(newuser) || checkString(newpass)) {
			state = DBstate.INVALID_SIGNUP;
			
			return state;
		}
		
		try {
			connection = db.getConnection();
			state = duplicateUser(connection, newuser);

			
			if (state == DBstate.NOERROR) {
				state = registerUser(connection, newfullname, newuser, newpass);
			}
		}
		catch (SQLException ex) {
			state = DBstate.CONNECTION_FAILUIRE;
		
		}
		finally {
			try { connection.close(); }
			catch (Exception ignored) { }
		}

		return state;
	}

	
	

	/**
	 * Authenticate User Credentials
	 * @param connection
	 * @param username
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	private DBstate authenticateUser(Connection connection, String username,
			String password) throws SQLException {

		DBstate state = DBstate.ERROR;

		try {
			String usersalt = getSalt(connection, username);
			String passhash = getHash(password, usersalt);

			PreparedStatement statement = connection.prepareStatement(AUTH_SQL);
			statement.setString(1, username);
			statement.setString(2, passhash);

			ResultSet results = statement.executeQuery();
			state = results.next() ? state = DBstate.NOERROR : DBstate.INVALID_LOGIN;
			results.close();
			statement.close();
		}
		catch (SQLException e) {
			
			state = DBstate.SQL_ISSUE;
		}

		return state;
	}

	
	/**
	 * Authenticate User Credentials
	 * @param username
	 * @param password
	 * @return
	 */
	public DBstate authenticateUser(String username, String password) {
		Connection connection = null;
		DBstate state = DBstate.ERROR;

		try {
			connection = db.getConnection();
			state = authenticateUser(connection, username, password);
		}
		catch (SQLException ex) {
			state = DBstate.CONNECTION_FAILUIRE;
			
		}
		finally {
			try { connection.close(); }
			catch (Exception ignored) { }
		}

		return state;
	}
	
	
	/**
	 * Authenticate User Favorite
	 * @param db
	 * @param user
	 * @param trackid
	 * @return
	 */
	private DBstate duplicateFavorite(Connection db, String user, String trackid) {

		assert db != null;
		assert user != null;
		assert trackid != null;

		DBstate stateTwo = DBstate.ERROR;

		try {
			PreparedStatement statement = db.prepareStatement(GET_FAVORITES_SQL);
			statement.setString(1, trackid);

			ResultSet results = statement.executeQuery();
			stateTwo = results.next() ? DBstate.DUPLICATE_FAVORITE : DBstate.NOERROR;
			results.close();
			statement.close();
		}
		catch (SQLException e) {
			
			stateTwo = DBstate.SQL_ISSUE;
		}

		return stateTwo;
	}

	/**
	 * Authenticate User Favorite
	 * @param user
	 * @param trackid
	 * @return
	 */
	public DBstate duplicateFavorite(String user, String trackid) {
		Connection connection = null;
		DBstate stateTwo = DBstate.ERROR;

		try {
			connection = db.getConnection();
			stateTwo = duplicateFavorite(connection, user, trackid);
		}
		catch (SQLException e) {
			stateTwo = DBstate.CONNECTION_FAILUIRE;
			
		}
		finally {
			try { connection.close(); }
			catch (Exception ignored) { }
		}

		return stateTwo;
	}
	
	
	
	
	/**
	 * Authenticate User 
	 * @param connection
	 * @param user
	 * @return
	 */
	private DBstate duplicateUser(Connection connection, String user) {

		assert connection != null;
		assert user != null;

		DBstate state = DBstate.ERROR;

		try {
			PreparedStatement statement = connection.prepareStatement(USER_SQL);
			statement.setString(1, user);

			ResultSet results = statement.executeQuery();
			state = results.next() ? DBstate.DUPLICATE_USER : DBstate.NOERROR;
			results.close();
			statement.close();
		}
		catch (SQLException e) {
			
			state = DBstate.SQL_ISSUE;
		}

		return state;
	}

	/**
	 * Authenticate User 
	 * @param user
	 * @return
	 */
	public DBstate duplicateUser(String user) {
		Connection connection = null;
		DBstate state = DBstate.ERROR;

		try {
			connection = db.getConnection();
			state = duplicateUser(connection, user);
		}
		catch (SQLException e) {
			state = DBstate.CONNECTION_FAILUIRE;
			
		}
		finally {
			try { connection.close(); }
			catch (Exception ignored) { }
		}

		return state;
	}
}
