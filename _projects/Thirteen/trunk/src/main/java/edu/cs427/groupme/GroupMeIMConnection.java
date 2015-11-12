package edu.cs427.groupme;

import hudson.plugins.im.AbstractIMConnection;
import hudson.plugins.im.IMChat;
import hudson.plugins.im.IMConnectionListener;
import hudson.plugins.im.IMException;
import hudson.plugins.im.IMMessageTarget;
import hudson.plugins.im.IMPresence;
import hudson.plugins.im.IMPublisherDescriptor;
import hudson.plugins.im.bot.Bot;


/**
 * Implementation of AbsractIMConnection that establishes a connection between
 * GroupMe and the IM plugin
 * 
 * @author blessin2 admathu2
 */
public class GroupMeIMConnection extends AbstractIMConnection 
{
	
	private final GroupMeMessagePolling polling;
	private final Bot bot;
	
	private final IMChat groupMeChat;
	
	// An interface containing many important connection variables
	private IMPublisherDescriptor desc;
	
	public GroupMeIMConnection() {
		registerGroupMeBot();
		this.groupMeChat = new GroupMeChat();
		this.bot = new Bot(this.groupMeChat, 
						   GroupMeStoredData.getGroupMeBotName(), 
						   GroupMeStoredData.getGroupMeGroupName(), 
						   GroupMeStoredData.getBotCommandPrefix(), 
						   null); 
		this.polling = new GroupMeMessagePolling(new GroupMeAPIInterface(GroupMeStoredData.getGroupMeToken()), bot);
	}
	
	public static void registerGroupMeBot()
	{
		String storedId = GroupMeStoredData.getGroupMeBotId();
		if (storedId.equals(""))
		{
			if (GroupMeBot.isUnregistered())
			{
				GroupMeBot.init(GroupMeStoredData.getGroupMeBotName(), 
								GroupMeStoredData.getGroupMeToken(), 
								GroupMeStoredData.getGroupMeGroupId(), 
								new GroupMeBotConnection(GroupMeStoredData.getGroupMeToken()));
				GroupMeBot.register();
			}
		}
		else
		{
			GroupMeBot.botId = storedId;
		}
	}
	
//TODO: implement constructor with descriptor later on.
// IMPublisherDescriptor is an interface while IMPublisher is a class. 
// In GroupMeConnectionProvider we called the GroupMeIMConnection with a IMPublisherDescriptor interface.	
	
//	public GroupMeIMConnection(IMPublisherDescriptor desc) {
//		super(desc);
//		this.groupMeChat = new GroupMeChat();
//		this.desc = desc;
//		this.botCommandPrefix = "!";	
//		this.polling = new GroupMeMessagePolling(new GroupMeAPIInterface(GroupMeStoredData.getGroupMeToken(), GroupMeStoredData.getGroupMeGroupId()), bot);
//	}

	@Override
	public boolean connect() {
		//spawn thread to run polling
		this.polling.init();
		return isConnected();
	}

	public String getName() {
		return GroupMeStoredData.getGroupMeGroupName();
	}

	
	@Override
	public boolean isConnected() {
		return polling.isPolling();
	}

	@Override
	public void close() {
		polling.close();
	}
	
	public void open() {
		polling.init();
	}

	@Override
	public void send(IMMessageTarget target, String text) throws IMException {
		this.groupMeChat.sendMessage(text);
	}

	@Override
	public void setPresence(IMPresence presence, String statusMessage) throws IMException {
		//Not needed due to GroupMe being a REST API
	}

	@Override
	public void addConnectionListener(IMConnectionListener listener) {
		//Not needed due to GroupMe being a REST API
	}

	@Override
	public void removeConnectionListener(IMConnectionListener listener) {
		//Not needed due to GroupMe being a REST API
	}

}
