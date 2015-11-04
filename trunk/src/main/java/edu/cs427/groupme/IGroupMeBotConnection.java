package edu.cs427.groupme;

import org.json.JSONObject;

public interface IGroupMeBotConnection {

	JSONObject register(String botName, String groupId, String accessToken);

	int sendMessage(String botId, String message);
}