package edu.cs427.groupme;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;

public class GroupMeAPIInterface {
	private static final String GROUPME_URL = "https://api.groupme.com/v3";
	private String GROUPME_TOKEN;
	private String GROUPME_ID;

	public GroupMeAPIInterface(String token, String id) {
		this.GROUPME_TOKEN = token;
		this.GROUPME_ID = id;
	}
	
	public JSONObject GET(String endpoint, String param)
	{
		JSONObject json = null;
		try 
		{
			URL url = new URL(GROUPME_URL + endpoint + "?token=" + GROUPME_TOKEN + param);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			
			conn.setRequestMethod("GET");
			int responseCode = conn.getResponseCode();

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			json = (JSONObject) (new JSONParser().parse(response.toString()));

		} 
		catch (IOException | ParseException e) 
		{
			e.printStackTrace();
		}
		
		return json;
	}

	public String getGROUPME_TOKEN() {
		return GROUPME_TOKEN;
	}

	public String getGROUPME_ID() {
		return GROUPME_ID;
	}	
	
}