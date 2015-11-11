package edu.cs427.groupme;

import org.json.simple.JSONObject;

public class GroupMeBotConnection implements IGroupMeBotConnection {

	private GroupMeAPIInterface groupmeApi;
	
	public GroupMeBotConnection(String token)
	{
		groupmeApi = new GroupMeAPIInterface(token);
	}
	
	
	/* (non-Javadoc)
	 * @see edu.cs427.groupme.IGroupMeBotConnection#register(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject register(String botName, String groupId, String accessToken)
	{
		String body = "{ \"bot\" : " + "{ \"name\" : \""+botName+"\", \"group_id\" : \""+groupId+"\" } }";
		return groupmeApi.POST_BODY("/bots", body);
	}
	
	@Override
	public int sendMessage(String botId, String message)
	{
		if (botId.equals(""))
			return 0;
		
		String urlParameters = "bot_id=" + botId + "&text=" + message + "&param3=c";
		return groupmeApi.POST_PARAMS("/bots/post", urlParameters);
	}
}
