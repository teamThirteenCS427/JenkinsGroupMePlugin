package edu.cs427.groupme;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hudson.model.AbstractBuild;
import hudson.model.Result;
import org.junit.Test;

import static hudson.model.Result.ABORTED;
import static hudson.model.Result.FAILURE;
import static hudson.model.Result.NOT_BUILT;
import static hudson.model.Result.SUCCESS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GMBotTest {

	@Test
	public void testResponseCode() {
		
		GroupMeBot bot = new GroupMeBot("test_bot_fricken2","40bL6d4xsBRLt0b3zBrbiXr6v6Fp46Snuu6ybZro","17407658","");
		assertEquals(201, bot.register());
	}
	
	@Test
	public void testSendMessage() {
		
		GroupMeBot bot = new GroupMeBot("test_bot_fricken2","40bL6d4xsBRLt0b3zBrbiXr6v6Fp46Snuu6ybZro","17407658","");
		assertEquals(202, bot.sendTextMessage("GroupMeBot_TEST_MESSAGE"));
	}
}
