package edu.cs427.groupme;

import org.json.simple.JSONObject;

public class GroupMeBotConnection {

	private GroupMeAPIInterface groupmeApi;

	public GroupMeBotConnection(String token) {
		groupmeApi = new GroupMeAPIInterface(token);
	}

	/**
	 * Register the bot by sending a message to the GroupMe servers
	 * @param botName The name of the bot to be registered
	 * @param groupId The id of the group this bot will be joined to
	 * @param accessToken The access token used by the creator of the group specified by groupId
	 * @return The server's response as an org.json.simple.JSONObject
	 */
	public JSONObject register(String botName, String groupId, String accessToken) {
		String body = "{ \"bot\" : " + "{ \"name\" : \"" + botName + "\", \"group_id\" : \"" + groupId + "\" } }";
		return groupmeApi.POST_BODY("/bots", body);
	}

	/**
	 * Send message to the GroupMe chat
	 * 
	 * @param botId
	 *            bot ID used
	 * @param message
	 *            message to send to the chat
	 * @return HTTP response code
	 */
	public int sendMessage(String botId, String message) {
		if (botId.equals(""))
			return 0;

		String urlParameters = "bot_id=" + botId + "&text=" + message + "&param3=c";
		return groupmeApi.POST_PARAMS("/bots/post", urlParameters);
	}
}
