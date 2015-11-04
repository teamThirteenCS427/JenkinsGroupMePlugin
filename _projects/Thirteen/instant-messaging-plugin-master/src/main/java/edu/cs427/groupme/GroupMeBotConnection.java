package edu.cs427.groupme;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class GroupMeBotConnection {

	
	public static JSONObject register(String botName, String groupId, String accessToken)
	{
		String body = "{ \"bot\" : " + "{ \"name\" : \""+botName+"\", \"group_id\" : \""+groupId+"\" } }";
		String GROUPME_URL = "https://api.groupme.com/v3/bots?token=";
		int responseCode = 0;
		try 
		{
			URL url = new URL(GROUPME_URL + accessToken);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length", String.valueOf(body.length())); 
			conn.setRequestProperty("Content-Type", "application/json");

			conn.setDoOutput(true); 
			conn.setDoInput(true); 
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.writeBytes(body);
			wr.flush();
			wr.close();

			responseCode = conn.getResponseCode();
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
				return new JSONObject(res);
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
}
