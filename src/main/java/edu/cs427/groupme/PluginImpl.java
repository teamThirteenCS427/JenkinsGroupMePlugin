package edu.cs427.groupme;

import hudson.Plugin;
import hudson.plugins.im.IMPlugin;

public class PluginImpl extends Plugin {
	private transient final IMPlugin imPlugin;

	public PluginImpl() {
		this.imPlugin = new IMPlugin(GroupMeConnectionProvider.getInstance());
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    public void start() throws Exception {
        super.start();
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
