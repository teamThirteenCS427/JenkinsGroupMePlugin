package edu.cs427.groupme;

import java.util.logging.Logger;

import org.json.JSONException;
import org.json.simple.JSONObject;

import hudson.plugins.im.bot.Bot;
 
/**  
 * This version is a simple implementation of the groupme bot which can send messages.
 * This version needs json library. We didn't modify the pom.xml probably need to fix the dependency issue.
 * @author blessin2 admathu2
 * @edits pzhao12 fricken2
 *
 */ 
public final class GroupMeBot {
	//Base URL used for all GroupMe interactions
	public static String botName;
	public static String getBotName() {
		return botName;
	}

	public static String accessToken;
	public static String groupId;
	public static String botId;
	public static IGroupMeBotConnection connection; 
	
	private static final Logger LOGGER = Logger.getLogger(GroupMeBot.class.getName());

	public static void init(String botName, String accessToken, String groupId, IGroupMeBotConnection connection) 
	{
		GroupMeBot.botName = botName;
		GroupMeBot.accessToken = accessToken;
		GroupMeBot.groupId = groupId;
		GroupMeBot.connection = connection;
		botId = "";
	}
	
	public static void initWithBotId(String botId, String botName, String accessToken, String groupId, IGroupMeBotConnection connection)
	{
		GroupMeBot.connection = connection;
		GroupMeBot.botId = botId;
		GroupMeBot.botName = botName;
		GroupMeBot.accessToken = accessToken;
		GroupMeBot.groupId = groupId;
	}
	
	
	public static boolean register()
	{
		JSONObject obj = connection.register(botName, groupId, accessToken);
		
		if(obj==null)
			return false;
		
		extractBotId(obj);
		GroupMeStoredData.setGroupMeBotId(botId);
		return true;
	}


	public static void extractBotId(JSONObject obj) {
		if (obj == null){
			botId = "";
			return;
		}
		String botIdString = "";
		try
		{
			if (obj.containsKey("response"))
			{
				JSONObject response_obj = (JSONObject) obj.get("response");
				if (response_obj.containsKey("bot"))
				{
					JSONObject bot_obj = (JSONObject) response_obj.get("bot");
					if (bot_obj.containsKey("bot_id"))
						botIdString = (String) bot_obj.get("bot_id");
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("error reading object  \n" + obj.toJSONString());
			botIdString = "";
		}
		botId = botIdString;
	}


	public static boolean sendTextMessage(String message)
	{
		LOGGER.warning("GROUP ME BOT:: TOKEN: " + accessToken + "\n groupId: " + groupId + "\n botName: " + botName + "\n botId: "+ botId);

		int resp = connection.sendMessage(botId, message);
		if (resp == 201){
			return true;
		} else if (resp == 420){
			try {
				Thread.sleep(60000);
				connection.sendMessage(botId, "We need to chill, we are being rate limited");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
			
		return false;
	}

	public static boolean isUnregistered()
	{
		if (botId == null)
			botId = "";
		return botId.equals("");
	}
}
