package edu.cs427.groupme;

import java.util.logging.Logger;

import antlr.StringUtils;
import hudson.plugins.im.IMChat;
import hudson.plugins.im.IMException;
import hudson.plugins.im.IMMessageListener;
import hudson.plugins.im.bot.Bot;

public class GroupMeChat implements IMChat {
	private static final Logger LOGGER = Logger.getLogger(GroupMeChat.class.getName());

	public GroupMeChat()
	{
		
	}
	
	@Override
	public void sendMessage(String message) throws IMException {
		GroupMeBot.sendTextMessage(message);
	}

	@Override
	public String getNickName(String senderId) {
		return GroupMeBot.getBotName();
	}

	@Override
	public String getIMId(String senderId) {
		// TODO Auto-generated method stub
        return null;
	}

	@Override
	public boolean isMultiUserChat() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCommandsAccepted() {
		return true;
	}

	@Override
	public void addMessageListener(IMMessageListener listener) {
		// polling already does onMessage, after new messages are parsed.

	}

	@Override
	public void removeMessageListener(IMMessageListener listener) {
		// polling already does onMessage, after new messages are parsed.
	}

}
