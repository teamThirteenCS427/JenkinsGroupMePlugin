package edu.cs427.groupme;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;
import hudson.tasks.Publisher;

import java.util.Map;
import java.util.logging.Logger;

@Extension//registers this class with jenkins
@SuppressWarnings("rawtypes")
//We listen to events in jenkins, which trigger our functions.
// https://github.com/jenkinsci/jenkins/blob/master/core/src/main/java/hudson/model/listeners/RunListener.java
public class GroupMeBuildListener extends RunListener<AbstractBuild> {

    private static final Logger logger = Logger.getLogger(GroupMeBuildListener.class.getName());

    public GroupMeBuildListener() {
        super(AbstractBuild.class);
    }

    @Override
	//called when a build has completed
    public void onCompleted(AbstractBuild r, TaskListener listener) {
		
    }

    @Override
	//Called when a build is started (i.e. it was in the queue, and will now start running on an executor)
    public void onStarted(AbstractBuild r, TaskListener listener) {
        String messageTest = "Build Started";
		GroupMeBot bot = new GroupMeBot("test_bot_pzhao12","40bL6d4xsBRLt0b3zBrbiXr6v6Fp46Snuu6ybZro","17407658","");
		bot.register();
		bot.sendTextMessage(messageTest);
    }

    @Override
	//Called right before a build is going to be deleted.
    public void onDeleted(AbstractBuild r) {
        
    }

    @Override
	//Called after a build is moved to the Run.State.COMPLETED state.
	//At this point, all the records related to a build is written down to the disk.
	//As such, TaskListener is no longer available. This happens later than onCompleted(Run, TaskListener).
    public void onFinalized(AbstractBuild r) {
		
    }
}
