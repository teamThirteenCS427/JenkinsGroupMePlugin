package edu.cs427.groupme;

import org.json.simple.JSONObject;

public interface IGroupMeBotConnection {

	JSONObject register(String botName, String groupId, String accessToken);

	int sendMessage(String botId, String message);
}