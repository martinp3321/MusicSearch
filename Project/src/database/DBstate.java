package database;

public enum DBstate {

	/**
	 * State Codes
	 */
	NOERROR("No errors occured."),
	ERROR("Unknown error occurred."),
	MISSING_CONFIG("Unable to find configuration file."),
	MISSING_VALUES("Missing values in configuration file."),
	CONNECTION_FAILUIRE("Failuire to establish a database connection."),
	CREATETABLE_FAILUIRE("Failuire to create necessary tables."),
	FAVORITETABLE_FAILUIRE("Failuire to find favorite tables."),
	INVALID_LOGIN("Invalid username and/or password."),
	INVALID_SIGNUP("Invalid username and/or password for account creation."),
	INVALID_USER("User does not exist."),
	DUPLICATE_USER("User with that username already exists."),
	DUPLICATE_FAVORITE("User has already favorited that trackId"),
	SQL_ISSUE("Unable to execute SQL statement.");

	private final String statusmessage;

	private DBstate(String message) {
		this.statusmessage = message;
	}

	public String statusmessage() {
		return statusmessage;
	}

	public String toString() {
		return this.statusmessage;
	}
}
