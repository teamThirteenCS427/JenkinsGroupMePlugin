package edu.cs427.groupme;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.swing.JOptionPane;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import hudson.Extension;
import hudson.ExtensionPoint;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.RootAction;
import jenkins.model.Jenkins;
import jenkins.model.ModelObjectWithContextMenu.ContextMenu;

/**
 * A data class that interacts with the plugin's Jelly Interface
 * @author blessin2 hlee145
 */
@Extension
public class GroupMeJellyData implements RootAction{
	
	private static final Logger LOGGER = Logger.getLogger(GroupMeJellyData.class.getName());
	
	/**
	 * Required by RootAction
	 * Returns the filename of the icon that accompanies the link on the main Jenkins page
	 */
	public String getIconFileName(){
		return "gear.png";
	}
	
	/**
	 * Required by RootAction
	 * Returns the displayed name on the main Jenkins page
	 */
	public String getDisplayName(){
		return "GroupMe Settings";
	}
	
	/**
	 * Required by RootAction
	 * Returns the page URL of this Jelly page
	 * This is not a reference to the Jelly file itself, this is the actual URL that Jenkins will use for navigation
	 */
	public String getUrlName(){
		return "groupme";
	}

	/**
	 * Required by RootAction
	 * Returns a ContextMenu for any subpages of this page
	 * Since this page has no subpages, the ContextMenu is empty
	 * @param request
	 * @param response
	 * @return An empty ContextMenu object
	 * @throws Exception
	 */
	public ContextMenu doContextMenu(StaplerRequest request, StaplerResponse response) throws Exception{
		return new ContextMenu();
	}
	
	
	/**
	 * An initializer for the data used by the the Jelly Form
	 * @return A StoredData object initialized from GroupMeStoredData values
	 */
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
	
	
	/**
	 * A data class used by the Jelly Form
	 * @author blessin2 hlee145
	 */
	public static class StoredData implements ExtensionPoint, Describable<StoredData> {

		private String groupMeGroupId;
		private String groupMeToken;
		private String groupMeBotName;
		private String groupMeGroupName;
		private String botCommandPrefix;
		
		/**
		 * StoredData Constructor - Stores all data given as is
		 * @param groupMeGroupId
		 * @param groupMeToken
		 * @param groupMeBotName
		 * @param groupMeGroupName
		 * @param botCommandPrefix
		 */
		@DataBoundConstructor
		public StoredData(String groupMeGroupId, String groupMeToken, String groupMeBotName, String groupMeGroupName, String botCommandPrefix) {
			this.setGroupMeGroupId(groupMeGroupId);
			this.setGroupMeToken(groupMeToken);
			this.setGroupMeBotName(groupMeBotName);
			this.setGroupMeGroupName(groupMeGroupName);
			this.setBotCommandPrefix(botCommandPrefix);
		}
		
		/**
		 * Returns the Descriptor instance for StoredData as assigned by Jenkins
		 * @return A StoredDataDescriptor instance
		 */
		@Override
		public Descriptor<StoredData> getDescriptor() {
			return Jenkins.getInstance().getDescriptor(getClass());
		}
		
		@Extension
		public static final StoredDataDescriptor D = new StoredDataDescriptor(StoredData.class);

		/**
		 * Get method for groupMeGroupId
		 * @return groupMeGroupId
		 */
		public String getGroupMeGroupId() {
			return groupMeGroupId;
		}

		/**
		 * Set method for groupMeGroupId
		 * @param groupMeGroupId
		 */
		public void setGroupMeGroupId(String groupMeGroupId) {
			this.groupMeGroupId = groupMeGroupId;
		}

		/**
		 * Get method for groupMeToken
		 * @return groupMeToken
		 */
		public String getGroupMeToken() {
			return groupMeToken;
		}

		/**
		 * Set method for groupMeToken
		 * @param groupMeToken
		 */
		public void setGroupMeToken(String groupMeToken) {
			this.groupMeToken = groupMeToken;
		}

		/**
		 * Get method for groupMeBotName
		 * @return groupMeBotName
		 */
		public String getGroupMeBotName() {
			return groupMeBotName;
		}

		/**
		 * Set method for groupMeBotName
		 * @param groupMeBotName
		 */
		public void setGroupMeBotName(String groupMeBotName) {
			this.groupMeBotName = groupMeBotName;
		}

		/**
		 * Get method for groupMeGroupName
		 * @return groupMeGroupName
		 */
		public String getGroupMeGroupName() {
			return groupMeGroupName;
		}
		
		/**
		 * Set method for groupMeGroupName
		 * @param groupMeGroupName
		 */
		public void setGroupMeGroupName(String groupMeGroupName) {
			this.groupMeGroupName = groupMeGroupName;
		}

		/**
		 * Get method for botCommandPrefix
		 * @return botCommandPrefix
		 */
		public String getBotCommandPrefix() {
			return botCommandPrefix;
		}

		/**
		 * Set method for botCommandPrefix
		 * @param botCommandPrefix
		 */
		public void setBotCommandPrefix(String botCommandPrefix) {
			this.botCommandPrefix = botCommandPrefix;
		}
		
		/**
		 * Returns the StoredData instance as a formatted string
		 * Formatted as [groupId, token, botName, groupName, botCommandPrefix]
		 * If a value is null it is replaced by the string "null"
		 * @return Formatted string representation of this StoredData instance
		 */
		public String toString() {
			return "[" + (groupMeGroupId==null?"null":groupMeGroupId) + ", " + 
						 (groupMeToken==null?"null":groupMeToken) + ", " + 
						 (groupMeBotName==null?"null":groupMeBotName) + ", " + 
						 (groupMeGroupName==null?"null":groupMeGroupName) + ", " + 
						 (botCommandPrefix==null?"null":botCommandPrefix) + "]";
		}
	}
	
	
	/**
	 * A descriptor class for StoredData
	 * @author blessin2 hlee145
	 */
	public static class StoredDataDescriptor extends Descriptor<StoredData> {
		
		/**
		 * Constructor for StoredDataDescriptor
		 * @param clazz A class reference of the class being Described
		 */
        public StoredDataDescriptor(Class<StoredData> clazz) {
            super(clazz);
        }
        
        /**
         * Returns the display name of the class being described
         * @return The display name of the class being described
         */
        public String getDisplayName() {
            return clazz.getSimpleName();
        }
    }
}
