package edu.cs427.groupme;

import java.util.logging.Logger;

import hudson.plugins.im.IMConnection;
import hudson.plugins.im.IMConnectionProvider;
import hudson.plugins.im.IMException;
import hudson.plugins.im.IMPublisherDescriptor;
import hudson.plugins.im.bot.Bot;

/**
 * Implementation of GroupMeConnectionProvider that provides a connection for
 * the GroupMeConnection
 * 
 * Used IRCConnectionProvider as reference
 * 
 * @author espaill2 zavelev2
 */

public class GroupMeConnectionProvider extends IMConnectionProvider {

	private static final Logger LOGGER = Logger.getLogger(GroupMeConnectionProvider.class.getName());
	private static final IMConnectionProvider INSTANCE = new GroupMeConnectionProvider();

	/**
	 * 
	 * @return the Instance
	 */
	public static final synchronized IMConnectionProvider getInstance() {
		return INSTANCE;
	}

	/**
	 * Sets the descriptor and releases the connection
	 * 
	 * @param desc
	 *            Descriptor for the instance
	 * @throws IMException
	 */
	public static final synchronized void setDesc(IMPublisherDescriptor desc) throws IMException {
		INSTANCE.setDescriptor(desc);
		INSTANCE.releaseConnection();
	}

	private GroupMeConnectionProvider() {
		super();
		this.init();
	}

	@Override
	protected void init() {
		try {
			this.createConnection();

		} catch (IMException e) {
			// TODO Auto-generated catch block
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized IMConnection createConnection() throws IMException {
		if (imConnection == NULL_CONNECTION)
			imConnection = new GroupMeIMConnection(getAuthenticationHolder());
		GroupMeStoredData.setIMConnection((GroupMeIMConnection) imConnection);
		if (getDescriptor() == null) {
			LOGGER.warning("Descriptor NOT set!");
		}
		if (imConnection.connect()) {
			return imConnection;
		} else {
			imConnection.close();
		}
		throw new IMException("Connection failed");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void releaseConnection() {
		if (imConnection != null) {
			imConnection.removeConnectionListener(this);
			imConnection.close();
			imConnection = NULL_CONNECTION;
		}
	}

}
