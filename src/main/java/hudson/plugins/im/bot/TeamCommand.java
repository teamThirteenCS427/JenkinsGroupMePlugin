package hudson.plugins.im.bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.HashMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
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
 * Returns a representation of who has committed recently
 * @author fricken2 aymei2
 */

@Extension
public class TeamCommand extends AbstractMultipleJobCommand {

    private static final Logger LOGGER = Logger.getLogger(LogCommand.class.getName());
    private static final String HELP = "team <job> <int>- Displays a count of the most recent (2nd parameter) commits by person for said job";
	private static HashMap<String, Integer> authMap = new HashMap<String, Integer>();

    @Override
    public Collection<String> getCommandNames() {
        return Collections.singleton("team");
    }

    @Override
    protected String getCommandShortName() {
        return "team";
    }

    /*
    * Returns string to display when viewing help
    */
    @Override
	public String getHelp() {
		return HELP;
	}


	/*
	 * Called when someone tried to see a log via !team jobname
	 */
	@Override
	public String getReply(Bot bot, Sender sender, String[] args) {
		authMap.clear();
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
		
		if(numBuilds >= 50 || numBuilds <= 0)
			numBuilds = 50;
				
		return getCommitCaller(sender, args, numBuilds);

	}
	
	/*
	 * Called from getReply -- will call getCommits when appropriate
	 * Doesn't need the bot as it is only ever called as a parameter in getReply
	 */
	private String getCommitCaller(Sender sender, String[] args, int numBuilds){

		StringBuilder msg = new StringBuilder();
        Collection<AbstractProject<?, ?>> projects = new ArrayList<AbstractProject<?, ?>>();

        try {
			getProjects(sender, args, projects);
        }
		catch (CommandException e) {
			return getErrorReply(sender, e);
        }
		
		//if there is a project with that name
        if (!projects.isEmpty()) {
			//For each project with that name
			for (AbstractProject<?, ?> project : projects) {
				getCommits(project, numBuilds);
			}
			
			for(Map.Entry<String, Integer> entry : authMap.entrySet()) {
				String key = entry.getKey().toString();
				Integer value = entry.getValue();
				msg.append("Author: " + key + "\t\t" + "Commits: " + value + "\n");
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
	protected void getCommits(AbstractProject<?, ?> project, int numBuilds) {
		//Get the last build
        AbstractBuild<?, ?> lastBuild = project.getLastBuild();
        while(numBuilds > 0) {
			//Get data from last build
			if(lastBuild == null) {
				LOGGER.warning("lastBuild was null.");
				return;
			}
			if(!lastBuild.isBuilding()) {
				getChanges(lastBuild);
				numBuilds -= 1;
			}
			lastBuild = lastBuild.getPreviousBuild();
		}
    }

	/*
	 * Returns commits for a particular build.
	 * todo when we call this function it breaks job finding for all commands
	 */
	public void getChanges(AbstractBuild<?, ?> r) {
		ChangeLogSet<?> commits = r.getChangeSet();
		for (Object o : commits.getItems()) {
			Entry commit = (Entry) o;
			String name = commit.getAuthor().getDisplayName();
			if(authMap.containsKey(name))
				authMap.put(name, authMap.get(name) + 1);
			else
				authMap.put(name, 1);
		}
	}
	
    @Override
    protected CharSequence getMessageForJob(AbstractProject<?, ?> project) {
		StringBuilder msg = new StringBuilder();
		// msg.append("getMessageForJob");
		return msg;
    }
}

