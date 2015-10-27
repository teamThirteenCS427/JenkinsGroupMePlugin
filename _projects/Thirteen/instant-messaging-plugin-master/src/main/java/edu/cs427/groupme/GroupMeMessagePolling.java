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

public class GroupMeMessagePolling {
	private GroupMeAPIInterface api;
	private boolean cont;
	private String lastMessageID;
	private Bot bot;
		
	//TODO: figure out where the bot is created and pass in the bot to the polling mechanism
	public GroupMeMessagePolling(GroupMeAPIInterface api) {
		this.api = api;
		this.bot = null;
		lastMessageID = null;
	}
	
	public void init(){
		cont = true;
		while(cont)
		{
			String param = "";
			if(lastMessageID  != null)
				param = "?after_id=" +lastMessageID;
			JSONObject response = api.GET("/groups/" + api.getGROUPME_ID() + "/messages", param);
			System.out.println(response.toString());
			JSONObject meta = ((JSONObject) response.get("meta"));
			int responseCode = (int) meta.get("code");
			if(response != null && responseCode == 200)
				parseResponse((JSONObject)response.get("response"));
			if(responseCode != 304)
				continue;
			try {
			    Thread.sleep(300000);                 
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
		}
	}
	/**
	 * TODO: figure out what to put in the TO field. Should we send name or user id for the FROM field.
	 * @param response
	 * @param messages
	 * @return
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
	public void close(){
		cont = false;
	}
	

}
