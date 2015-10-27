package edu.cs427.groupme;

import hudson.plugins.im.AbstractIMConnection;
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
	private GroupMeMessagePolling polling;
	
	// An interface containing many important connection variables
	private IMPublisherDescriptor desc;
	
	
	public GroupMeIMConnection(IMPublisherDescriptor desc) {
		super(desc);
		this.desc = desc;
		polling = new GroupMeMessagePolling(new GroupMeAPIInterface(GROUPME_TOKEN, GROUPME_GROUP_ID));
	}

	@Override
	public boolean connect() {
		// TODO Auto-generated method stub
		
		return false;
	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

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

	}

	@Override
	public void removeConnectionListener(IMConnectionListener listener) {
		// TODO Auto-generated method stub

	}

}
