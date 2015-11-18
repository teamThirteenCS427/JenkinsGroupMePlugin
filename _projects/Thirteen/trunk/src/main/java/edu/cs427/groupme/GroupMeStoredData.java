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
		if(!dataFileExists())
    		writeToFile();
    	readAllData();
    }
    
    //Determines whether the file at FILEPATH exists
    public static boolean dataFileExists()
    {
    	File file = new File(FILEPATH);
    	boolean exists = file.exists();
    	LOGGER.info("File at " + FILEPATH + (exists ? "exists" : "does not exist"));
		return exists;
    }
    
    //Writes all data to the data file
    public static void writeToFile()
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
    	try (FileWriter file = new FileWriter(FILEPATH)) {
    		LOGGER.info("Writing to storedData file: " + obj.toJSONString());
			file.write(obj.toJSONString());
			file.flush();
			file.close();
		} catch (IOException e) {
			LOGGER.warning("IOException occureed while writing to file");
			e.printStackTrace();
		} catch (Exception e) {
			LOGGER.warning("Exception occured while writing to file");
		}
    }
    
    //Reads the file at FILEPATH and sets all variables
    private static void readAllData()
    {
    	JSONParser parser = new JSONParser();
		
		try {
			 
			LOGGER.info("Attempting to read from StoredData file");
			 
	        Object obj = parser.parse(new FileReader(FILEPATH));
	
	        JSONObject jsonObject = (JSONObject) obj;
	        
	        LOGGER.info("Parsing data from " + jsonObject.toJSONString());
	        
	        JSONObject settings = (JSONObject) jsonObject.get("Settings");
	        JSONObject data = (JSONObject) jsonObject.get("Data");
	
	        groupMeToken = (String) settings.get(GROUPME_TOKEN_KEY);
	        groupMeGroupId = (String) settings.get(GROUPME_GROUP_ID_KEY);
	        groupMeGroupName = (String) settings.get(GROUPME_GROUP_NAME_KEY);
	        groupMeBotName = (String) settings.get(GROUPME_BOT_NAME_KEY);
	        botCommandPrefix = (String) settings.get(BOT_COMMAND_PREFIX_KEY);
	        groupMeBotId = (String) data.get(GROUPME_BOT_ID_KEY);
	        lastMessageId = (String) data.get(LAST_MESSAGE_ID_KEY);
	        
	        LOGGER.info("Data read from file successfully");
		} catch (FileNotFoundException ex) {
            LOGGER.warning("FileNotFoundException while reading data file");
        } catch (IOException ex) {
            LOGGER.warning("IOException while reading data file");
        } catch (NullPointerException ex) {
            LOGGER.warning("NullPointerException while reading data file");
        } catch (Exception ex) {
        	LOGGER.warning("Exception while reading data file");
        }

    }
    
	
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
		groupMeToken = token;
		writeToFile();
		GroupMeIMConnection.forceRegisterGroupMeBot();
		connection.startPolling();
    }
    
    public static void setGroupMeGroupId(String groupId)
    {
		groupMeGroupId = groupId;
		writeToFile();
		GroupMeIMConnection.forceRegisterGroupMeBot();
    }
    
    public static void setGroupMeGroupName(String groupName)
    {
    	groupMeGroupName = groupName;
		writeToFile();
		connection.instantiateIMBot();
    }
	
    public static void setGroupMeBotName(String name)
    {
		groupMeBotName = name;
		writeToFile();
		GroupMeIMConnection.forceRegisterGroupMeBot();
		connection.instantiateIMBot();
    }
    
    public static void setBotCommandPrefix(String prefix)
    {
		botCommandPrefix = prefix;
		writeToFile();
		connection.instantiateIMBot();
    }
    
    public static void setGroupMeBotId(String id)
    {
		groupMeBotId = id;
		writeToFile();
    }
	
    public static void setLastMessageId(String id)
    {
		lastMessageId = id;
		writeToFile();
    }
}
