package edu.cs427.groupme;

import hudson.Extension;
import org.kohsuke.stapler.QueryParameter;
import hudson.model.RootAction;
import jenkins.model.Jenkins;
import jenkins.model.ModelObjectWithContextMenu;
import jenkins.model.ModelObjectWithContextMenu.ContextMenu;

import java.io.IOException;

import javax.servlet.ServletException;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import hudson.ExtensionPoint;
import hudson.model.Describable;
import hudson.model.Descriptor;

import java.util.logging.Logger;

import edu.cs427.groupme.GroupMeStoredData;

import javax.swing.JOptionPane;

/**
 * A data class that interacts with the plugin's Jelly Interface
 * @author blessin2 hlee145
 */
@Extension
public class GroupMeJellyData implements RootAction{
	
	private static final Logger LOGGER = Logger.getLogger(GroupMeJellyData.class.getName());
	
	public String getIconFileName(){
		return "gear.png";
	}
	
	public String getDisplayName(){
		return "GroupMe Settings";
	}
	
	public String getUrlName(){
		return "groupme";
	}

	public ContextMenu doContextMenu(StaplerRequest request, StaplerResponse response) throws Exception{
		return new ContextMenu();
	}
	
	
	//Initial Data
	public StoredData getStoredData() {
		return new StoredData(GroupMeStoredData.getGroupMeGroupId(),
							  GroupMeStoredData.getGroupMeToken(),
							  GroupMeStoredData.getGroupMeBotName(),
							  GroupMeStoredData.getGroupMeGroupName(),
							  GroupMeStoredData.getBotCommandPrefix());
	}
	
	
	/**
	 * Called on Jelly form submission
	 * Checks each field against GroupMeStoredData, and if different, saves the new value
	 * @param req 
	 * @param rsp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doSubmit(StaplerRequest req, StaplerResponse rsp) throws ServletException, IOException {
        StoredData data = req.bindJSON(StoredData.class, req.getSubmittedForm());

        if (data != null)
        {
        	LOGGER.info("GroupMe Settings Form Submitted" + data.toString());
        	
        	//Check for changes and write to GroupMeStoredData if different
        	if (changed(GroupMeStoredData.getGroupMeGroupId(), data.getGroupMeGroupId()))
        		GroupMeStoredData.setGroupMeGroupId(data.getGroupMeGroupId());
        	if (changed(GroupMeStoredData.getGroupMeToken(), data.getGroupMeToken()))
        		GroupMeStoredData.setGroupMeToken(data.getGroupMeToken());
        	if (changed(GroupMeStoredData.getGroupMeGroupName(), data.getGroupMeGroupName()))
        		GroupMeStoredData.setGroupMeGroupName(data.getGroupMeGroupName());
        	if (changed(GroupMeStoredData.getGroupMeBotName(), data.getGroupMeBotName()))
        		GroupMeStoredData.setGroupMeBotName(data.getGroupMeBotName());
        	if (changed(GroupMeStoredData.getBotCommandPrefix(), data.getBotCommandPrefix()))
        		GroupMeStoredData.setBotCommandPrefix(data.getBotCommandPrefix());
        	
        	//Redirect away from blank "Submit" page
        	rsp.forwardToPreviousPage(req);
        }
        else
        {
        	LOGGER.severe("GroupMe Settings Form Submission Failure");
        	JOptionPane.showMessageDialog(null, "Submission Failed", "GroupMe Settings", JOptionPane.ERROR_MESSAGE);
        }
    }
	
	/**
	 * Simple string inequality check to improve readability
	 * @param init The original value of the string
	 * @param newVal The possibly modified new value
	 * @return A boolean indicating if the Strings are different
	 */
	private boolean changed(String init, String newVal) {
		return !init.equals(newVal);
	}
	
	
	public static class StoredData implements ExtensionPoint, Describable<StoredData> {

		private String groupMeGroupId;
		private String groupMeToken;
		private String groupMeBotName;
		private String groupMeGroupName;
		private String botCommandPrefix;
		
		@DataBoundConstructor
		public StoredData(String groupMeGroupId, String groupMeToken, String groupMeBotName, String groupMeGroupName, String botCommandPrefix) {
			this.setGroupMeGroupId(groupMeGroupId);
			this.setGroupMeToken(groupMeToken);
			this.setGroupMeBotName(groupMeBotName);
			this.setGroupMeGroupName(groupMeGroupName);
			this.setBotCommandPrefix(botCommandPrefix);
		}
		
		@Override
		public Descriptor<StoredData> getDescriptor() {
			return Jenkins.getInstance().getDescriptor(getClass());
		}
		
		@Extension
		public static final StoredDataDescriptor D = new StoredDataDescriptor(StoredData.class);

		public String getGroupMeGroupId() {
			return groupMeGroupId;
		}

		public void setGroupMeGroupId(String groupMeGroupId) {
			this.groupMeGroupId = groupMeGroupId;
		}

		public String getGroupMeToken() {
			return groupMeToken;
		}

		public void setGroupMeToken(String groupMeToken) {
			this.groupMeToken = groupMeToken;
		}

		public String getGroupMeBotName() {
			return groupMeBotName;
		}

		public void setGroupMeBotName(String groupMeBotName) {
			this.groupMeBotName = groupMeBotName;
		}

		public String getGroupMeGroupName() {
			return groupMeGroupName;
		}

		public void setGroupMeGroupName(String groupMeGroupName) {
			this.groupMeGroupName = groupMeGroupName;
		}

		public String getBotCommandPrefix() {
			return botCommandPrefix;
		}

		public void setBotCommandPrefix(String botCommandPrefix) {
			this.botCommandPrefix = botCommandPrefix;
		}
		
		public String toString() {
			return "[" + (groupMeGroupId==null?"null":groupMeGroupId) + ", " + 
						 (groupMeToken==null?"null":groupMeToken) + ", " + 
						 (groupMeBotName==null?"null":groupMeBotName) + ", " + 
						 (groupMeGroupName==null?"null":groupMeGroupName) + ", " + 
						 (botCommandPrefix==null?"null":botCommandPrefix) + "]";
		}
	}
	
	
	
	public static class StoredDataDescriptor extends Descriptor<StoredData> {
        public StoredDataDescriptor(Class<StoredData> clazz) {
            super(clazz);
        }
        public String getDisplayName() {
            return clazz.getSimpleName();
        }
    }
}
