package edu.cs427.groupme;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public final class GroupMeStoredData 
{
	private static final String FILEPATH = "data/groupMeData.json";
	
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
    
    //Instance of the GroupMeIMConnection
    private static GroupMeIMConnection connection = null;
    
    
    
    public static void setIMConnection(GroupMeIMConnection conn)
    {
    	connection = conn;
    }
    
    public static void init()
    {
    	if (!dataFileExists())
    		createDataFile();
    	readAllData();
    }
    
    //Determines whether the file at FILEPATH exists
    private static boolean dataFileExists()
    {
    	File file = new File(FILEPATH);
		return file.exists();
    }
    
    //Creates a default data file at FILEPATH
    private static void createDataFile()
    {
    	JSONObject obj = new JSONObject();
    	JSONObject settings = new JSONObject();
    	JSONObject data = new JSONObject();
    	settings.put(GROUPME_TOKEN_KEY, "8fyym11XsTj5XrHTHzNChDDHia0LAM4afuflybhg");
    	settings.put(GROUPME_GROUP_ID_KEY, "17407658");
    	settings.put(GROUPME_GROUP_NAME_KEY, "JenkinsGroup");
    	settings.put(GROUPME_BOT_NAME_KEY, "JenkinsBot");
    	settings.put(BOT_COMMAND_PREFIX_KEY, "!");
    	data.put(GROUPME_BOT_ID_KEY, "");
    	obj.put("Settings", settings);
    	obj.put("Data", data);
    	try (FileWriter file1 = new FileWriter(FILEPATH)) {
			file1.write(obj.toJSONString());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    //Reads the file at FILEPATH and sets all variables
    private static void readAllData()
    {
    	JSONParser parser = new JSONParser();
		
		try {
			 
	        Object obj = parser.parse(new FileReader(FILEPATH));
	
	        JSONObject jsonObject = (JSONObject) obj;
	        JSONObject settings = (JSONObject) jsonObject.get("Settings");
	        JSONObject data = (JSONObject) jsonObject.get("Data");
	
	        groupMeToken = (String) settings.get(GROUPME_TOKEN_KEY);
	        groupMeGroupId = (String) settings.get(GROUPME_GROUP_ID_KEY);
	        groupMeGroupName = (String) settings.get(GROUPME_GROUP_NAME_KEY);
	        groupMeBotName = (String) settings.get(GROUPME_BOT_NAME_KEY);
	        botCommandPrefix = (String) settings.get(BOT_COMMAND_PREFIX_KEY);
	        groupMeBotId = (String) data.get(GROUPME_BOT_ID_KEY);
		}
	    catch (Exception e) {
	        e.printStackTrace();
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
    
    
    //TODO: Create set methods that both write to file and cause appropriate modifications
}
