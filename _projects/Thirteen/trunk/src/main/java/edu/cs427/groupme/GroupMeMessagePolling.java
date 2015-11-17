package edu.cs427.groupme;

import java.util.ArrayList;

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
			afterIDParam = "&after_id=" +lastMessageID;
		JSONObject response = api.GET("/groups/" + GroupMeBot.groupId + "/messages", afterIDParam);
		//checking the response, parsing if correct
		JSONObject meta = ((JSONObject) response.get("meta"));
		long responseCode = (long) meta.get("code");
		JSONObject responseObject = (JSONObject)response.get("response");
		int messageArraySize = ((JSONArray) responseObject.get("messages")).size();
		if(response != null && responseCode == 200 && messageArraySize > 0)
			parseResponse(responseObject);
		else if(messageArraySize == 0 && cont){
			try {
			    Thread.sleep(20000);  
 
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
			if(from != "JenkinsBot")
				bot.onMessage(message);
			lastMessageID = (String) obj.get("id");
		}
	}
	
	/**
	 * Makes a new list given the input list with all duplicate IMMessages (where the body is equal) removed
	 * @param imMessages  List of IMMessage commands read in from GroupMe
	 * @return ArrayList of IMMessages in the original ArrayList with all duplicates removed
	 */
	private ArrayList<IMMessage> removeDuplicates(ArrayList<IMMessage> imMessages) {
		ArrayList<IMMessage> imMessagesDuplicatesRemoved = new ArrayList<>();
		for(int i = 0; i < imMessages.size(); i++) {
			boolean isDuplicateFound = false;
			for(int j = i+1; j < imMessages.size(); j++) {
				IMMessage message1 = imMessages.get(i);
				IMMessage message2 = imMessages.get(j);
				if(message1.getBody().equals(message2.getBody())) {
					isDuplicateFound = true;
					break;
				}
			}
			if(!isDuplicateFound) {
				imMessagesDuplicatesRemoved.add(imMessages.get(i));
			}
		}
		return imMessagesDuplicatesRemoved;
	}
	
	/**
	 * Create a thread that runs the polling function continuously until close() is called
	 */
	public void init(){
		GroupMeBot.sendTextMessage("Polling Started");
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

	public boolean isPolling()
	{
		return cont;
	}	
}
