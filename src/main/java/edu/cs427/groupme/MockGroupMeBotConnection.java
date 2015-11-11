package edu.cs427.groupme;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MockGroupMeBotConnection implements IGroupMeBotConnection {

	public static String TEST_BOT_NAME = "TestBot";
	public static String TEST_GROUP_ID = "id";
	public static String TEST_VALID_TOKEN = "valid";
	public static String TEST_INVALID_TOKEN = "invalid";
	public static String TEST_VALID_BOTID = "vBot";
	public static String TEST_INVALID_BOTID = "iBot";
	
	@Override
	public JSONObject register(String botName, String groupId, String accessToken) {
		if (accessToken.equals(TEST_VALID_TOKEN))
		{
			try {
				return (JSONObject) new JSONParser().parse("{\"response\":{\"bot\":{\"bot_id\":\""+TEST_VALID_BOTID+"\"}}}");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public int sendMessage(String botId, String message) {
		if (botId.equals(""))
			return 0;
		if (botId.equals(TEST_VALID_BOTID))
			return 202;
		return 500;
	}

}
