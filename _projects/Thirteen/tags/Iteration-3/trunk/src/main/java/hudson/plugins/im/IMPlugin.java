package hudson.plugins.im;

import edu.cs427.groupme.GroupMeBot;

/**
 * initiate the Instant Messaging plugin and connect to the IM Connection Provider
 * @author Dima
 * @author Enrique
 */
public class IMPlugin {
	
	private transient IMConnectionProvider provider;
	private transient JenkinsIsBusyListener busyListener;

    public IMPlugin(IMConnectionProvider provider) {
    	this.provider = provider;
    }
    
    public void start() throws Exception {
    	this.busyListener = JenkinsIsBusyListener.getInstance();
    	this.busyListener.addConnectionProvider(this.provider);
    }

    public void stop() throws Exception {
    	this.busyListener.removeConnectionProvider(this.provider);
    	this.provider.releaseConnection();
    }
}
