import hudson.Extension;
import org.kohsuke.stapler.QueryParameter;
import hudson.model.RootAction;

public class GroupMeJellyData {

	public String getGroupMeAccessToken() implements RootAction{
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
