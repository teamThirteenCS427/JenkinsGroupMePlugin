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
	
		StringBuilder msg = new StringBuilder();
		msg.append("log message\n");

        Collection<AbstractProject<?, ?>> projects = new ArrayList<AbstractProject<?, ?>>();

        try {
			getProjects(sender, args, projects);
        }
		catch (CommandException e) {
            return getErrorReply(sender, e);
        }

		LOGGER.warning("projects size: " + projects.size());
		
		
		//if there is a project with that name
        if (!projects.isEmpty()) {
			//For each project with that name
            for (AbstractProject<?, ?> project : projects) {
                msg.append(getLogs(project));
            }
            return msg.toString();
        }
		else {
            return sender + ": no job found";
        }
	}
	/*
	 * For the last build, return logs by calling getChanges.
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
			LOGGER.warning("getChanges--started");
			String changes = getChanges(lastBuild);
			LOGGER.warning("getChanges--completed[" + changes + "]");
        	msg.append("\nLog: " + changes);
		}
    	else
            msg.append("Not finished building yet!");
			
        return msg;
    }
	/*
	 * Returns commits for a particular build.
	 * todo when we call this function it breaks job finding for all commands
	 */
	public String getChanges(AbstractBuild<?, ?> r) {
		ChangeLogSet<?> commits = r.getChangeSet();
		//return "getChangesCalled";
		if(commits.isEmptySet()) {
			LOGGER.warning("changes empty");
			return " no changes";
		}

		Set<String> authors = new HashSet<String>();
		Set<String> messages = new HashSet<String>();
		
		LOGGER.warning("changes not empty" + commits.getItems().length);
		
		String id = ((Entry)(commits.getItems()[0])).getCommitId();
		LOGGER.warning("id:" + id);
	//	for (Object o : commits.getItems()) {
	//		Entry commit = (Entry) o;
			//messages.add(commit.getMsgEscaped());
	//		authors.add(commit.getAuthor());//.getDisplayName());
	//	}

		return id;
	}
	
    @Override
    protected CharSequence getMessageForJob(AbstractProject<?, ?> project) {
		StringBuilder msg = new StringBuilder();
		msg.append("getMessageForJob");
		return msg;
    }
}

