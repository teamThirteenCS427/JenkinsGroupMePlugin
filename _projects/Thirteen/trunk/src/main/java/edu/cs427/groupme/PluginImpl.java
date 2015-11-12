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

	public PluginImpl() {
		GroupMeStoredData.init();
		this.imPlugin = new IMPlugin(GroupMeConnectionProvider.getInstance());
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
