package edu.cs427.groupme;

import hudson.Plugin;
import hudson.plugins.im.IMPlugin;

/**
 * This is the class that starts the actual plugin
 * @author zavelev2, espaie2
 *
 */
public class PluginImpl extends Plugin {
	private transient IMPlugin imPlugin = null;

	public PluginImpl() {
		GroupMeStoredData.init();
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    public void start() throws Exception {
        super.start();
        //The listener and IMMessanger gets instantiated here
        if(imPlugin == null)
        	this.imPlugin = new IMPlugin(GroupMeConnectionProvider.getInstance());
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
