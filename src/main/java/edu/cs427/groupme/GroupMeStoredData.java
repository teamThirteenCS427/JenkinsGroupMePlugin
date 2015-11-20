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

public final class GroupMeStoredData 
{
	private static final Logger LOGGER = Logger.getLogger(GroupMeStoredData.class.getName());

	private static final String FILEPATH = "groupMeData.json";
	
	//User Settings
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
    
    //Data
    private static final String GROUPME_BOT_ID_KEY = "GroupMeBotId";
    private static String groupMeBotId = "";
	private static final String LAST_MESSAGE_ID_KEY = "LastMessageId";
	private static String lastMessageId = "";
    
    //Instance of the GroupMeIMConnection
    private static GroupMeIMConnection connection = null;
    
    
    
    public static void setIMConnection(GroupMeIMConnection conn)
    {
    	connection = conn;
    }
	
    public static void init()
    {
    	try 
    	{
			if(!dataFileExists(FILEPATH))
				writeToFile(FILEPATH);
			readAllData(FILEPATH);
		}
		catch (IOException ex)
		{
			LOGGER.warning("IOException during init of GroupMeStoredData");
		}
    	catch (ParseException e)
    	{
    		LOGGER.warning("File contains invalid JSON");
    	}
    }
    
    //Determines whether the file at FILEPATH exists
    public static boolean dataFileExists(String fp)
    {
    	File file = new File(fp);
    	boolean exists = file.exists();
    	LOGGER.info("File at " + FILEPATH + (exists ? " exists" : " does not exist"));
		return exists;
    }
    
    //Writes all data to the data file
    public static void writeToFile(String fp) throws IOException
    {
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
    
    //Reads the file at filepath fp and sets all variables
    private static void readAllData(String fp) throws FileNotFoundException, IOException, ParseException
    {
    	JSONParser parser = new JSONParser();

		LOGGER.info("Attempting to read from Stored Data file");
		 
        Object obj = parser.parse(new FileReader(fp));
		if (obj == null || !(obj instanceof JSONObject))
			throw new ParseException(ParseException.ERROR_UNEXPECTED_TOKEN);

        JSONObject jsonObject = (JSONObject) obj;
        
        LOGGER.info("Parsing Stored Data from " + jsonObject.toJSONString());
        
        JSONObject settings = (JSONObject) jsonObject.get("Settings");
        JSONObject data = (JSONObject) jsonObject.get("Data");

		if (settings != null)
		{
		    groupMeToken = nullCheck((String) settings.get(GROUPME_TOKEN_KEY), "");
		    groupMeGroupId = nullCheck((String) settings.get(GROUPME_GROUP_ID_KEY), "");
		    groupMeGroupName = nullCheck((String) settings.get(GROUPME_GROUP_NAME_KEY), "JenkinsGroup");
		    groupMeBotName = nullCheck((String) settings.get(GROUPME_BOT_NAME_KEY), "JenkinsBot");
		    botCommandPrefix = nullCheck((String) settings.get(BOT_COMMAND_PREFIX_KEY), "!");
		}
        
        if (data != null)
        {
		    groupMeBotId = nullCheck((String) data.get(GROUPME_BOT_ID_KEY), "");
		    lastMessageId = nullCheck((String) data.get(LAST_MESSAGE_ID_KEY), "");
		}
        
        LOGGER.info("Stored Data read from file successfully");
    }
    
    public static String nullCheck(String value, String defaultVal)
    {
		return (value == null) ? defaultVal : value;
	}
    
	//GETTERS
    public static String getGroupMeToken()
    {
    	return groupMeToken;
    }
    
    public static String getGroupMeGroupId()
    {
    	return groupMeGroupId;
    }
    
    public static String getGroupMeGroupName()
    {
    	return groupMeGroupName;
    }
    
    public static String getGroupMeBotName()
    {
    	return groupMeBotName;
    }
    
    public static String getBotCommandPrefix()
    {
    	return botCommandPrefix;
    }
    
    public static String getGroupMeBotId()
    {
    	return groupMeBotId;
    }
	
    public static String getLastMessageId()
    {
    	return lastMessageId;
    }
	
    //SETTERS
    public static void setGroupMeToken(String token)
    {
		setGroupMeToken(token, FILEPATH, true);
    }
    
    public static void setGroupMeToken(String token, String filepath, boolean causeEffects)
    {
		groupMeToken = token;
		
		try {
			writeToFile(filepath);
		
			if (causeEffects)
			{
				GroupMeIMConnection.forceRegisterGroupMeBot();
				connection.startPolling();
			}
		} catch (IOException ex) {
			LOGGER.warning("Error writing GroupMeToken to Stored Data file");
		}
    }
    
    
    public static void setGroupMeGroupId(String groupId)
    {
		setGroupMeGroupId(groupId, FILEPATH, true);
    }
    
    public static void setGroupMeGroupId(String groupId, String filepath, boolean causeEffects)
    {
		groupMeGroupId = groupId;
		
		try {
			writeToFile(filepath);
		
			if (causeEffects)
				GroupMeIMConnection.forceRegisterGroupMeBot();
		} catch (IOException ex) {
			LOGGER.warning("Error writing GroupMeGroupId to Stored Data file");
		}
    }
    
    
    public static void setGroupMeGroupName(String groupName)
    {
    	setGroupMeGroupName(groupName, FILEPATH, true);
    }
	
	public static void setGroupMeGroupName(String groupName, String filepath, boolean causeEffects)
    {
    	groupMeGroupName = groupName;
    	
    	try {
			writeToFile(filepath);
		
			if (causeEffects)
				connection.instantiateIMBot();
		} catch (IOException ex) {
			LOGGER.warning("Error writing GroupMeGroupName to Stored Data file");
		}
    }
	
	
    public static void setGroupMeBotName(String name)
    {
		setGroupMeBotName(name, FILEPATH, true);
    }
    
    public static void setGroupMeBotName(String name, String filepath, boolean causeEffects)
    {
		groupMeBotName = name;
		
		try {
			writeToFile(filepath);
		
			if (causeEffects)
			{
				GroupMeIMConnection.forceRegisterGroupMeBot();
				connection.instantiateIMBot();
			}
		} catch (IOException ex) {
			LOGGER.warning("Error writing GroupMeBotName to Stored Data file");
		}
    }
    
    
    public static void setBotCommandPrefix(String prefix)
    {
		setBotCommandPrefix(prefix, FILEPATH, true);
    }
    
    public static void setBotCommandPrefix(String prefix, String filepath, boolean causeEffects)
    {
		botCommandPrefix = prefix;
		
		try {
			writeToFile(filepath);
		
			if (causeEffects)
				connection.instantiateIMBot();
		} catch (IOException ex) {
			LOGGER.warning("Error writing BotCommandPrefix to Stored Data file");
		}
    }
    
    
    public static void setGroupMeBotId(String id)
    {
		setGroupMeBotId(id, FILEPATH);
    }
    
    public static void setGroupMeBotId(String id, String filepath)
    {
		groupMeBotId = id;
		
		try {
			writeToFile(filepath);
		} catch (IOException ex) {
			LOGGER.warning("Error writing GroupMeBotId to Stored Data file");
		}
    }
    
    
    public static void setLastMessageId(String id)
    {
		setLastMessageId(id, FILEPATH);
    }
	
    public static void setLastMessageId(String id, String filepath)
    {
		lastMessageId = id;
		
		try {
			writeToFile(filepath);
		} catch (IOException ex) {
			LOGGER.warning("Error writing LastMessageId to Stored Data file");
		}
    }
}
