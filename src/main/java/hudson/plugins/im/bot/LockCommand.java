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
import hudson.plugins.im.Sender;
import hudson.plugins.im.tools.MessageHelper;

/**
 * Returns a list of changed files
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

		@Override
		protected String getReply(Bot bot, Sender sender, String[] args) {
			if (args.length >= 1) {
				String lockTime = args[1];
				LOGGER.info("lock time is" + lockTime);
			}
			String msg = "";
			if(bot.isSleep()){
				msg += "I am already asleep...";
			}else{
				bot.setSleep(true);
				msg += "Alright I am going to sleep";
			}
			return msg;
		}

		public String getHelp() {
			return HELP;
		}

}
