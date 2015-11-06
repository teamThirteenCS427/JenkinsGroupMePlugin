package edu.cs427.groupme;

import hudson.Plugin;
import hudson.plugins.im.IMPlugin;

/**
 * This is the class that starts the actual plugin
 * @author Dima and Erique
 *
 */
public class PluginImpl extends Plugin {
	private transient final IMPlugin imPlugin;
	private static final String GROUPME_TOKEN = "8fyym11XsTj5XrHTHzNChDDHia0LAM4afuflybhg";
	private static final String GROUPME_GROUP_ID = "17407658";

	public PluginImpl() {
		GroupMeBot.init("JenkinsBot", GROUPME_TOKEN, GROUPME_GROUP_ID, new GroupMeBotConnection());
		GroupMeBot.register();
		this.imPlugin = new IMPlugin(GroupMeConnectionProvider.getInstance());
		GroupMeBot.sendTextMessage("PluginImpl Instantiated");
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    public void start() throws Exception {
        super.start();
        //The listener and IMMessanger gets instantiated here
        this.imPlugin.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() throws Exception {
    	this.imPlugin.stop();
        super.stop();
    }
}
