package hudson.plugins.im;

import hudson.model.User;
import hudson.plugins.im.tools.ExceptionHelper;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.acegisecurity.Authentication;
import org.acegisecurity.userdetails.UsernameNotFoundException;

import edu.cs427.groupme.GroupMeStoredData;

/**
 * Abstract implementation of a provider of {@link IMConnection}s.
 * 
 * @author kutzi
 */
public abstract class IMConnectionProvider implements IMConnectionListener {
	
	private static final Logger LOGGER = Logger.getLogger(IMConnectionProvider.class.getName());
	
	protected static final IMConnection NULL_CONNECTION = new DummyConnection();

	protected IMPublisherDescriptor descriptor;
	protected IMConnection imConnection = NULL_CONNECTION;
	
	private Authentication authentication = null;
	
//	private final ConnectorRunnable connector = new ConnectorRunnable();
    
	protected IMConnectionProvider() {
	}
	
	/**
	 * Must be called once to initialize the provider.
	 */
	protected void init() {
//		Thread connectorThread = new Thread(this.connector, "IM-Reconnector-Thread");
//		connectorThread.setDaemon(true);
//		connectorThread.start();
//		tryReconnect();
	}
	
	/**
	 * Creates a new connection.
	 * 
	 * @return the new connection. Never null.
	 * @throws IMException if the connection couldn't be created for any reason.
	 * @throws IMException
	 */
	public abstract IMConnection createConnection() throws IMException;

	private synchronized boolean create() throws IMException {
		if (this.descriptor == null || !this.descriptor.isEnabled()) {

			// plugin is disabled
			this.imConnection = NULL_CONNECTION;
			return true;
		}
		
		try {
			this.imConnection = createConnection();
			this.imConnection.addConnectionListener(this);
			return true;
		} catch (IMException e) {
			this.imConnection = NULL_CONNECTION;
			tryReconnect();
			return false;
		}
	}
	
	/**
	 * Return the current connection.
	 * Returns an instance of {@link DummyConnection} if the plugin
	 * is currently not connection to a IM network.
	 * 
	 * @return the current connection. Never null.
	 */
    public synchronized IMConnection currentConnection() {
        return this.imConnection;
    }

    /**
     * Releases (and thus closes) the current connection.
     */
    public synchronized void releaseConnection() {
        if (this.imConnection != null) {
        	this.imConnection.removeConnectionListener(this);
        	this.imConnection.close();
            this.imConnection = NULL_CONNECTION;
        }
    }

	protected IMPublisherDescriptor getDescriptor() {
		return this.descriptor;
	}

	public void setDescriptor(IMPublisherDescriptor desc) {
		this.descriptor = desc;
		
		if (desc != null && desc.isEnabled()) {
		    tryReconnect();
		}
	}
	
    @Override
	public void connectionBroken(Exception e) {
		tryReconnect();
	}
    
    private void tryReconnect() {
//    	this.connector.semaphore.release();
    }

    // we need an additional level of indirection to the Authentication entity
    // to fix HUDSON-5978 and HUDSON-5233
	public synchronized AuthenticationHolder getAuthenticationHolder() {
//	    if (descriptor == null || descriptor.getHudsonUserName() == null) {
//	        return null;
//	    }
	    
	    return new AuthenticationHolder() {
            @Override
            public Authentication getAuthentication() {
                if (authentication != null) {
                    return authentication;
                }
                
                User u = User.get(GroupMeStoredData.getGroupMeBotName()); //Authenticating the JenkinsBot
                try{
                	return u.impersonate();
                } catch (UsernameNotFoundException e) {
                	LOGGER.warning("Username not found, therefore no authentication holder.");
                }
                return null;
            }
        };
	}
}
