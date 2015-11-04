package edu.cs427.groupme;

import org.json.JSONObject;

public class MockGroupMeBotConnection implements IGroupMeBotConnection {

	public static String TEST_BOT_NAME = "TestBot";
	public static String TEST_GROUP_ID = "id";
	public static String TEST_VALID_TOKEN = "valid";
	public static String TEST_INVALID_TOKEN = "invalid";
	
	@Override
	public JSONObject register(String botName, String groupId, String accessToken) {
		if (accessToken.equals(TEST_VALID_TOKEN))
		{
			return new JSONObject();
		}
		return null;
	}

}
