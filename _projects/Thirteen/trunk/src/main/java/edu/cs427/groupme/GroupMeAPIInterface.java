package edu.cs427.groupme;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
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
	/**
	 * Separated the two mostly for unit testing purposes. This one takes an already formed URL to do the GET request.
	 * @param url - formed url object onto which the REST connection will be established
	 * @return jsonObject - returns the parsed response in a JSONObject
	 */
	public JSONObject GET(URL url)
	{
		JSONObject json = null;
		try 
		{
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
	
	/**
	 * This one you can send in the endpoint and url params and the get request gets made.
	 * @param endpoint - in other words the path to the specific endpoint in the group me service
	 * @param param - different get params to append to url
	 * @return returns JSONObject of response
	 */
	public JSONObject GET(String endpoint, String param)
	{
		JSONObject json = null;
		try {
			json = GET(new URL(GROUPME_URL + endpoint + "?token=" + GROUPME_TOKEN + param));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} finally {
			return json;
		}
	}

	public String getGROUPME_TOKEN() {
		return GROUPME_TOKEN;
	}

	public String getGROUPME_ID() {
		return GROUPME_ID;
	}	
	
}