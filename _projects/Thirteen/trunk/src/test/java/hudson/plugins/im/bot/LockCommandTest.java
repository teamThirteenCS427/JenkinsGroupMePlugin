package hudson.plugins.im.bot;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import edu.cs427.groupme.GroupMeStoredData;
import hudson.plugins.im.IMException;
import hudson.plugins.im.Sender;

public class LockCommandTest {
	private Bot mockedBot;
	private Sender sender;
	@Before
	public void setUp() throws Exception {
		mockedBot = Mockito.mock(Bot.class);
		sender = Mockito.mock(Sender.class);
		GroupMeStoredData gmsd = new GroupMeStoredData();
	}
	@Test
	public void getReplyWithNoArgumentsLocksBotIndefinitely() throws IMException{
		LockCommand lc = new LockCommand();
		Mockito.when(mockedBot.isSleep()).thenReturn(false);
		assertEquals(lc.getReply(mockedBot, sender, new String[1]), "Alright I am going to sleep");
		Mockito.verify(mockedBot, Mockito.times(1)).setSleep(true);
	}
	
	@Test
	public void getReplyWithNoArgumentsLocksBotForSpecifiedTime() throws IMException{
		LockCommand lc = new LockCommand();
		Mockito.when(mockedBot.isSleep()).thenReturn(false);
		String[] args = new String[2];
		args[1] = "5";
		assertEquals(lc.getReply(mockedBot, sender, args), "Alright I am going to sleep for 5 seconds");
		Mockito.verify(mockedBot, Mockito.times(1)).setSleep(true);
	}
	
	@Test
	public void getReplyWithNonsenseIntegerReturnsHelp() throws IMException{
		LockCommand lc = new LockCommand();
		Mockito.when(mockedBot.isSleep()).thenReturn(false);
		String[] args = new String[2];
		args[1] = "fsadf";
		assertEquals(lc.getReply(mockedBot, sender, args), "lock [time_to_lock_in_seconds] - schedule a lock for the specificed number of seconds");
		Mockito.verify(mockedBot, Mockito.times(0)).setSleep(true);
	}
	@Test
	public void verifyCorrectCommandName(){
		LockCommand lc = new LockCommand();
		Collection<String> s = lc.getCommandNames();
		assertEquals(s.size(), 1);
		assert(s.contains("lock"));
	}
	
	@Test
	public void verifyGetHelpString(){
		LockCommand lc = new LockCommand();
		assertEquals(lc.getHelp(), "lock [time_to_lock_in_seconds] - schedule a lock for the specificed number of seconds");
	}
}

