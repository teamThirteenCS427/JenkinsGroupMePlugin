package edu.cs427.groupme;

import hudson.plugins.im.AbstractIMConnection;
import hudson.plugins.im.AuthenticationHolder;
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
 * @author blessin2 admathu2 espaill2 zavelev2
 */
public class GroupMeIMConnection extends AbstractIMConnection 
{
	
	private GroupMeMessagePolling polling = null;
	private Bot bot;
	private final AuthenticationHolder authenticationHolder;
	private IMChat groupMeChat;
	
	// An interface containing many important connection variables
	private IMPublisherDescriptor desc;
	
	/**
	 * Constructor to instantiate GroupMe Connection
	 * @param authenticationHolder  The Authentication of the GroupMeConnection
	 */
	public GroupMeIMConnection(AuthenticationHolder authenticationHolder) {
	    this.authenticationHolder = authenticationHolder;
		registerGroupMeBot();
		instantiateIMBot();
		startPolling();
	}
	
	/**
	 * Sets up the class IMBot and makes a new GroupMeChat. The requirements for the plugin to run.
	 */
	public void instantiateIMBot()
	{
		this.groupMeChat = new GroupMeChat();
		this.bot = new Bot(this.groupMeChat, 
						   GroupMeStoredData.getGroupMeBotName(), 
						   GroupMeStoredData.getGroupMeGroupName(), 
						   GroupMeStoredData.getBotCommandPrefix(), 
						   this.authenticationHolder); 
	}
	
	/**
	 * Tells the polling functionality to begin a new instance.
	 */
	public void startPolling()
	{
		if(this.polling != null) {
			close();
		}
		this.polling = new GroupMeMessagePolling(new GroupMeAPIInterface(GroupMeStoredData.getGroupMeToken()), bot);
	}
	
	/**
	 * Registers the GroupMeBot so that it can be used by the plugin
	 */
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
			GroupMeBot.initWithBotId(storedId,
								GroupMeStoredData.getGroupMeBotName(), 
								GroupMeStoredData.getGroupMeToken(), 
								GroupMeStoredData.getGroupMeGroupId(), 
								new GroupMeBotConnection(GroupMeStoredData.getGroupMeToken()));
		}
	}
	
	/**
	 * Registers the GroupMeBot regardless of StoredData
	 */
	public static void forceRegisterGroupMeBot()
	{
		GroupMeBot.init(GroupMeStoredData.getGroupMeBotName(), 
						GroupMeStoredData.getGroupMeToken(), 
						GroupMeStoredData.getGroupMeGroupId(), 
						new GroupMeBotConnection(GroupMeStoredData.getGroupMeToken()));
		GroupMeBot.register();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean connect() {
		//spawn thread to run polling
		this.polling.init();
		return isConnected();
	}

	/**
	 * 
	 * @return The name of the GroupMeGroup
	 */
	public String getName() {
		return GroupMeStoredData.getGroupMeGroupName();
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isConnected() {
		return polling.isPolling();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		polling.close();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void open() {
		polling.init();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void send(IMMessageTarget target, String text) throws IMException {
		this.groupMeChat.sendMessage(text);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPresence(IMPresence presence, String statusMessage) throws IMException {
		//Not needed due to GroupMe being a REST API
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addConnectionListener(IMConnectionListener listener) {
		//Not needed due to GroupMe being a REST API
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeConnectionListener(IMConnectionListener listener) {
		//Not needed due to GroupMe being a REST API
	}

}
