package hudson.plugins.im.bot;

import java.util.Collection;
import java.util.Collections;

import edu.cs427.groupme.GroupMeStoredData;
import hudson.Extension;
import hudson.plugins.im.IMException;
import hudson.plugins.im.Sender;

/**
 * Returns a list of changed files
 * @author espaill2 admathu2
 */
@Extension
public class UnlockCommand extends AbstractTextSendingCommand {

		private static final String HELP = "unlock";

		@Override
	    public Collection<String> getCommandNames() {
	        return Collections.singleton("unlock");
	    }    

		@Override
		protected String getReply(Bot bot, Sender sender, String[] args) {
			String msg = "";
			String senderNick = sender.getNickname();
			String authorizedNick = GroupMeStoredData.getLockedByUsername();
			if(!bot.isSleep()){
				msg += "I was never locked";
			}else if(!senderNick.equals(authorizedNick)){
				msg += "Hey what do you think you are doing! " + authorizedNick + " locked the chat.";
			}else{
				try {
					bot.setSleep(false);
					GroupMeStoredData.setLockedByUsername(null);
				} catch (IMException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				msg += "Going back to work...";
			}
			return msg;
		}

		public String getHelp() {
			return HELP;
		}

}
