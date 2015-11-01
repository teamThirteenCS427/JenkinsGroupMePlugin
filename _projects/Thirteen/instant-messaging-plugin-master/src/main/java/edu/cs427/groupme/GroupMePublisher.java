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
 * Used IRCPublisher as reference
 * @author aymei2 hlee145
 */
 
 public class GroupMePublisher extends IMPublisher {
 	 
	 // TO DO: Implement getName -- look at IRCConnection.java
	 private static final Logger LOGGER = Logger.getLogger(GroupMeIMConnection.class.getName());
	 
     public static final DescriptorImp DESCRIPTOR = new DescriptorImp();
 
     @Override
     public BuildStepDescriptor<Publisher> getDescriptor() {
        return DESCRIPTOR;
     }
 
     // This constructor passes its parameters directly into its parent class (IMPublisher) and creates an instance of that
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

     // TODO: Implement abstract methods -- either create them or delete them from the original class
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
	}
 	
     
    @Override
 	protected String getPluginName() {
		return "GroupMe notifier plugin";
	}
     
 	@Override
 	protected IMConnection getIMConnection() throws IMException {
 		return GroupMeConnectionProvider.getInstance().currentConnection();
 	 }

 	@Override
	protected String getConfiguredIMId(User user) {
		// I don't think this method is important. -- Austin and Henry
 		return null;
	}
 	
     
     

 }