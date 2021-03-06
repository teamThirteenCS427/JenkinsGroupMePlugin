package edu.cs427.groupme;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.CauseAction;
import hudson.model.Result;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;
import hudson.scm.ChangeLogSet;
import hudson.scm.ChangeLogSet.AffectedFile;
import hudson.scm.ChangeLogSet.Entry;

/**
 * Implementation of GroupMeBuildListener Overrides functions on the RunListener
 * which act as triggers for a build. So when a build starts, these functions
 * get called.
 * 
 * @author fricken2 pzhao12
 */

@Extension // registers this class with jenkins
@SuppressWarnings("rawtypes")
public class GroupMeBuildListener extends RunListener<AbstractBuild> {

	private static final Logger logger = Logger.getLogger(GroupMeBuildListener.class.getName());

	public GroupMeBuildListener() {
		super(AbstractBuild.class);
	}
	
	private String getInfo(AbstractBuild r) {
		return r.getProject().getDisplayName() + " - " + r.getDisplayName() + " build ";
	}
	
	/**
	 * Called when a build completes.
	 *
	 * @author fricken2 pzhao12
	 * @param r
	 *            - The build (which is now completed)
	 * @param listener
	 *            - Used to produce log messages which show up in console
	 *            output.
	 */
	@Override
	public void onCompleted(AbstractBuild r, TaskListener listener) {
		String buildDuration = r.getDurationString();
		Result result = r.getResult();
		GroupMeIMConnection.registerGroupMeBot();
		String changeString = getChanges(r);
		GroupMeBot.sendTextMessage(changeString);
		GroupMeBot.sendTextMessage(getInfo(r) + result.toString() + " after " + buildDuration);
	}

	/**
	 * Called when a build starts.
	 *
	 * @author fricken2 pzhao12
	 * @param r
	 *            - The build (which is now completed)
	 * @param listener
	 *            - Used to produce log messages which show up in console
	 *            output.
	 */
	@Override
	public void onStarted(AbstractBuild r, TaskListener listener) {
		AbstractProject<?, ?> me = r.getProject();
		AbstractProject<?, ?> root = r.getProject().getRootProject();
		if (me == root) {// only message about the root project
			CauseAction cause = r.getAction(CauseAction.class);
			String causeString = "";
			if (cause != null)
				causeString = cause.getShortDescription();
			GroupMeIMConnection.registerGroupMeBot();
			GroupMeBot.sendTextMessage(getInfo(r) + causeString);
		}
	}

	/**
	 * Called right before a build is going to be deleted.
	 *
	 * @author fricken2 pzhao12
	 * @param r
	 *            - The build (which is now completed)
	 */
	@Override
	public void onDeleted(AbstractBuild r) {
		String taskName = r.getProject().getDisplayName();
		GroupMeIMConnection.registerGroupMeBot();
		GroupMeBot.sendTextMessage(taskName + " build cancelled.");
	}

	/**
	 * Called after a build is moved to the Run.State.COMPLETED state. At this
	 * point, all the records related to a build are written down to the disk.
	 * As such, TaskListener is no longer available. This happens later than
	 * onCompleted(Run, TaskListener).
	 *
	 * @author fricken2 pzhao12
	 * @param r
	 *            - The build (which is now completed)
	 */
	@Override
	public void onFinalized(AbstractBuild r) {

	}

	private String getChanges(AbstractBuild r) {
		ChangeLogSet changeSet = r.getChangeSet();
		if (changeSet.isEmptySet()) {
			return "";
		}
		Set<String> authors = new HashSet<String>();
		Set<String> commitMsgs = new HashSet<String>();
		Set<AffectedFile> files = new HashSet<AffectedFile>();
		Set<String> filePaths = new HashSet<String>();
		for (Object o : changeSet.getItems()) {
			Entry entry = (Entry) o;
			commitMsgs.add(entry.getMsgEscaped());
			authors.add(entry.getAuthor().getDisplayName());
			files.addAll(entry.getAffectedFiles());
		}
		for (AffectedFile f : files) {
			filePaths.add(f.getPath());
		}
		return "Author: " + authors.toString() + "\nCommit Msg: " + commitMsgs.toString() + "\nFile Changed: "
				+ filePaths.toString();
	}
}
