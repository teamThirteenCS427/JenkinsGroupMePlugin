package edu.cs427.groupme;

import java.util.HashSet;
import java.util.Set;

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
	
	private final GroupMeMessagePolling polling;
	private final Bot bot;
	private final IMChat groupMeChat;
	private final String botCommandPrefix;
	
	// An interface containing many important connection variables
	private IMPublisherDescriptor desc;
	
	public GroupMeIMConnection() {
		this.groupMeChat = new GroupMeChat();
		this.botCommandPrefix = "!";
		this.bot = new Bot(this.groupMeChat, "JenkinsBot", "ThirteenGroup", this.botCommandPrefix, null);
		this.polling = new GroupMeMessagePolling(new GroupMeAPIInterface(GROUPME_TOKEN, GROUPME_GROUP_ID), bot);
		
		//TODO: Move this somewhere more appropriate
		//TODO: Attempt to load bot_id from XML file and skip init and register
		GroupMeBot.init("JenkinsBot", GROUPME_TOKEN, GROUPME_GROUP_ID);
		GroupMeBot.register();
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
		return false;
	}

	public String getName() {
		// TODO Get a name: GROUPME_GROUP_ID is a placeholder for now.
		return GROUPME_GROUP_ID;
	}

	
	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void close() {
		polling.close();

	}

	@Override
	public void send(IMMessageTarget target, String text) throws IMException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPresence(IMPresence presence, String statusMessage) throws IMException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addConnectionListener(IMConnectionListener listener) {
		// TODO Auto-generated method stub
		//I don't think we need this

	}

	@Override
	public void removeConnectionListener(IMConnectionListener listener) {
		// TODO Auto-generated method stub
		//I don't think we need this

	}

}
