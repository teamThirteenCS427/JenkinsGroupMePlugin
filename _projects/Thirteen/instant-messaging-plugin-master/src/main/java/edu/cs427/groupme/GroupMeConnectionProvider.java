package edu.cs427.groupme;

import hudson.plugins.im.IMConnection;
import hudson.plugins.im.IMConnectionProvider;
import hudson.plugins.im.IMException;
import hudson.plugins.im.IMPublisherDescriptor;

/**
 * Implementation of GroupMeConnectionProvider that provides a connection
 * for the GroupMeConnection
 * 
 * Used IRCGroupMeConnectionProvider as reference
 * @author aymei2 hlee145
 */


public class GroupMeConnectionProvider extends IMConnectionProvider {

    private static final IMConnectionProvider INSTANCE = new GroupMeConnectionProvider();
    
    public static final synchronized IMConnectionProvider getInstance() {
        return INSTANCE;
    }
    
    public static final synchronized void setDesc(IMPublisherDescriptor desc) throws IMException {
    	INSTANCE.setDescriptor(desc);
    	INSTANCE.releaseConnection();
    }

    private GroupMeConnectionProvider() {
    	// super() calls parent constructor -- confused as constructor looks like it does nothing
		super();
    	init();
    }

    @Override
    public synchronized IMConnection createConnection() throws IMException {
        releaseConnection();

        if (getDescriptor() == null) {
        	throw new IMException("Descriptor not set");
        }
        
        IMConnection imConnection = new GroupMeConnection((GroupMePublisher.DescriptorImpl)getDescriptor(),
        		getAuthenticationHolder());
        if (imConnection.connect()) {
        	return imConnection;
        } else {
        	imConnection.close();
        }
        throw new IMException("Connection failed");
    }

}