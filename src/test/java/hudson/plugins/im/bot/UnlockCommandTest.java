package hudson.plugins.im.bot;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import edu.cs427.groupme.GroupMeStoredData;
import hudson.plugins.im.IMException;
import hudson.plugins.im.Sender;

public class UnlockCommandTest {
	private Bot mockedBot;
	private Sender sender;
	private String nickname;
	@Before
	public void setUp() throws Exception {
		mockedBot = Mockito.mock(Bot.class);
		sender = Mockito.mock(Sender.class);
	}
	@Test
	public void getReplyWhenBotisAsleepAndCorrectUser() throws IMException{
		nickname = "darko";
		Mockito.when(sender.getNickname()).thenReturn(nickname);
		GroupMeStoredData.setLockedByUsername(nickname);
		UnlockCommand ul = new UnlockCommand();
		Mockito.when(mockedBot.isSleep()).thenReturn(true);
		assertEquals("Going back to work...", ul.getReply(mockedBot, sender, new String[1]));
		Mockito.verify(mockedBot, Mockito.times(1)).setSleep(false);
	}
	
	@Test
	public void getReplyWhenBotisNotAsleepAndCorrectUser() throws IMException{
		nickname = "darko";
		Mockito.when(sender.getNickname()).thenReturn(nickname);
		GroupMeStoredData.setLockedByUsername(nickname);
		UnlockCommand ul = new UnlockCommand();
		Mockito.when(mockedBot.isSleep()).thenReturn(false);
		assertEquals("I was never locked", ul.getReply(mockedBot, sender, new String[1]));
		Mockito.verify(mockedBot, Mockito.times(0)).setSleep(false);
	}
	
	@Test
	public void getReplyWhenBotisAsleepAndIncorrectUser() throws IMException{
		nickname = "darko";
		String unauthorizedNickname = "semih";
		String properResponse = "Hey what do you think you are doing! " + unauthorizedNickname + " locked the chat.";
		Mockito.when(sender.getNickname()).thenReturn(nickname);
		GroupMeStoredData.setLockedByUsername(unauthorizedNickname);
		UnlockCommand ul = new UnlockCommand();
		Mockito.when(mockedBot.isSleep()).thenReturn(true);
		assertEquals(properResponse, ul.getReply(mockedBot, sender, new String[1]));
		Mockito.verify(mockedBot, Mockito.times(0)).setSleep(false);
	}
	
	
	@Test
	public void getReplyWhenBotisAlreadyAwake() throws IMException{
		UnlockCommand ul = new UnlockCommand();
		Mockito.when(mockedBot.isSleep()).thenReturn(false);
		assertEquals("I was never locked", ul.getReply(mockedBot, sender, new String[1]));
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
		Mockito.when(sender.getNickname()).thenReturn(nickname);
		GroupMeStoredData.setLockedByUsername(nickname);
		UnlockCommand ul = new UnlockCommand();
		assertEquals("unlock", ul.getHelp());
	}
}

