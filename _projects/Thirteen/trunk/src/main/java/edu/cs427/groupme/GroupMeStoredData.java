package edu.cs427.groupme;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.File;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.logging.Logger;
/**
 * GroupMeStoredData is served to store necessary variables such like GroupMeBotID, GroupMeAccessToken etc
 * in an external file that can be read from and write to
 */
public final class GroupMeStoredData {
	private static final Logger LOGGER = Logger.getLogger(GroupMeStoredData.class.getName());

	public static final String FILEPATH = "groupMeData.json";

	// User Settings
	private static final String GROUPME_TOKEN_KEY = "GroupMeToken";
	private static String groupMeToken = "8fyym11XsTj5XrHTHzNChDDHia0LAM4afuflybhg";
	private static final String GROUPME_GROUP_ID_KEY = "GroupMeGroupId";
	private static String groupMeGroupId = "17407658";
	private static final String GROUPME_GROUP_NAME_KEY = "GroupMeGroupName";
	private static String groupMeGroupName = "JenkinsGroup";
	private static final String GROUPME_BOT_NAME_KEY = "GroupMeBotName";
	private static String groupMeBotName = "JenkinsBot";
	private static final String BOT_COMMAND_PREFIX_KEY = "BotCommandPrefix";
	private static String botCommandPrefix = "!";

	// Data
	private static final String GROUPME_BOT_ID_KEY = "GroupMeBotId";
	private static String groupMeBotId = "";
	private static final String LAST_MESSAGE_ID_KEY = "LastMessageId";
	private static String lastMessageId = "";
	
	//Temporary Data (Not persisted)
	private static String lockedByUsername = null;

	// Instance of the GroupMeIMConnection
	private static GroupMeIMConnection connection = null;
	/**
	 * constructor
	 * @param conn GroupMeIMConnection
	 */
	public static void setIMConnection(GroupMeIMConnection conn) {
		connection = conn;
	}
	/**
	 * initialize stored data
	 */
	public static void init() {
		try {
			if (!dataFileExists(FILEPATH))
				writeToFile(FILEPATH);
			readAllData(FILEPATH);
		} catch (IOException ex) {
			LOGGER.warning("IOException during init of GroupMeStoredData");
		} catch (ParseException e) {
			LOGGER.warning("File contains invalid JSON");
		}
	}

	/**
	 * Determines whether the file at FILEPATH exists
	 * @param fp filepath for the stored data
	 * @return true if the file exists, false if not
	 */
	public static boolean dataFileExists(String fp) {
		File file = new File(fp);
		boolean exists = file.exists();
		LOGGER.info("File at " + FILEPATH + (exists ? " exists" : " does not exist"));
		return exists;
	}

	/**
	 * Writes all data to the data file
	 * @param fp filepath for the stored data
	 * @throws IOException
	 */
	public static void writeToFile(String fp) throws IOException {
		JSONObject obj = new JSONObject();
		JSONObject settings = new JSONObject();
		JSONObject data = new JSONObject();

		settings.put(GROUPME_TOKEN_KEY, groupMeToken);
		settings.put(GROUPME_GROUP_ID_KEY, groupMeGroupId);
		settings.put(GROUPME_GROUP_NAME_KEY, groupMeGroupName);
		settings.put(GROUPME_BOT_NAME_KEY, groupMeBotName);
		settings.put(BOT_COMMAND_PREFIX_KEY, botCommandPrefix);

		data.put(GROUPME_BOT_ID_KEY, groupMeBotId);
		data.put(LAST_MESSAGE_ID_KEY, lastMessageId);

		obj.put("Settings", settings);
		obj.put("Data", data);

		FileWriter file = new FileWriter(fp);
		LOGGER.info("Writing to Stored Data file: " + obj.toJSONString());
		file.write(obj.toJSONString());
		file.flush();
		file.close();
	}
	/**
	 * Reads the file at filepath fp and sets all variables
	 * @param fp filepath for the stored data
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void readAllData(String fp) throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();

		LOGGER.info("Attempting to read from Stored Data file");

		Object obj = parser.parse(new FileReader(fp));
		if (obj == null || !(obj instanceof JSONObject))
			throw new ParseException(ParseException.ERROR_UNEXPECTED_TOKEN);

		JSONObject jsonObject = (JSONObject) obj;

		LOGGER.info("Parsing Stored Data from " + jsonObject.toJSONString());

		JSONObject settings = (JSONObject) jsonObject.get("Settings");
		JSONObject data = (JSONObject) jsonObject.get("Data");

		if (settings != null) {
			groupMeToken = nullCheck((String) settings.get(GROUPME_TOKEN_KEY), "");
			groupMeGroupId = nullCheck((String) settings.get(GROUPME_GROUP_ID_KEY), "");
			groupMeGroupName = nullCheck((String) settings.get(GROUPME_GROUP_NAME_KEY), "JenkinsGroup");
			groupMeBotName = nullCheck((String) settings.get(GROUPME_BOT_NAME_KEY), "JenkinsBot");
			botCommandPrefix = nullCheck((String) settings.get(BOT_COMMAND_PREFIX_KEY), "!");
		}

		if (data != null) {
			groupMeBotId = nullCheck((String) data.get(GROUPME_BOT_ID_KEY), "");
			lastMessageId = nullCheck((String) data.get(LAST_MESSAGE_ID_KEY), "");
		}

		LOGGER.info("Stored Data read from file successfully");
	}
	/**
	 * check if the value is null, if null return default value
	 * @param value value to be checked
	 * @param defaultVal default value
	 * @return default value if the value is null, value if it's not null
	 */
	public static String nullCheck(String value, String defaultVal) {
		return (value == null) ? defaultVal : value;
	}

	// GETTERS
	/**
	 * getter for groupme token
	 * 
	 * @return the token of groupme
	 */
	public static String getGroupMeToken() {
		return groupMeToken;
	}

	/**
	 * getter for the GroupMe group ID
	 * 
	 * @return the GroupMe group ID
	 */
	public static String getGroupMeGroupId() {
		return groupMeGroupId;
	}

	/**
	 * getter for the GroupMe group name
	 * 
	 * @return name of the GroupMe group
	 */
	public static String getGroupMeGroupName() {
		return groupMeGroupName;
	}

	/**
	 * getter for the GroupMe bot name
	 * 
	 * @return name of the GroupMe bot
	 */
	public static String getGroupMeBotName() {
		return groupMeBotName;
	}

	/**
	 * getter for the bot command prefix
	 * 
	 * @return bot command prefix
	 */
	public static String getBotCommandPrefix() {
		return botCommandPrefix;
	}

	/**
	 * getter for the GroupMe bot id
	 * 
	 * @return id of the GroupMe bot
	 */
	public static String getGroupMeBotId() {
		return groupMeBotId;
	}

	/**
	 * getter for the id of last message
	 * 
	 * @return the id of last message
	 */
	public static String getLastMessageId() {
		return lastMessageId;
	}

	// SETTERS
	/**
	 * setter for the GroupMeToken
	 * 
	 * @param token GroupMeToken
	 */
	public static void setGroupMeToken(String token) {
		setGroupMeToken(token, FILEPATH, true);
	}

	/**
	 * setter for the GroupMeToken
	 * @param token GroupMeToken
	 * @param filepath filepath for the stored data
	 * @param causeEffects
	 */
	public static void setGroupMeToken(String token, String filepath, boolean causeEffects) {
		groupMeToken = token;

		try {
			writeToFile(filepath);

			if (causeEffects) {
				GroupMeIMConnection.forceRegisterGroupMeBot();
				connection.startPolling();
			}
		} catch (IOException ex) {
			LOGGER.warning("Error writing GroupMeToken to Stored Data file");
		}
	}
	
	/**
	 * @return the lockedByUsername
	 */
	public static String getLockedByUsername() {
		return lockedByUsername;
	}
	/**
	 * @param lockedByUsername the lockedByUsername to set
	 */
	public static void setLockedByUsername(String lockedByUsername) {
		GroupMeStoredData.lockedByUsername = lockedByUsername;
	}
	
	/**
	 * setter for the GroupMe ID
	 * @param groupId GroupMe Id
	 */
	public static void setGroupMeGroupId(String groupId) {
		setGroupMeGroupId(groupId, FILEPATH, true);
	}
	
	/**
	 * setter for the GroupMe ID
	 * @param groupId GroupMe Id
	 * @param filepath filepath for the stored data
	 * @param causeEffects
	 */
	public static void setGroupMeGroupId(String groupId, String filepath, boolean causeEffects) {
		groupMeGroupId = groupId;

		try {
			writeToFile(filepath);

			if (causeEffects)
				GroupMeIMConnection.forceRegisterGroupMeBot();
		} catch (IOException ex) {
			LOGGER.warning("Error writing GroupMeGroupId to Stored Data file");
		}
	}
	
	/**
	 * setter for the GroupMe group name
	 * @param groupName name of the group chat
	 */
	public static void setGroupMeGroupName(String groupName) {
		setGroupMeGroupName(groupName, FILEPATH, true);
	}
	
	/**
	 * setter for the GroupMe group name
	 * @param groupName name of the group chat
	 * @param filepath filepath for the stored data
	 * @param causeEffects
	 */
	public static void setGroupMeGroupName(String groupName, String filepath, boolean causeEffects) {
		groupMeGroupName = groupName;

		try {
			writeToFile(filepath);

			if (causeEffects)
				connection.instantiateIMBot();
		} catch (IOException ex) {
			LOGGER.warning("Error writing GroupMeGroupName to Stored Data file");
		}
	}
	/**
	 * setter for the GroupMeBot name
	 * @param name name of the GroupMe bot
	 */
	public static void setGroupMeBotName(String name) {
		setGroupMeBotName(name, FILEPATH, true);
	}
	
	/**
	 * setter for the GroupMeBot name
	 * @param name name of the GroupMe bot
	 * @param filepath filepath for the stored data
	 * @param causeEffects
	 */
	public static void setGroupMeBotName(String name, String filepath, boolean causeEffects) {
		groupMeBotName = name;

		try {
			writeToFile(filepath);

			if (causeEffects) {
				GroupMeIMConnection.forceRegisterGroupMeBot();
				connection.instantiateIMBot();
			}
		} catch (IOException ex) {
			LOGGER.warning("Error writing GroupMeBotName to Stored Data file");
		}
	}
	
	/**
	 * setter for the bot command prefix
	 * @param prefix command prefix
	 */
	public static void setBotCommandPrefix(String prefix) {
		setBotCommandPrefix(prefix, FILEPATH, true);
	}
	/**
	 * setter for the bot command prefix
	 * @param prefix command prefix
	 * @param filepath filepath for the stored data
	 * @param causeEffects
	 */
	public static void setBotCommandPrefix(String prefix, String filepath, boolean causeEffects) {
		botCommandPrefix = prefix;

		try {
			writeToFile(filepath);

			if (causeEffects) {
				connection.instantiateIMBot();
				connection.startPolling();
			}
				
		} catch (IOException ex) {
			LOGGER.warning("Error writing BotCommandPrefix to Stored Data file");
		}
	}
	
	/**
	 * setter for the GroupMe Bot ID
	 * @param id GroupMe Bot id
	 */
	public static void setGroupMeBotId(String id) {
		setGroupMeBotId(id, FILEPATH);
	}

	/**
	 * setter for the GroupMe Bot ID
	 * @param id GroupMe Bot id
	 * @param filepath filepath for the stored data
	 */
	public static void setGroupMeBotId(String id, String filepath) {
		groupMeBotId = id;

		try {
			writeToFile(filepath);
		} catch (IOException ex) {
			LOGGER.warning("Error writing GroupMeBotId to Stored Data file");
		}
	}
	
	/**
	 * setter for the last message id
	 * @param id id of the last message
	 */
	public static void setLastMessageId(String id) {
		setLastMessageId(id, FILEPATH);
	}
	
	/**
	 * setter for the last message id
	 * @param id id of the last message
	 * @param filepath filepath for the stored data
	 */
	public static void setLastMessageId(String id, String filepath) {
		lastMessageId = id;

		try {
			writeToFile(filepath);
		} catch (IOException ex) {
			LOGGER.warning("Error writing LastMessageId to Stored Data file");
		}
	}
}
