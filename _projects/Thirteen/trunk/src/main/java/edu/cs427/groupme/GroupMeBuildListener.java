package edu.cs427.groupme;

import hudson.Extension;
import hudson.model.*;
import hudson.model.listeners.RunListener;
import hudson.tasks.Publisher;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Implementation of GroupMeBuildListener
 * Overrides functions on the RunListener which act as triggers for a build.
 * So when a build starts, these functions get called.
 * 
 * @author fricken2 pzhao12
 */

@Extension//registers this class with jenkins
@SuppressWarnings("rawtypes")
public class GroupMeBuildListener extends RunListener<AbstractBuild> {

    private static final Logger logger = Logger.getLogger(GroupMeBuildListener.class.getName());

    public GroupMeBuildListener() {
        super(AbstractBuild.class);
    }

	/**
	 * Called when a build completes.
	 *
	 * @author fricken2 pzhao12
	 * @param r - The build (which is now completed)
	 * @param listener - Used to produce log messages which show up in console output.
	 */
    @Override
    public void onCompleted(AbstractBuild r, TaskListener listener) {
		String taskName = r.getProject().getDisplayName();
		GroupMeIMConnection.registerGroupMeBot();
		Result result = r.getResult();
		GroupMeBot.sendTextMessage(taskName + " build " + result.toString() + ".");		
    }
	
	/**
	 * Called when a build starts.
	 *
	 * @author fricken2 pzhao12
	 * @param r - The build (which is now completed)
	 * @param listener - Used to produce log messages which show up in console output.
	 */
    @Override
    public void onStarted(AbstractBuild r, TaskListener listener) {
		String taskName = r.getProject().getDisplayName();
		String buildNum = r.getDisplayName();
		GroupMeIMConnection.registerGroupMeBot();
		CauseAction cause = r.getAction(CauseAction.class);
		String causeString = "";
		if(cause != null)
			causeString = cause.getShortDescription();
			
		GroupMeBot.sendTextMessage(taskName + " - "+ buildNum + " build started." + causeString);
    }

	/**
	 * Called right before a build is going to be deleted.
	 *
	 * @author fricken2 pzhao12
	 * @param r - The build (which is now completed)
	 */
    @Override
    public void onDeleted(AbstractBuild r) {
		String taskName = r.getProject().getDisplayName();
		GroupMeIMConnection.registerGroupMeBot();
		GroupMeBot.sendTextMessage(taskName + " build cancelled.");
    }

	/**
	 * Called after a build is moved to the Run.State.COMPLETED state.
	 * At this point, all the records related to a build are written down to the disk.
	 * As such, TaskListener is no longer available. This happens later than onCompleted(Run, TaskListener).
	 *
	 * @author fricken2 pzhao12
	 * @param r - The build (which is now completed)
	 */
    @Override
    public void onFinalized(AbstractBuild r) {
		
    }
}
