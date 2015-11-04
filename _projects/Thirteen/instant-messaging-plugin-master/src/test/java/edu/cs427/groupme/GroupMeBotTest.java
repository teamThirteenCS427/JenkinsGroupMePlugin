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

import org.json.JSONObject;

public class GroupMeBotTest {
	
	
	@Test
	public void testRegister_valid() {
		GroupMeBot.init(MockGroupMeBotConnection.TEST_BOT_NAME, 
				MockGroupMeBotConnection.TEST_GROUP_ID, 
				MockGroupMeBotConnection.TEST_VALID_TOKEN, 
			    new MockGroupMeBotConnection());
		assertNotNull(GroupMeBot.register());
		assertFalse(GroupMeBot.isUnregistered());
	}

	@Test
	public void testRegister_invalid() {
		GroupMeBot.init(MockGroupMeBotConnection.TEST_BOT_NAME, 
				MockGroupMeBotConnection.TEST_GROUP_ID, 
				MockGroupMeBotConnection.TEST_INVALID_TOKEN, 
			    new MockGroupMeBotConnection());
		assertNull(GroupMeBot.register());
		assertTrue(GroupMeBot.isUnregistered());
	}
	
	@Test
	public void testExtractBotId_valid() {
		GroupMeBot.init(MockGroupMeBotConnection.TEST_BOT_NAME, 
				MockGroupMeBotConnection.TEST_GROUP_ID, 
				MockGroupMeBotConnection.TEST_VALID_TOKEN, 
			    new MockGroupMeBotConnection());
		JSONObject testObj = new JSONObject("{\"response\":{\"bot\":{\"bot_id\":\"test_bot_id\"}}}");
		GroupMeBot.extractBotId(testObj);
		assertEquals("test_bot_id", GroupMeBot.botId);
	}
	
	@Test
	public void testExtractBotId_invalid() {
		GroupMeBot.init(MockGroupMeBotConnection.TEST_BOT_NAME, 
				MockGroupMeBotConnection.TEST_GROUP_ID, 
				MockGroupMeBotConnection.TEST_VALID_TOKEN, 
			    new MockGroupMeBotConnection());
		JSONObject testObj = new JSONObject("{\"response\":{\"bot\":{\"bot_id2\":\"test_bot_id\"}}}");
		GroupMeBot.extractBotId(testObj);
		assertEquals("", GroupMeBot.botId);
		GroupMeBot.extractBotId(null);
		assertEquals("", GroupMeBot.botId);
	}
	
	@Test
	public void testSendMessage_valid() {
		GroupMeBot.init(MockGroupMeBotConnection.TEST_BOT_NAME, 
				MockGroupMeBotConnection.TEST_GROUP_ID, 
				MockGroupMeBotConnection.TEST_VALID_TOKEN, 
			    new MockGroupMeBotConnection());
		GroupMeBot.botId = MockGroupMeBotConnection.TEST_VALID_BOTID;
		assertTrue(GroupMeBot.sendTextMessage("message"));
	}

	@Test
	public void testSendMessage_invalid() {
		GroupMeBot.init(MockGroupMeBotConnection.TEST_BOT_NAME, 
				MockGroupMeBotConnection.TEST_GROUP_ID, 
				MockGroupMeBotConnection.TEST_VALID_TOKEN, 
			    new MockGroupMeBotConnection());
		assertFalse(GroupMeBot.sendTextMessage("message"));
		GroupMeBot.botId = MockGroupMeBotConnection.TEST_INVALID_BOTID;
		assertFalse(GroupMeBot.sendTextMessage("message"));
	}
}
