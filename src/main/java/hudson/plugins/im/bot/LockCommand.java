package hudson.plugins.im.bot;

import java.util.Collection;
import java.util.Collections;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;

/**
 * Returns a list of changed files
 * @author espaill2 admathu2
 */
@Extension
public class LockCommand extends AbstractMultipleJobCommand {
	@Override
    public Collection<String> getCommandNames() {
        return Collections.singleton("lock");
    }

    private static final String HELP = " lock <job> ";

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
        
        //TODO: Implement Lock command
        if (lastBuild != null) {
        	
        	msg.append("Lock command has not been implemented yet. Sorry!");
          } 
    	else {
            msg.append("no finished build yet");
        }
        return msg;
    }
    
    

    @Override
    protected String getCommandShortName() {
        return "lock";
    }
	public String getHelp() {
		return HELP;
	}

}
