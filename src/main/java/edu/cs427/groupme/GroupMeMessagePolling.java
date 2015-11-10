package edu.cs427.groupme;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import hudson.plugins.im.IMMessage;
import hudson.plugins.im.bot.Bot;

/**
 * Polls for new messages from the GroupMe group, and parses it into
 * IMMessage objects which are sent to the bot.
 * @author espaill2, zavelev2 
 */
public class GroupMeMessagePolling {
	private GroupMeAPIInterface api;
	private volatile boolean cont;
	private String lastMessageID;
	private Bot bot;
		
	/**
	 * TODO: figure out where the bot is created and pass in the bot to the polling mechanism
	 * Constructor for GrouoMeMessagePolling class
	 * @param api  An instance of the groupme API to make calls to
	 * @param bot  And instance of the IM bot to send commands to
	 */
	public GroupMeMessagePolling(GroupMeAPIInterface api, Bot bot) {
		this.api = api;
		this.bot = bot;
		this.lastMessageID = null;
	}
	
	/**
	 * get the latest set of messages (since the last polling call)
	 * parse the messages received
	 */
	public void poll() {
		//unspecified gets all messages
		String afterIDParam = "";
		if(lastMessageID  != null)
			afterIDParam = "?after_id=" +lastMessageID;
		JSONObject response = api.GET("/groups/" + api.getGROUPME_ID() + "/messages", afterIDParam);
		//checking the response, parsing if correct
		JSONObject meta = ((JSONObject) response.get("meta"));
		long responseCode = (long) meta.get("code");
		GroupMeBot.sendTextMessage("Just Polled, and received a response: " + responseCode);
		if(response != null && responseCode == 200)
			parseResponse((JSONObject)response.get("response"));
		else if(responseCode == 304 && cont){
			try {
			    Thread.sleep(60000);                 
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
		}
	}
	
	
	
	/**
	 * TODO: figure out what to put in the TO field. Should we send name or user id for the FROM field.
	 * parse a single message out of the JSONObject and send it to the bot
	 * @param response  The JSON response from the get call. Want the messages from it.
	 */
	private void parseResponse(JSONObject response) {
		JSONArray msgs = (JSONArray) response.get("messages");
		for(int i = 0; i < msgs.size(); i++){
			JSONObject obj = (JSONObject) msgs.get(i);
			String text = (String) obj.get("text");
			String from = (String) obj.get("name");
			String to = "FIX LATER";
			IMMessage message = new IMMessage(from, to, text, true);
			bot.onMessage(message);
			lastMessageID = (String) obj.get("id");
		}
	}
	
	/**
	 * Create a thread that runs the polling function continuously until close() is called
	 */
	public void init(){
		cont = true;
		Runnable r = new Runnable(){
			public void run(){

				while(cont)
				{
					poll();
				}
			}
		};
		new Thread(r).start();
	}
	
	/**
	 * stop the poll command from running. Ends the thread.
	 */
	public void close(){
		cont = false;
		GroupMeBot.sendTextMessage("For Some Reason polling is being closed...");
	}
}
