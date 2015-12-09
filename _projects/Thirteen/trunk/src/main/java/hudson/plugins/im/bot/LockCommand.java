package hudson.plugins.im.bot;

import java.util.Collection;
import java.util.Collections;
import java.util.logging.Logger;

import edu.cs427.groupme.GroupMeStoredData;
import hudson.Extension;
import hudson.plugins.im.IMException;
import hudson.plugins.im.Sender;

/**
 * Returns a list of changed files
 * 
 * @author espaill2 admathu2
 */
@Extension
public class LockCommand extends AbstractTextSendingCommand {
	private static final Logger LOGGER = Logger.getLogger(LockCommand.class.getName());

	private static final String SYNTAX = "lock [time_to_lock_in_seconds]";
	private static final String HELP = SYNTAX + " - schedule a lock for the specificed number of seconds";

	@Override
	/**
	 * Returns commandName
	 */
	public Collection<String> getCommandNames() {
		return Collections.singleton("lock");
	}
	/**
	 * Waits lockTime seconds before unlocking the project
	 * @param bot
	 * @param lockTime
	 * @throws InterruptedException
	 * @throws IMException
	 */
	private void waitAndWake(Bot bot, int lockTime) throws InterruptedException, IMException {
		Thread.sleep(lockTime * 1000);
		if(GroupMeStoredData.getLockedByUsername() != null){
			bot.setSleep(false);
			GroupMeStoredData.setLockedByUsername(null);
		}
			
		
		LOGGER.info("waiting and waking");
	}
	/**
	 * Returns the command reply to lockcommand being called
	 * @param Bot
	 * @param sender
	 * @param args
	 * @return string
	 */
	@Override
	protected String getReply(final Bot bot, Sender sender, String[] args) {
		int lockTime = -1;
		if (args.length > 1) {
			String lockTimeString = args[1];
			try{
				lockTime = Integer.parseInt(lockTimeString);
			}catch (NumberFormatException e){
				return getHelp();
			}
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
					GroupMeStoredData.setLockedByUsername(sender.getNickname());
					LOGGER.info(GroupMeStoredData.getLockedByUsername() + " locked the chatroom.");
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
	/**
	 * returns the help string for the command
	 */
	public String getHelp() {
		return HELP;
	}

}
