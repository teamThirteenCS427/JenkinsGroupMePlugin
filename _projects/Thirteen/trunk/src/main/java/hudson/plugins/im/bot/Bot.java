/*
 * Created on Apr 22, 2007
 */
package hudson.plugins.im.bot;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

import edu.cs427.groupme.GroupMeStoredData;
import hudson.Extension;
import hudson.plugins.im.AuthenticationHolder;
import hudson.plugins.im.IMChat;
import hudson.plugins.im.IMException;
import hudson.plugins.im.IMMessage;
import hudson.plugins.im.IMMessageListener;
import hudson.plugins.im.Sender;
import hudson.plugins.im.bot.SetAliasCommand.AliasCommand;
import hudson.plugins.im.tools.ExceptionHelper;
import hudson.plugins.im.tools.MessageHelper;
import hudson.security.ACL;
import jenkins.model.Jenkins;
import jenkins.security.NotReallyRoleSensitiveCallable;

/**
 * Instant messaging bot.
 * 
 * @author Pascal Bleser
 * @author kutzi
 */
public class Bot implements IMMessageListener {

	private static final Logger LOGGER = Logger.getLogger(Bot.class.getName());

    @Extension
	public static class HelpCommand extends BotCommand {
    	/**
    	  * {@inheritDoc}
    	  */
        @Override
        public Collection<String> getCommandNames() {
            return Collections.singleton("help");
        }

        /**
	   	  * {@inheritDoc}
	   	  */
        @Override
        public void executeCommand(Bot bot, IMChat chat, IMMessage message,
                                   Sender sender, String[] args) throws IMException {
			LOGGER.warning("executing command help");
			StringBuilder msg = generateHelpString(bot);
			chat.sendMessage(msg.toString());
		}

        /**
         * Return null since we do not want to give help regarding the help command
         */
		public String getHelp() {
			return null;
		}
		
		private StringBuilder generateHelpString(Bot bot) {
			StringBuilder msg = new StringBuilder("Available commands:");
			
			HashSet<BotCommand> commandSet = new HashSet(bot.cmdsAndAliases.values());
			HashSet<String> outputStringSet = new HashSet();
			for(BotCommand b: commandSet){
				
				if(b.getHelp()!=null){
					outputStringSet.add(b.getHelp());
				}
			}
			for(String s:outputStringSet){
				msg.append("\n");
				msg.append(s);
			}
			return msg;
		}
	}

	private final SortedMap<String, BotCommand> cmdsAndAliases = new TreeMap<String, BotCommand>();

	private final IMChat chat;
	private final String nick;
	private final String imServer;
	private final String commandPrefix;
	private boolean commandsAccepted;
	private String helpCache = null;
	private boolean sleep;

	private final AuthenticationHolder authentication;

	/**
	 * Constructs the new Bot instance and generates command list
	 * @param chat the IMChat to interact with
	 * @param nick the nickname
	 * @param imServer The string for the IM server
	 * @param commandPrefix Prefix expected for bot commands
	 * @param authentication  Authentication level
	 */
	public Bot(IMChat chat, String nick, String imServer,
			String commandPrefix, AuthenticationHolder authentication
			) {
		this.chat = chat;
		this.nick = nick;
		this.imServer = imServer;
		this.commandPrefix = commandPrefix;
		this.authentication = authentication;
        this.commandsAccepted = chat.isCommandsAccepted();
        this.sleep = false;
        
        for (BotCommand cmd : BotCommand.all()) {
            for (String name : cmd.getCommandNames())
                this.cmdsAndAliases.put(name,cmd);
        }

		chat.addMessageListener(this);
	}

    /**
     * Returns an identifier describing the Im account used to send the build command.
     *   E.g. the Jabber ID of the Bot.
     */
    public String getImId() {
        return this.nick;
    }

    /**
     * {@inheritDoc}
     */
    public void onMessage(final IMMessage msg) {
    	for (BotCommand cmd : BotCommand.all()) {
            for (String name : cmd.getCommandNames())
                this.cmdsAndAliases.put(name,cmd);
        }
        // is it a command for me ? (returns null if not, the payload if so)
        String payload = retrieveMessagePayLoad(msg.getBody());
        if (payload != null) {
            final Sender s = getSender(msg);
        	LOGGER.info(s.getId() + " ----- " + s.getNickname());
        	try {
            	if (!this.commandsAccepted) {
            	    this.chat.sendMessage(s.getNickname() + " you may not issue bot commands in this chat!");
            	    return;
            	} else if (!msg.isAuthorized()) {
    				this.chat.sendMessage(s.getNickname() + " you're not a buddy of me. I won't take any commands from you!");
    				return;
            	}
        	} catch (IMException e) {
                LOGGER.warning(ExceptionHelper.dump(e));
                return;
            }
        	
            // split words
            final String[] args = MessageHelper.extractCommandLine(payload);
            if (args.length > 0) {
                // first word is the command name
                String cmd = args[0];
                
                try {
                	final BotCommand command = this.cmdsAndAliases.get(cmd);
                    if (command != null) {
                    	if (isAuthenticationNeeded()) {
                    		ACL.impersonate(this.authentication.getAuthentication(), new NotReallyRoleSensitiveCallable<Void, IMException>() {
								private static final long serialVersionUID = 1L;

								@Override
								public Void call() throws IMException {
									executeCommand(msg, s, args, command);
									return null;
								}
							});
                    	} else {
                    		executeCommand(msg, s, args, command);
                    	}
                    } else {
                        this.chat.sendMessage(s.getNickname() + " did you mean me? Unknown command '" + cmd
                                + "'\nUse '" + this.commandPrefix + " help' to get help!");
                    }
                } catch (Exception e) {
                    LOGGER.warning(ExceptionHelper.dump(e));
                }
            }
        }
	}
    
    private boolean isAuthenticationNeeded() {
		LOGGER.warning("authentication = " + (this.authentication != null));
		LOGGER.warning("Jenkins is secured? " + Jenkins.getInstance().isUseSecurity());
    	return this.authentication != null && Jenkins.getInstance().isUseSecurity();
    }

	private Sender getSender(IMMessage msg) {
	    String sender = msg.getFrom();
	    LOGGER.info("Sender of message = " + sender);
	    String id = this.chat.getIMId(sender);
        
        final Sender s;
        if (id != null) {
            s = new Sender(sender, id);
        } else {
            s = new Sender(sender);
        }
        return s;
    }

    private static boolean isNickSeparator(final String candidate) {
		return ":".equals(candidate) || ",".equals(candidate);
	}
    
    /**
     * 
     * @return if the bot is sleeping
     */
	public boolean isSleep() {
		return sleep;
	}

	/**
	 * Make the Bot sleep or wake up
	 * @param sleep  boolean representing whether the bot should sleep or not
	 * @throws IMException
	 */
	public void setSleep(boolean sleep) throws IMException {
		if(this.sleep && !sleep) chat.sendMessage("I'm up...");
		if(!this.sleep && sleep) chat.sendMessage("zzz..");

		this.sleep = sleep;
	}

	private String retrieveMessagePayLoad(final String body) {
		if (body == null) {
			return null;
		}

		if (body.startsWith(this.commandPrefix)) {
			return body.substring(this.commandPrefix.length()).trim();
		}

		if (body.startsWith(this.nick)
				&& isNickSeparator(body.substring(this.nick.length(), this.nick
						.length() + 1))) {
			return body.substring(this.nick.length() + 1).trim();
		}

		return null;
	}
	
	/**
	 * Returns the command or alias associated with the given name
	 * or <code>null</code>.
	 */
	BotCommand getCommand(String name) {
		return this.cmdsAndAliases.get(name);
	}
	
	/**
	 * Registers a new alias.
	 * 
	 * @return the alias previously registered under this name or <code>null</code>
	 * if no alias was registered by that name previously
	 * @throws IllegalArgumentException when trying to override a built-in command
	 */
	BotCommand addAlias(String name, BotCommand alias) {
		BotCommand old = this.cmdsAndAliases.get(name);
		if (old != null && ! (old instanceof AliasCommand)) {
			throw new IllegalArgumentException("Won't override built-in command: '" + name + "'!");
		}
		
		this.cmdsAndAliases.put(name, alias);
		this.helpCache = null;
		return old;
	}
	
	/**
	 * Removes an existing alias.
	 *
	 * @param name The name of the alias
	 * @return the removed alias or <code>null</code> if no alias by that name is registered
	 */
	AliasCommand removeAlias(String name) {
		BotCommand alias = this.cmdsAndAliases.get(name);
		if (alias instanceof AliasCommand) {
			this.cmdsAndAliases.remove(name);
			return (AliasCommand) alias;
		} else if (alias != null) {
			throw new IllegalArgumentException("Won't remove built-in command: '" + name + "'!");
		}
		return null;
	}
	
	/**
	 * Returns a map of all currently defined aliases.
	 * The map is sorted by the alias name.
	 */
	SortedMap<String, AliasCommand> getAliases() {
		SortedMap<String, AliasCommand> result = new TreeMap<String, AliasCommand>();
		for (Map.Entry<String, BotCommand> entry : this.cmdsAndAliases.entrySet()) {
			if (entry.getValue() instanceof AliasCommand) {
				result.put(entry.getKey(), (AliasCommand) entry.getValue());
			}
		}
		return result;
	}

	/**
	 * Called on Jenkins shutdown.
	 */
	public void shutdown() {
		this.chat.removeMessageListener(this);
		
		if (this.chat.isMultiUserChat()) {
			try {
				chat.sendMessage("Oops, seems like Jenkins is going down now. See ya!");
			} catch (IMException e) {
				// ignore
			}
		}
	}

	private void executeCommand(final IMMessage msg, final Sender s, final String[] args, final BotCommand command)
			throws IMException {
		LOGGER.info("command is the unlock command? " + (command instanceof UnlockCommand));
		LOGGER.info("sleep? " + (this.sleep));
		
		if(this.sleep){
			if(command.getCommandNames().toString().equals("[unlock]")){
				command.executeCommand(Bot.this, chat, msg, s, args);
			}else{
				String lockedBy =  GroupMeStoredData.getLockedByUsername();
				String senderNick = s.getNickname();
				LOGGER.info("Currently locked by: " + lockedBy + ". " + senderNick + " wants to execute command.");
				if(senderNick.equals(lockedBy)){
					chat.sendMessage("Hey " + senderNick + " you should probably unlock the chatroom. Remember you told me to put this chatroom on lockdown.");
				}else{
					chat.sendMessage("I am currently locked. Ask " + lockedBy  + " to let me get back to work.");
				}
				LOGGER.info("won't execute command, currently sleeping " + command.getCommandNames().toString());
			}
		} else {
			command.executeCommand(Bot.this, chat, msg, s, args);
		}
			
	}
}
