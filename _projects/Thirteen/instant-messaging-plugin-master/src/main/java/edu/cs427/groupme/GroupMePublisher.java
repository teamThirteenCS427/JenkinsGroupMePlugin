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
import hudson.plugins.ircbot.v2.IRCConnectionProvider;
import hudson.plugins.ircbot.v2.IRCMessageTargetConverter;
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
 
     public static final class DescriptorImp extends BuildStepDescriptor<Publisher> implements IMPublisherDescriptor {
        
        DescriptorImp() {
            super(GroupMePublisher.class);
            load();
			try {
              	IRCConnectionProvider.setDesc(this);
            } catch (final Exception e) {
                // Server temporarily unavailable or misconfigured?
                LOGGER.warning(ExceptionHelper.dump(e));
            }
        }
	}

 }