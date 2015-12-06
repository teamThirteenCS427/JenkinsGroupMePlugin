package hudson.plugins.im.bot;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import hudson.plugins.im.IMException;
import hudson.plugins.im.Sender;

public class UnlockCommandTest {
	private Bot mockedBot;
	private Sender sender;
	@Before
	public void setUp() throws Exception {
		mockedBot = Mockito.mock(Bot.class);
		sender = Mockito.mock(Sender.class);
	}
	@Test
	public void getReplyWhenBotisAsleep() throws IMException{
		UnlockCommand ul = new UnlockCommand();
		//Mockito.when(mockedBot.isSleep()).thenReturn(false);
		assertEquals(ul.getReply(mockedBot, sender, new String[1]), "Going back to work...");
		Mockito.verify(mockedBot, Mockito.times(1)).setSleep(false);
	}
	
	@Test
	public void getReplyWhenBotisAlreadyAwake() throws IMException{
		UnlockCommand ul = new UnlockCommand();
		Mockito.when(mockedBot.isSleep()).thenReturn(false);
		assertEquals(ul.getReply(mockedBot, sender, new String[1]), "I was never locked");
	}
	
	@Test
	public void verifyCorrectCommandName(){
		UnlockCommand ul = new UnlockCommand();
		Collection<String> s = ul.getCommandNames();
		assertEquals(s.size(), 1);
		assert(s.contains("unlock"));
	}
	
	@Test
	public void verifyGetHelpString(){
		UnlockCommand ul = new UnlockCommand();
		assertEquals(ul.getHelp(), "unlock");
	}
}

