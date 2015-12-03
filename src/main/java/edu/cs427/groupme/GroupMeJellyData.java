import hudson.Extension;
import org.kohsuke.stapler.QueryParameter;
import hudson.model.RootAction;

public class GroupMeJellyData implements RootAction{

	public String getGroupMeAccessToken(){
		return "z";
	}
	
	public String getIconFileName(){
		return "";
	}
	
	public String getDisplayName(){
		return "GroupMe Settings";
	}
	
	public String getUrlName(){
		return "groupme";
	}

}
