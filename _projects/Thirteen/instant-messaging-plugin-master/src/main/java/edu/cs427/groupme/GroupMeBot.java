package edu.cs427.groupme;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.json.simple.*;
import java.net.MalformedURLException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
//test For autobuild

/**
 * This version is a simple implementation of the groupme bot which can send messages.
 * This version needs json library. We didn't modify the pom.xml probably need to fix the dependency issue.
 * @author blessin2 admathu2
 * @edits pzhao12 fricken2
 *
 */
public class GroupMeBot {
	//Base URL used for all GroupMe interactions
	private String botName;
	private String accessToken;
	private String groupId;
	private String callbackUrl;
	private String botId;

	public GroupMeBot(String botName,String accessToken, String groupId, String callbackUrl) 
	{
		this.botName = botName;
		this.accessToken = accessToken;
		this.groupId = groupId;
		this.callbackUrl = callbackUrl;
		botId = "";
	}

	public void register()
	{
		String body = "{ \"bot\" : " + "{ \"name\" : \""+botName+"\", \"group_id\" : \""+groupId+"\", \"callback_url\" : \""+callbackUrl+"\" } }";
		String GROUPME_URL = "https://api.groupme.com/v3/bots?token=";
		try 
		{
			URL url = new URL(GROUPME_URL + accessToken);
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
				String res = response.toString();
				JSONObject obj = new JSONObject(res);
				String botIdString = obj.getJSONObject("response").getJSONObject("bot").getString("bot_id");
				this.botId = botIdString;
//				System.out.println("Response: " + res);
//				System.out.println(botIdString);
			}
			else
			{
				System.out.println("Response Code: " + responseCode);
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}


	public void sendTextMessage(String message)
	{
		String urlParameters = "bot_id=" + this.botId + "&text=" + message + "&param3=c";
		String REQUEST_URL = "https://api.groupme.com/v3/bots/post";
		try
		{
			URL url = new URL(REQUEST_URL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);

			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			connection.disconnect();

			int responseCode = connection.getResponseCode();
			if (responseCode != 202)
				System.out.println(responseCode + " error has occured while sending the message: " + message);
		} catch (MalformedURLException e)
		{
			System.out.println("Error occured while establishing a connection");
			e.printStackTrace();
		} catch (IOException e)
		{
			System.out.println("Error occured while sending data");
			e.printStackTrace();
		}
	}

//	public void sendImage(String text, String imageURL)
//	{
//		try
//		{
//			String urlParameters = "{\"bot_id\":\"" + botId + "\",\"text\":\"" + text
//					+ "\",\"attachments\":[{\"type\":\"image\",\"url\":\"" + imageURL + "\"}]}";
//			String request = "https://api.groupme.com/v3/bots/post";
//			URL url = new URL(request);
//			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//			connection.setDoOutput(true);
//			connection.setDoInput(true);
//			connection.setInstanceFollowRedirects(false);
//			connection.setRequestMethod("POST");
//			connection.setUseCaches(false);
//
//			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
//			wr.writeBytes(urlParameters);
//			wr.flush();
//			wr.close();
//			connection.disconnect();
//
//			int responseCode = connection.getResponseCode();
//			if (responseCode != 202)
//				System.out.println(responseCode + " error has occured while sending the image: " + imageURL);
//		} catch (MalformedURLException e)
//		{
//			System.out.println("Error occured while establishing a connection");
//			e.printStackTrace();
//		} catch (IOException e)
//		{
//			System.out.println("Error occured while sending data");
//			e.printStackTrace();
//		}
//	}
		
//	public void sendLocation(String text, String locationName, double longitude, double latitude)
//	{
//		try
//		{
//			String urlParameters = "{\"bot_id\":\"" + botId + "\",\"text\":\"" + text +"\",\"attachments\":[{\"type\":\"location\",\"lng\":\"" 
//					+ longitude +"\",\"lat\":\"" + latitude + "\",\"name\":\"" + locationName +"\"}]}";
//			String request = "https://api.groupme.com/v3/bots/post";
//			URL url = new URL(request);
//			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//			connection.setDoOutput(true);
//			connection.setDoInput(true);
//			connection.setInstanceFollowRedirects(false);
//			connection.setRequestMethod("POST");
//			connection.setUseCaches(false);
//
//			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
//			wr.writeBytes(urlParameters);
//			wr.flush();
//			wr.close();
//			connection.disconnect();
//
//			int responseCode = connection.getResponseCode();
//			if (responseCode != 202)
//				System.out.println(responseCode + " error has occured while sending the location: " + latitude + " " + longitude);
//		} catch (MalformedURLException e)
//		{
//			System.out.println("Error occured while establishing a connection");
//			e.printStackTrace();
//		} catch (IOException e)
//		{
//			System.out.println("Error occured while sending data");
//			e.printStackTrace();
//		}
//	}
	/*
		public static void main(String args[]){
			GroupmeBot bot = new GroupmeBot("test_bot_pzhao12","40bL6d4xsBRLt0b3zBrbiXr6v6Fp46Snuu6ybZro","17407658","");
			bot.register();
			//bot.sendTextMessage("local test with bot id "+bot.botId);
			//bot.sendImage("test with bot id"+bot.botId, "");
			//bot.sendLocation("local test with bot id "+ bot.botId, "grainger",  -88.22,40.11);
		}
	 */
}
