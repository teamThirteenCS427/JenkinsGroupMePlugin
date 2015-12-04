package edu.cs427.groupme;

import hudson.Extension;
import org.kohsuke.stapler.QueryParameter;
import hudson.model.RootAction;
import jenkins.model.ModelObjectWithContextMenu;
import jenkins.model.ModelObjectWithContextMenu.ContextMenu;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

@Extension
public class GroupMeJellyData implements RootAction{

	public String getGroupMeAccessToken(){
		return "z";
	}
	
	public String getIconFileName(){
		return "gear.png";
	}
	
	public String getDisplayName(){
		return "GroupMe Settings";
	}
	
	public String getUrlName(){
		return "groupme";
	}

	
	public ContextMenu doContestMenu(StaplerRequest request, StaplerResponse response) throws Exception{
		return new ContextMenu();
	}
}
