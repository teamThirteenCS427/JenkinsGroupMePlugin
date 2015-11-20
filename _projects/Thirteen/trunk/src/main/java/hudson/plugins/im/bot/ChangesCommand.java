package hudson.plugins.im.bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
public class ChangesCommand extends AbstractMultipleJobCommand {
	@Override
    public Collection<String> getCommandNames() {
        return Collections.singleton("changes");
    }

    private static final String HELP = " changes <job> ";

    @Override
    protected CharSequence getMessageForJob(AbstractProject<?, ?> project) {
        
    	StringBuilder msg = new StringBuilder(32);
        msg.append(project.getFullDisplayName());
        

        if (project.isDisabled()) {
            msg.append("(disabled)");
        } else if (project.isBuilding()) {
            msg.append("(BUILDING: ").append(project.getLastBuild().getDurationString()).append(")");
        } else if (project.isInQueue()) {
            msg.append("(in queue)");
        }
        msg.append(": ");

        AbstractBuild<?, ?> lastBuild = project.getLastBuild();
	
        while ((lastBuild != null) && lastBuild.isBuilding()) {
            lastBuild = lastBuild.getPreviousBuild();
        }
        
        if (lastBuild != null) {
        	ChangeLogSet<?> changeSet = lastBuild.getChangeSet();
        	while(changeSet.isEmptySet()){
			lastBuild = lastBuild.getPreviousBuild();
			if(lastBuild == null){
				return "";
			}
			changeSet = lastBuild.getChangeSet();
        	}
        	Set<AffectedFile> files = new HashSet<AffectedFile>();
        	Set<String> filePaths = new HashSet<String>();
                for (Object o : changeSet.getItems()) {
                	Entry entry = (Entry) o;
        		files.addAll(entry.getAffectedFiles());
                }
         	for(AffectedFile f : files){
        		filePaths.add(f.getPath());
        	}
        	msg.append("\nFiles Changed: "+filePaths.toString());
          } 
    	else {
            msg.append("no finished build yet");
        }
        return msg;
    }
    
    protected CharSequence getMessageForJobWithBuildNum(AbstractProject<?, ?> project, int buildNumber) {
    	StringBuilder msg = new StringBuilder(32);
        msg.append(project.getFullDisplayName());
        

        if (project.isDisabled()) {
            msg.append("(disabled)");
        } else if (project.isBuilding()) {
            msg.append("(BUILDING: ").append(project.getLastBuild().getDurationString()).append(")");
        } else if (project.isInQueue()) {
            msg.append("(in queue)");
        }
        msg.append(": ");

        AbstractBuild<?, ?> lastBuild = project.getBuildByNumber(buildNumber);
    	if(lastBuild == null) {
    		msg.append("no such build exists");
    		return msg;
    	}
	
        while ((lastBuild != null) && lastBuild.isBuilding()) {
            lastBuild = lastBuild.getPreviousBuild();
        }
        
        if (lastBuild != null) {
        	ChangeLogSet<?> changeSet = lastBuild.getChangeSet();
        	while(changeSet.isEmptySet()){
			lastBuild = lastBuild.getPreviousBuild();
			if(lastBuild == null){
				return "";
			}
			changeSet = lastBuild.getChangeSet();
        	}
        	Set<AffectedFile> files = new HashSet<AffectedFile>();
        	Set<String> filePaths = new HashSet<String>();
                for (Object o : changeSet.getItems()) {
                	Entry entry = (Entry) o;
        		files.addAll(entry.getAffectedFiles());
                }
         	for(AffectedFile f : files){
        		filePaths.add(f.getPath());
        	}
        	msg.append("\nFiles Changed: "+filePaths.toString());
          } 
    	else {
            msg.append("no finished build yet");
        }
        return msg;
    }
    
    @Override
	protected String getReply(Bot bot, Sender sender, String[] args) {
    	//if parameters are project and buildNumber
    	int buildNumber = -1;
    	boolean usingBuildNumber = false;
    	if(args.length >= 3) {
    		usingBuildNumber = true;
    		buildNumber = Integer.parseInt(args[2]); //TODO: Make sure it can be parsed
    		args = Arrays.copyOfRange(args, 0, 2);
    	}

        Collection<AbstractProject<?, ?>> projects = new ArrayList<AbstractProject<?, ?>>();

        final Pair<Mode, String> pair;
        try {
            pair = getProjects(sender, args, projects);
        } catch (CommandException e) {
            return getErrorReply(sender, e);
        }

        if (!projects.isEmpty()) {
            StringBuilder msg = new StringBuilder();
                
            switch(pair.getHead()) {
            	case SINGLE : break;
            	case ALL:
            		msg.append(getCommandShortName())
            			.append(" of all projects:\n");
            		break;
            	case VIEW:
            		msg.append(getCommandShortName())
        				.append(" of projects in view " + pair.getTail() + ":\n");
            		break;
            }

            boolean first = true;
            for (AbstractProject<?, ?> project : projects) {
                if (!first) {
                    msg.append("\n");
                } else {
                    first = false;
                }

                if(usingBuildNumber)
                	msg.append(getMessageForJobWithBuildNum(project, buildNumber));
                else
                	msg.append(getMessageForJob(project));
            }
            return msg.toString();
        } else {
            return sender + ": no job found";
        }
	}

    @Override
    protected String getCommandShortName() {
        return "changes";
    }
	public String getHelp() {
		return HELP;
	}

}
