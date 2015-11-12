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
	//Developer Token needed to interact with GroupMe API
	private static final String GROUPME_TOKEN = "8fyym11XsTj5XrHTHzNChDDHia0LAM4afuflybhg";
	//ID of our GroupMe Group (TODO: Replace with a user-set parameter)
	private static final String GROUPME_GROUP_ID = "17407658";
	private static final String GROUPME_GROUP_NAME = "ThirteenGroup";
	private static final String GROUPME_BOT_NAME = "JenkinsBot";
	private static final String BOT_COMMAND_PREFIX = "!";
	
	private final GroupMeMessagePolling polling;
	private final Bot bot;
	
	private final IMChat groupMeChat;
	
	// An interface containing many important connection variables
	private IMPublisherDescriptor desc;
	
	public GroupMeIMConnection() {
		registerGroupMeBot();
		this.groupMeChat = new GroupMeChat();
		this.bot = new Bot(this.groupMeChat, GROUPME_BOT_NAME, GROUPME_GROUP_NAME, BOT_COMMAND_PREFIX, null); 
		this.polling = new GroupMeMessagePolling(new GroupMeAPIInterface(GROUPME_TOKEN), bot);
	}
	
	public static void registerGroupMeBot()
	{
		//TODO: Move this somewhere more appropriate
		//TODO: Attempt to load bot_id from XML file and skip init and register
		if (GroupMeBot.isUnregistered())
		{
			GroupMeBot.init(GROUPME_BOT_NAME, GROUPME_TOKEN, GROUPME_GROUP_ID, new GroupMeBotConnection(GROUPME_TOKEN));
			GroupMeBot.register();
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
//		this.polling = new GroupMeMessagePolling(new GroupMeAPIInterface(GROUPME_TOKEN, GROUPME_GROUP_ID), bot);
//	}

	@Override
	public boolean connect() {
		//spawn thread to run polling
		this.polling.init();
		return true;
	}

	public String getName() {
		return GROUPME_GROUP_NAME;
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
