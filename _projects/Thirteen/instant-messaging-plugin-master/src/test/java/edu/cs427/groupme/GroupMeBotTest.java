package edu.cs427.groupme;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import hudson.model.AbstractBuild;
import hudson.model.Result;

import org.junit.Before;
import org.junit.Test;

import static hudson.model.Result.ABORTED;
import static hudson.model.Result.FAILURE;
import static hudson.model.Result.NOT_BUILT;
import static hudson.model.Result.SUCCESS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GroupMeBotTest {
	
	
	@Test
	public void testResponseCode1() {
		GroupMeBot.init(MockGroupMeBotConnection.TEST_BOT_NAME, 
				MockGroupMeBotConnection.TEST_GROUP_ID, 
				MockGroupMeBotConnection.TEST_VALID_TOKEN, 
			    new MockGroupMeBotConnection());
		assertNotNull(GroupMeBot.register());
	}

	@Test
	public void testResponseCode2() {
		GroupMeBot.init(MockGroupMeBotConnection.TEST_BOT_NAME, 
				MockGroupMeBotConnection.TEST_GROUP_ID, 
				MockGroupMeBotConnection.TEST_INVALID_TOKEN, 
			    new MockGroupMeBotConnection());
		assertNull(GroupMeBot.register());
	}
	
	@Test
	public void testSendMessage1() {
		assertEquals(202, GroupMeBot.sendTextMessage("GroupMeBot_TEST_MESSAGE1"));
	}

	@Test
	public void testSendMessage2() {
		assertEquals(404, GroupMeBot.sendTextMessage("GroupMeBot_TEST_MESSAGE2"));
	}
}
