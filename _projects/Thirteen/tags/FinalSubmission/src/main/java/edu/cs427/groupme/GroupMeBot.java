package edu.cs427.groupme;

import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 * This version is a simple implementation of the groupme bot which can send
 * messages. This version needs json library. We didn't modify the pom.xml
 * probably need to fix the dependency issue.
 * 
 * @author blessin2 admathu2
 * @edits pzhao12 fricken2
 *
 */
public final class GroupMeBot {

	public static String botName;
	public static String accessToken;
	public static String groupId;
	public static String botId;
	public static GroupMeBotConnection connection;

	private static final Logger LOGGER = Logger.getLogger(GroupMeBot.class.getName());

	/**
	 * getter of the bot name
	 * 
	 * @return name of the bot
	 */
	public static String getBotName() {
		return botName;
	}

	/**
	 * initialize the GroupMeBot without botID
	 * 
	 * @param botName
	 *            name of the bot
	 * @param accessToken
	 *            accessToken for GroupMe
	 * @param groupId
	 *            GroupMe group ID
	 * @param connection
	 *            GroupMeBotConnection needed for communication
	 */
	public static void init(String botName, String accessToken, String groupId, GroupMeBotConnection connection) {
		GroupMeBot.botName = botName;
		GroupMeBot.accessToken = accessToken;
		GroupMeBot.groupId = groupId;
		GroupMeBot.connection = connection;
		botId = "";
	}

	/**
	 * initialize the GroupMeBot with botID
	 * 
	 * @param botId
	 *            GroupMe Bot Id
	 * @param botName
	 *            name of the bot
	 * @param accessToken
	 *            accessToken for GroupMe
	 * @param groupId
	 *            GroupMe group ID
	 * @param connection
	 *            GroupMeBotConnection needed for communication
	 */
	public static void initWithBotId(String botId, String botName, String accessToken, String groupId, GroupMeBotConnection connection) {
		GroupMeBot.connection = connection;
		GroupMeBot.botId = botId;
		GroupMeBot.botName = botName;
		GroupMeBot.accessToken = accessToken;
		GroupMeBot.groupId = groupId;
	}

	/**
	 * register the GroupMe Bot
	 * 
	 * @return true if bot regiestered successfully, false if the returned JSON
	 *         by the connection is null
	 */
	public static boolean register() {
		JSONObject obj = connection.register(botName, groupId, accessToken);

		if (obj == null)
		{
			LOGGER.warning("GroupMeBot.register response: null");
			return false;
		}

		LOGGER.warning("GroupMeBot.register response: " + obj.toJSONString());
		
		extractBotId(obj);
		GroupMeStoredData.setGroupMeBotId(botId);
		return true;
	}

	/**
	 * extract bot Id from the JSONObject returned by GroupMe
	 * 
	 * @param obj
	 *            JSONobject returned by the GroupeMe
	 */
	private static void extractBotId(JSONObject obj) {
		if (obj == null) {
			botId = "";
			return;
		}
		String botIdString = "";
		try {
			if (obj.containsKey("response")) {
				JSONObject response_obj = (JSONObject) obj.get("response");
				if (response_obj.containsKey("bot")) {
					JSONObject bot_obj = (JSONObject) response_obj.get("bot");
					if (bot_obj.containsKey("bot_id"))
						botIdString = (String) bot_obj.get("bot_id");
				}
			}
		} catch (Exception e) {
			System.out.println("error reading object  \n" + obj.toJSONString());
			botIdString = "";
		}
		botId = botIdString;
	}

	/**
	 * send text message to the GroupMe group
	 * 
	 * @param message
	 *            message to be sent
	 * @return true if succeeded, false if not
	 */
	public static boolean sendTextMessage(String message) {
		LOGGER.warning("GROUP ME BOT:: TOKEN: " + accessToken + "\n groupId: " + groupId + "\n botName: " + botName
				+ "\n botId: " + botId);

		int resp = connection.sendMessage(botId, message);
		if (resp == 201) {
			return true;
		} else if (resp == 420) {
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

	/**
	 * check if the GroupeMeBot is not registered on the GroupMe
	 * 
	 * @return true if the bot is not registered, false if alreadly registered
	 */
	public static boolean isUnregistered() {
		if (botId == null)
			botId = "";
		return botId.equals("");
	}
}
