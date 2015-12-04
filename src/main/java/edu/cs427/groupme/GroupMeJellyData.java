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

import edu.cs427.groupme.GroupMeStoredData;

@Extension
public class GroupMeJellyData implements RootAction{
	
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
	
	
	public void doSubmit(StaplerRequest req, StaplerResponse rsp) throws ServletException, IOException {
        StoredData data = req.bindJSON(StoredData.class, req.getSubmittedForm().getJSONObject("storedData"));
        //TODO: send data to GroupMeStoredData
        //TODO: Redirect to main jenkins page?
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
