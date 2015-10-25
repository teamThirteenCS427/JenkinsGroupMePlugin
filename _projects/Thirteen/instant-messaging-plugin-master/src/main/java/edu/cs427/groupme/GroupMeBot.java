package edu.cs427.groupme;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * This bot initializes the connection between our plugin and GroupMe
 * Call register() to register the bot with GroupMe
 * Once registered, bot can be used to send messages to the specified group
 * 
 * @author blessin2 admathu2
 *
 */
public class GroupMeBot 
{
	//Base URL used for all GroupMe interactions
	private static final String GROUPME_URL = "https://api.groupme.com/v3";
	private static final String BOT_NAME = "JenkinsBot";
	
	private String accessToken;
	private String groupId;
	private String callbackUrl;
	private String botId;

	public GroupMeBot(String accessToken, String groupId, String callbackUrl) 
	{
		this.accessToken = accessToken;
		this.groupId = groupId;
		this.callbackUrl = callbackUrl;
		botId = "";
	}
	
	public boolean register()
	{
		//TODO: This method is sending invalid request formats, need to fix
		//See: https://dev.groupme.com/tutorials/bots
		String body = "{ \"bot\" : " + "{ \"name\" : \""+BOT_NAME+"\", \"group_id\" : \""+groupId+"\", \"callback_url\" : \""+callbackUrl+"\" } }";
		System.out.println(body);
		
		try 
		{
			URL url = new URL(GROUPME_URL + "/bots?token=" + accessToken);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length", String.valueOf(body.length())); 
			conn.setRequestProperty("Content-Type","application/json");
			
			conn.setDoOutput(true); 
			conn.setDoInput(true); 
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.writeBytes(body);
			wr.flush();
			wr.close();
			
			int responseCode = conn.getResponseCode();
			if (responseCode == 201)
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				
				String inputLine;
				StringBuffer response = new StringBuffer();
	
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				
				System.out.println("Response: " + response);
			}
			else
			{
				System.out.println("Response Code: " + responseCode);
				return false;
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		
		
		
		return true;
	}

}
