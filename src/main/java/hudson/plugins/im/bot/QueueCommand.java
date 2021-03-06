/*
 * Created on Apr 22, 2007
 */
package hudson.plugins.im.bot;

import java.util.Arrays;
import java.util.Collection;

import hudson.Extension;
import hudson.model.Hudson;
import hudson.model.Queue;
import hudson.model.Queue.Item;
import hudson.plugins.im.IMChat;
import hudson.plugins.im.IMException;
import hudson.plugins.im.IMMessage;
import hudson.plugins.im.Sender;

/**
 * Queue command for the jabber bot.
 * @author Pascal Bleser
 */
@Extension
public class QueueCommand extends BotCommand {
	
	private static final String HELP = "queue - show the state of the build queue";

    @Override
    public Collection<String> getCommandNames() {
        return Arrays.asList("queue","q");
    }

    public void executeCommand(Bot bot, IMChat chat, IMMessage message,
                               Sender sender, String[] args) throws IMException {
		Queue queue = Hudson.getInstance().getQueue();
		Item[] items = queue.getItems();
		String reply;
		if (items.length > 0) {
			StringBuffer msg = new StringBuffer();
			msg.append("Build queue:");
			for (Item item : queue.getItems()) {
				msg.append("\n- ")
				.append(item.task.getFullDisplayName())
				.append(": ").append(item.getWhy());
			}
			reply = msg.toString();
		} else {
			reply = "build queue is empty";
		}
		
		chat.sendMessage(reply);
		
		System.out.println("Executes the message in sends it to the chat. Variables such as bot and sender are not used within the funciton");
	}

	public String getHelp() {
		return HELP;
	}

}
