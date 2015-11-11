package edu.cs427.groupme;

import hudson.plugins.im.IMConnection;
import hudson.plugins.im.IMConnectionProvider;
import hudson.plugins.im.IMException;
import hudson.plugins.im.IMPublisherDescriptor;

/**
 * Implementation of GroupMeConnectionProvider that provides a connection
 * for the GroupMeConnection
 * 
 * Used IRCConnectionProvider as reference
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
		//super();
		//making sure to call init from this class (super class also has init())
    	this.init();
    }
    
    @Override
    protected void init(){
    	try {
			this.createConnection();

		} catch (IMException e) {
			// TODO Auto-generated catch block
		}
    }
    
    @Override
    public synchronized IMConnection createConnection() throws IMException {
        imConnection = new GroupMeIMConnection();
        if (imConnection.connect()) {
        	return imConnection;
        } else {
        	imConnection.close();
        }
        throw new IMException("Connection failed");
    }
    
    @Override
    public synchronized void releaseConnection() {
        if (imConnection != null) {
        	GroupMeBot.sendTextMessage("Releasing Connection from Provider class");
        	imConnection.removeConnectionListener(this);
        	imConnection.close();
            imConnection = NULL_CONNECTION;
        }
    }

    
}

