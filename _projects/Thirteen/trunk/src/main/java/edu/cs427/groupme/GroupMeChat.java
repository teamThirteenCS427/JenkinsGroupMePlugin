package edu.cs427.groupme;

import java.util.logging.Logger;

import antlr.StringUtils;
import hudson.plugins.im.IMChat;
import hudson.plugins.im.IMException;
import hudson.plugins.im.IMMessageListener;
import hudson.plugins.im.bot.Bot;

public class GroupMeChat implements IMChat {
	private static final Logger LOGGER = Logger.getLogger(GroupMeChat.class.getName());

	/**
	 * Empty Constructor
	 */
	public GroupMeChat()
	{
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendMessage(String message) throws IMException {
		GroupMeBot.sendTextMessage(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getNickName(String senderId) {
		return GroupMeBot.getBotName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getIMId(String senderId) {
		// TODO Auto-generated method stub
        return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMultiUserChat() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCommandsAccepted() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addMessageListener(IMMessageListener listener) {
		// polling already does onMessage, after new messages are parsed.

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeMessageListener(IMMessageListener listener) {
		// polling already does onMessage, after new messages are parsed.
	}

}
