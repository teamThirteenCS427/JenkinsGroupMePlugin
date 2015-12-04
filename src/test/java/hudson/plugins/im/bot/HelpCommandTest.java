package hudson.plugins.im.bot;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;

import hudson.plugins.im.IMChat;
import hudson.plugins.im.IMException;
import hudson.plugins.im.bot.Bot.HelpCommand;

public class HelpCommandTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testHelpNoCommands() throws IMException {
		assertTrue(true);
//		Bot bot = mock(Bot.class);
//		IMChat chat = mock(IMChat.class);
//		Mockito.
//		HelpCommand help = new Bot.HelpCommand();
//		help.executeCommand(bot, null, null, null, null);
	}

}
