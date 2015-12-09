package hudson.plugins.im;

import java.util.logging.Logger;

import org.acegisecurity.Authentication;
import org.acegisecurity.userdetails.UsernameNotFoundException;

import edu.cs427.groupme.GroupMeStoredData;
import hudson.model.User;

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
    
	protected IMConnectionProvider() {
	}
	
	/**
	 * Must be called once to initialize the provider.
	 */
	protected void init() {
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
    }

    /**
     * 
     * we need an additional level of indirection to the Authentication entity to fix HUDSON-5978 and HUDSON-5233
     * @return Authentication holder for the authentication of the user being impersonated
     */
	public synchronized AuthenticationHolder getAuthenticationHolder() {
	    
	    return new AuthenticationHolder() {
            @Override
            public Authentication getAuthentication() {
                if (authentication != null) {
                    return authentication;
                }
                LOGGER.warning("USERNAME FOR IMPERSONATION = " + GroupMeStoredData.getGroupMeBotName());
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
