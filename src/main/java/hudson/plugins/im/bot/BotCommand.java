/*
 * Created on Apr 22, 2007
 */
package hudson.plugins.im.bot;

import java.util.Collection;

import hudson.Extension;
import hudson.ExtensionList;
import hudson.ExtensionPoint;
import hudson.plugins.im.IMChat;
import hudson.plugins.im.IMException;
import hudson.plugins.im.IMMessage;
import hudson.plugins.im.Sender;
import jenkins.model.Jenkins;


/**
 * Command pattern contract for IM bot commands.
 *
 * <p>
 * To register custom bot commands, define a subtype and then put @{@link Extension} on your class.
 *
 * @author Pascal Bleser
 * @author Christoph Kutzinski
 * @author Kohsuke Kawaguchi
 * @see Bot
 */
public abstract class BotCommand implements ExtensionPoint {
    /**
     * Obtains the name of the command. Single commands can register multiple aliases,
     * so this method returns a collection.
     *
     * @return
     *      Can be empty but never null.
     */
    public abstract Collection<String> getCommandNames();
	
	/**
	 * Execute a command.
	 * 
	 * @param bot
     *      The bot for which this command runs. Never null.
     * @param chat the {@link IMChat} object, may be used to send reply messages
     * @param message the original {@link IMMessage}
     * @param sender the command sender
     * @param args arguments passed to the command, where <code>args[0]</code> is the command name itself
     * @throws IMException if anything goes wrong while communicating with the remote IM server
	 */
	public abstract void executeCommand(Bot bot, IMChat chat, IMMessage message,
                                        Sender sender, String[] args) throws IMException;
  //This function takes in the necessary values to run any command through the args on the bot
  //It also has options to allow responses to be sent back
	
	
	/**
	 * Return the command usage text.
	 * @return the command usage text
	 */
	public abstract String getHelp();

    /**
     * Returns all the registered {@link BotCommand}s.
     */
    public static ExtensionList<BotCommand> all() {
        return Jenkins.getInstance().getExtensionList(BotCommand.class);
    }    
}
