package hudson.plugins.im.bot;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import edu.cs427.groupme.GroupMeMessagePolling;
import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.plugins.im.IMException;
import hudson.plugins.im.Sender;
import hudson.plugins.im.tools.MessageHelper;

/**
 * Returns a list of changed files
 * 
 * @author espaill2 admathu2
 */
@Extension
public class LockCommand extends AbstractTextSendingCommand {
	private static final Logger LOGGER = Logger.getLogger(LockCommand.class.getName());

	private static final String SYNTAX = " <job> [time_to_lock_in_seconds]";
	private static final String HELP = SYNTAX + " - schedule a lock for the specificed number of seconds";

	@Override
	public Collection<String> getCommandNames() {
		return Collections.singleton("lock");
	}

	private void waitAndWake(Bot bot, int lockTime) throws InterruptedException, IMException {
		Thread.sleep(lockTime * 1000);
		bot.setSleep(false);
		
		LOGGER.info("waiting and waking");
	}

	@Override
	protected String getReply(final Bot bot, Sender sender, String[] args) {
		int lockTime = -1;
		if (args.length > 1) {
			String lockTimeString = args[1];
			lockTime = Integer.parseInt(lockTimeString);
			LOGGER.info("lock time is " + lockTime);
		}
		String msg = "";
		if (bot.isSleep()) {
			msg += "I am already asleep...";
		} 
		else
			try {
				{
					bot.setSleep(true);
					
					if (lockTime != -1) {
						msg += "Alright I am going to sleep for " + lockTime + " seconds";
						final int finalLockTime = lockTime;
						LOGGER.info("setting up runnable");

						Runnable r = new Runnable() {
							public void run() {
								try {
									LOGGER.info("entering wait and wake");
									waitAndWake(bot, finalLockTime);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IMException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						};
						new Thread(r).start();
						LOGGER.info("after thread");
					} 
					
					else {
						msg += "Alright I am going to sleep";
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return msg;
	}

	public String getHelp() {
		return HELP;
	}

}
