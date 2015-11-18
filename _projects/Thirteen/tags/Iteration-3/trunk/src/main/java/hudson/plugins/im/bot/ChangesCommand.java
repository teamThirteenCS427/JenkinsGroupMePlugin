package hudson.plugins.im.bot;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
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
        	if(changeSet.isEmptySet())
        		return "";
        	
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
    protected String getCommandShortName() {
        return "changes";
    }
	public String getHelp() {
		return HELP;
	}

}