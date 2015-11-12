package edu.cs427.groupme;

import hudson.Extension;
import hudson.Util;
import hudson.model.AbstractProject;
import hudson.model.User;
import hudson.plugins.im.GroupChatIMMessageTarget;
import hudson.plugins.im.IMConnection;
import hudson.plugins.im.IMException;
import hudson.plugins.im.IMMessageTarget;
import hudson.plugins.im.IMMessageTargetConverter;
import hudson.plugins.im.IMPublisher;
import hudson.plugins.im.IMPublisherDescriptor;
import hudson.plugins.im.MatrixJobMultiplier;
import hudson.plugins.im.NotificationStrategy;
import hudson.plugins.im.build_notify.BuildToChatNotifier;
import hudson.plugins.im.config.ParameterNames;
import hudson.plugins.im.tools.ExceptionHelper;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;


import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.logging.Logger;

//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;

/**
 * Implementation of GroupMePublisher that provides a description
 * called from GroupMeIMConnection
 * 
 * Publisher is intended to push build results to where ever necessary, <- not implemented yet
 * Only basic functionality for descriptors is implemented
 *
 * Used IRCPublisher as reference
 * @author aymei2 hlee145
 */
 
 public class GroupMePublisher extends IMPublisher {
 	 
	 // TO DO: Implement getName -- look at IRCConnection.java
	 private static final Logger LOGGER = Logger.getLogger(GroupMeIMConnection.class.getName());
	 
     public static final DescriptorImp DESCRIPTOR = new DescriptorImp();
 
 		 //Returns the descriptor for the build
     @Override
     public BuildStepDescriptor<Publisher> getDescriptor() {
        return DESCRIPTOR;
     }
 
     // This constructor passes its parameters directly into its parent class (IMPublisher) and creates an instance of that
     // Takes in the options on who to notify about the build results and targets
     public GroupMePublisher(List<IMMessageTarget> defaultTargets, String notificationStrategy,
     		boolean notifyGroupChatsOnBuildStart,
     		boolean notifySuspects,
     		boolean notifyCulprits,
     		boolean notifyFixers,
     		boolean notifyUpstreamCommitters,
             BuildToChatNotifier buildToChatNotifier,
             MatrixJobMultiplier matrixMultiplier)
     {
         super(defaultTargets, notificationStrategy, notifyGroupChatsOnBuildStart,
         		notifySuspects, notifyCulprits, notifyFixers, notifyUpstreamCommitters,
         		buildToChatNotifier, matrixMultiplier);
     }

     //A class for a descriptor implementation
     public static final class DescriptorImp extends BuildStepDescriptor<Publisher> implements IMPublisherDescriptor {
        
        DescriptorImp() {
            super(GroupMePublisher.class);
            load();
			try {
              	GroupMeConnectionProvider.setDesc(this);
            } catch (final Exception e) {
                // Server temporarily unavailable or misconfigured?
                LOGGER.warning(ExceptionHelper.dump(e));
            }
        }
        
        
        
        //Methods from IMPublisherDescriptor
     
//		 * Internally used to construct the parameter names on the config page.
//     * @return a prefix which must be unique among all IM plugins.
     public ParameterNames getParamNames(){
     	return null;
     }
    
	
//	 	 * Returns <code>true</code> iff the plugin is globally enabled.
     public boolean isEnabled(){
		 	return false;
		 }
	
	
//		 * Returns an informal, short description of the concrete plugin.
		 public String getPluginDescription(){
		 	return null;
		 }
	
	
//	 	 * Returns if the plugin should expose its presence on the IM network.
//	 	 * I.e. if it should report as 'available' or that like.
		 public boolean isExposePresence(){
     	return false;
     }
    
//     * Returns the hostname of the IM network. I.e. the host to which the plugin should connect.
     public String getHost(){
    		return null;
    	}
    
//     * Returns the hostname. May be null in which case the host must be determined from the
//     * Jabber 'service name'.
//     * 
//     * @deprecated Should be replaced by getHost
    	@Deprecated
    	public String getHostname(){
    		return null;
    	}
    
//     * Returns the port of the IM network..
    	public int getPort(){
    		return 0;
    	}
    
//     * Returns the user name needed to login into the IM network.
    	public String getUserName(){
   	 	return null;
   	 }
    
//     * Returns the password needed to login into the IM network.
   	public String getPassword() {
     	return null;
     }
    
//		 * Returns the prefix for the commands
     public String getCommandPrefix(){
     	return null;
     }
 
     public String getDefaultIdSuffix(){
     	return null;
     }
    
//     * Returns the user name needed to login into Hudson.
     public String getHudsonUserName(){
     	return null;
     }
    
//     * Returns the default targets which should be used for build notification.
//     * 
//     * This can be overwritten on a per job basis.
     public List<IMMessageTarget> getDefaultTargets() {
     	return null;
     }
    
     public IMMessageTargetConverter getIMMessageTargetConverter(){
     	return null;
     }



	@Override
	public boolean isApplicable(Class<? extends AbstractProject> arg0) {
		return false;
	}



	@Override
	public String getDisplayName() {
		return null;
	}
        
        
	}
 	
     //Returns the plugin name for our implementation
    @Override
 	protected String getPluginName() {
		return "GroupMe notifier plugin";
	}
   
   
   //Returns the current instance of the im connection  
 	@Override
 	protected IMConnection getIMConnection() throws IMException {
 		return GroupMeConnectionProvider.getInstance().currentConnection();
 	 }

	 //Returns a user id, may not be necessary for our groupme implemenetation
 	@Override
	protected String getConfiguredIMId(User user) {
		// I don't think this method is important. -- Austin and Henry
 		return null;
	}
}

