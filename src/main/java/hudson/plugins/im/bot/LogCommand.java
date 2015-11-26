package hudson.plugins.im.bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import java.util.logging.Logger;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.plugins.im.Sender;
import hudson.plugins.im.bot.AbstractMultipleJobCommand.Mode;
import hudson.plugins.im.tools.Pair;
import hudson.scm.ChangeLogSet;
import hudson.scm.ChangeLogSet.AffectedFile;
import hudson.scm.ChangeLogSet.Entry;


/**
 * Returns a list of changed files
 * @author pzhao12 admathu2
 */
@Extension
public class LogCommand extends AbstractMultipleJobCommand {

	private static final Logger LOGGER = Logger.getLogger(LogCommand.class.getName());
    private static final String HELP = "log <job> - Displays recent svn log for that job";
	

	@Override
    public Collection<String> getCommandNames() {
        return Collections.singleton("log");
    }

    @Override
    protected String getCommandShortName() {
        return "log";
    }

	/* returns string to display when viewing help
	 */
    @Override
	public String getHelp() {
		return HELP;
	}
    
	/* Called when someone tried to see a log via !log jobname
	 */
    @Override
	public String getReply(Bot bot, Sender sender, String[] args) {
		StringBuilder msg = new StringBuilder();
		msg.append("log");
		return msg.toString();
	
    	//if parameters are project and buildNumber
		/*
    	int buildNumber = -1;
    	boolean usingBuildNumber = false;
    	if(args.length >= 3) {
    		usingBuildNumber = true;
    		try {
    			buildNumber = Integer.parseInt(args[2]);
    		}
    		catch (NumberFormatException nfe) {
    			return "Format is not correct for the build number parameter";
    		}
    		args = Arrays.copyOfRange(args, 0, 2); 
    	}

        Collection<AbstractProject<?, ?>> projects = new ArrayList<AbstractProject<?, ?>>();

        try {
			getProjects(sender, args, projects);
        }
		catch (CommandException e) {
            return getErrorReply(sender, e);
        }

		//if there is a project with that name
        if (!projects.isEmpty()) {
            StringBuilder msg = new StringBuilder();
			
			//For each project
            for (AbstractProject<?, ?> project : projects) {
                msg.append(getLogs(project));
            }
            return msg.toString();
        }
		else {
            return sender + ": no job found";
        }
		*/
	}
	
	/*returns a string of the last n commit messages for a project
	 */
    protected CharSequence getLogs(AbstractProject<?, ?> project) {
        
    	StringBuilder msg = new StringBuilder(32);
        msg.append(project.getFullDisplayName());

		//Get the last build
        AbstractBuild<?, ?> lastBuild = project.getLastBuild();
        while ((lastBuild != null) && lastBuild.isBuilding()) {
            lastBuild = lastBuild.getPreviousBuild();
        }

		//Get data from last build
        if (lastBuild != null) {
        	msg.append("\nLog: " + getChanges(lastBuild));
		}
    	else {
            msg.append("Not finished building yet!");
        }
        return msg;
    }
	
    @Override
    protected CharSequence getMessageForJob(AbstractProject<?, ?> project) {
		StringBuilder msg = new StringBuilder();
		msg.append("getMessageForJob");
		return msg;
    }
	
	public String getChanges(AbstractBuild r) {
		ChangeLogSet changeSet = r.getChangeSet();
		if(changeSet.isEmptySet()){
			return "";
		}
		Set<String> authors = new HashSet<String>();
		Set<String> commitMsgs = new HashSet<String>();
		
		for (Object o : changeSet.getItems()) {
			Entry entry = (Entry) o;
			commitMsgs.add(entry.getMsgEscaped());
			authors.add(entry.getAuthor().getDisplayName());
		}

		return "Author: "+ authors.toString() + "\nCommit Msg: " + commitMsgs.toString();
	}
}

