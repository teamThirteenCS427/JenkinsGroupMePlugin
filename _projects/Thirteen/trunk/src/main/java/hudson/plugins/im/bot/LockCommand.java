package hudson.plugins.im.bot;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

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
	 @Override
	    public Collection<String> getCommandNames() {
	        return Collections.singleton("lock");
	    }

	    private static final String HELP = "";
	    

		@Override
		protected String getReply(Bot bot, Sender sender, String[] args) {
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
