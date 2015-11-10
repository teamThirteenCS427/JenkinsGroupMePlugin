package edu.cs427.groupme;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
/**
 * API Interface which right now only does GET requests.
 * @author espaill2, zavelev2 
 */
public class GroupMeAPIInterface {
	private static final String GROUPME_URL = "https://api.groupme.com/v3";
	private String GROUPME_TOKEN;

	public GroupMeAPIInterface(String token) {
		this.GROUPME_TOKEN = token;
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
	
	//TODO: Write javadoc
	public JSONObject POST_BODY(String endpoint, String body)
	{
		URL myUrl;
		try {
			myUrl = new URL(GROUPME_URL + endpoint + "?token=" + GROUPME_TOKEN);
			HttpsURLConnection conn = (HttpsURLConnection) myUrl.openConnection();
	
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length", String.valueOf(body.length())); 
			conn.setRequestProperty("Content-Type", "application/json");
	
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
				return new JSONObject(res);
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getStackTrace());
			return null;
		}
		return null;
	}
	
	public int POST_PARAMS(String endpoint, String params)
	{
		int responseCode = 0;
		try
		{
			URL url = new URL(GROUPME_URL + endpoint /*+ token=" + GROUPME_TOKEN*/);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);

			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(params);
			wr.flush();
			wr.close();
			connection.disconnect();

			responseCode = connection.getResponseCode();
			if (responseCode != 202)
				System.out.println(responseCode + " error has occured while sending a message");
		} catch (MalformedURLException e)
		{
			System.out.println("Error occured while establishing a connection");
			e.printStackTrace();
		} catch (IOException e)
		{
			System.out.println("Error occured while sending data");
			e.printStackTrace();
		}
		return responseCode;
	}

	/**
	 * Returns APIInterface GroupMe Token set when instantiated
	 * @return GroupMe Token
	 */
	public String getGROUPME_TOKEN() {
		return GROUPME_TOKEN;
	}
	
}