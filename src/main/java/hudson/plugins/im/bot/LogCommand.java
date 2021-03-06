package hudson.plugins.im.bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Logger;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.plugins.im.Sender;
import hudson.scm.ChangeLogSet;
import hudson.scm.ChangeLogSet.Entry;

/**
 * Returns the latest *specified number* of logs
 * @author fricken2 aymei2
 */

@Extension
public class LogCommand extends AbstractMultipleJobCommand {

	private static final Logger LOGGER = Logger.getLogger(LogCommand.class.getName());
	private static final String HELP = "log <job> <int>- Displays recent (2nd parameter) svn logs for that job";
	

	@Override
	public Collection<String> getCommandNames() {
        	return Collections.singleton("log");
    	}


	@Override
    	protected String getCommandShortName() {
        	return "log";
	}
	

	/*
	 * Returns string to display when viewing help
	 */
	@Override
	public String getHelp() {
		return HELP;
	}

	/*
	 * Called when someone tried to see a log via !log jobname
	 */
	@Override
	public String getReply(Bot bot, Sender sender, String[] args) {
    		int numBuilds = -1;
	    	boolean usingBuildNumber = false;
	    	if(args.length >= 3) {
	    		usingBuildNumber = true;
	    		try {
	    			numBuilds = Integer.parseInt(args[2]);
	    		}
	    		catch (NumberFormatException nfe) {
	    			numBuilds = 3;
	    		}
	    		args = Arrays.copyOfRange(args, 0, 2); 
	    	}
		
		if(numBuilds > 20) {
			numBuilds = 20;
		}
		if(numBuilds < 0) {
			numBuilds = 1;
		}
			
	
		StringBuilder msg = new StringBuilder();
		Collection<AbstractProject<?, ?>> projects = new ArrayList<AbstractProject<?, ?>>();
		try {
			getProjects(sender, args, projects);
        	} catch (CommandException e) {
	            	return getErrorReply(sender, e);
        	}
		
		//if there is a project with that name
	        if (!projects.isEmpty()) {
			//For each project with that name
		        for (AbstractProject<?, ?> project : projects) {
		                msg.append(getLogs(project, numBuilds));
				msg.append("-\n");
            		}
            	return msg.toString();
        	} else {
				return sender + ": no job found";
        	}
	}

	/*
	 * For the last build, return logs by calling getChanges.
	 */
	protected CharSequence getLogs(AbstractProject<?, ?> project, int numBuilds) {
    		StringBuilder msg = new StringBuilder(32);
        	msg.append(project.getFullDisplayName() + "\n");

		//Get the last build
        	AbstractBuild<?, ?> lastBuild = project.getLastBuild();
        	while(numBuilds > 0) {
			//Get data from last build
			if(lastBuild == null) {
				String msg_reply = "lastBuild was null.";
				LOGGER.warning(msg_reply);
				return msg_reply;
			}
			if(!lastBuild.isBuilding())
			{
				String changes = getChanges(lastBuild);
				msg.append("\n" + changes);
				msg.append("-------------");
				numBuilds -= 1;
			}
			lastBuild = lastBuild.getPreviousBuild();
		}
		return msg;
    	}
	
	/*
	 * Returns commits for a particular build.
	 */
	public String getChanges(AbstractBuild<?, ?> r) {
		ChangeLogSet<?> commits = r.getChangeSet();
		//return "getChangesCalled";
		if(commits.isEmptySet()) {
			return "No changes this build.\n";
		}
		
		String message = "";
		for (Object o : commits.getItems()) {
			Entry commit = (Entry) o;
			message = "Author:" + commit.getAuthor().getDisplayName() + "\nMessage:" + commit.getMsg() + "\n";
		}

		return message;
	}
	
	@Override
	protected CharSequence getMessageForJob(AbstractProject<?, ?> project) {
		StringBuilder msg = new StringBuilder();
		//msg.append("getMessageForJob");
		return msg;
    }
}

