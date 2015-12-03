package hudson.plugins.im.bot;

import java.util.Collection;
import java.util.Collections;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.plugins.im.Sender;

/**
 * Returns a list of changed files
 * @author espaill2 admathu2
 */
@Extension
public class UnlockCommand extends AbstractTextSendingCommand {

		private static final String HELP = "";

		@Override
	    public Collection<String> getCommandNames() {
	        return Collections.singleton("unlock");
	    }    

		@Override
		protected String getReply(Bot bot, Sender sender, String[] args) {
			String msg = "";
			if(!bot.isSleep()){
				msg += "I was never locked";
			}else{
				bot.setSleep(false);
				msg += "Going back to work...";
			}
			return msg;
		}

		public String getHelp() {
			return HELP;
		}

}