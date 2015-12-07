package edu.cs427.groupme;

import static org.mockito.Matchers.any;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import edu.cs427.groupme.GroupMeBot;
import edu.cs427.groupme.GroupMeBotConnection;

public class GroupMeBotTest {
	
	private GroupMeBotConnection mockedConn;
	
	@Before
	public void setup() throws Exception{
		mockedConn = Mockito.mock(GroupMeBotConnection.class);
	}
	
	@Test
	public void testRegister_validResponse() {
		GroupMeBot.init("botname", "token", "groupid", new GroupMeBotConnection("token"));
		JSONObject mockObject = null;
		try {
			mockObject = (JSONObject) new JSONParser().parse("{\"response\":{\"bot\":{\"bot_id\":\"" + "botid" + "\"}}}");
		} catch (ParseException e) {
			fail();
		}
		Mockito.when(mockedConn.register(Matchers.anyString(), Matchers.anyString(), Matchers.anyString())).thenReturn(mockObject);
		assertTrue(GroupMeBot.register());
		assertFalse(GroupMeBot.isUnregistered());
	}

	/*@Test
	public void testRegister_valid() {
		GroupMeBot.init(MockGroupMeBotConnection.TEST_BOT_NAME, MockGroupMeBotConnection.TEST_VALID_TOKEN,
				MockGroupMeBotConnection.TEST_GROUP_ID, new MockGroupMeBotConnection());
		assertTrue(GroupMeBot.register());
		assertFalse(GroupMeBot.isUnregistered());
	}*/

	@Test
	public void testRegister_invalidResponse() {
		Mockito.when(mockedConn.register(Matchers.anyString(), Matchers.anyString(), Matchers.anyString())).thenReturn(null);
		GroupMeBot.init("botname", "token", "groupid", new GroupMeBotConnection("token"));
		assertFalse(GroupMeBot.register());
		assertTrue(GroupMeBot.isUnregistered());
	}

	@Test
	public void testExtractBotId_valid() {
		GroupMeBot.init(MockGroupMeBotConnection.TEST_BOT_NAME, MockGroupMeBotConnection.TEST_VALID_TOKEN,
				MockGroupMeBotConnection.TEST_GROUP_ID, new MockGroupMeBotConnection());

		JSONObject testObj = null;
		try {
			testObj = (JSONObject) new JSONParser().parse("{\"response\":{\"bot\":{\"bot_id\":\"test_bot_id\"}}}");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GroupMeBot.extractBotId(testObj);
		assertEquals("test_bot_id", GroupMeBot.botId);
	}

	@Test
	public void testExtractBotId_invalid() {
		GroupMeBot.init(MockGroupMeBotConnection.TEST_BOT_NAME, MockGroupMeBotConnection.TEST_VALID_TOKEN,
				MockGroupMeBotConnection.TEST_GROUP_ID, new MockGroupMeBotConnection());
		JSONObject testObj = null;
		try {
			testObj = (JSONObject) new JSONParser().parse("{\"response\":{\"bot\":{\"bot_id2\":\"test_bot_id\"}}}");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GroupMeBot.extractBotId(testObj);
		assertEquals("", GroupMeBot.botId);
		GroupMeBot.extractBotId(null);
		assertEquals("", GroupMeBot.botId);
	}

	@Test
	public void testSendMessage_valid() {
		GroupMeBot.init(MockGroupMeBotConnection.TEST_BOT_NAME, MockGroupMeBotConnection.TEST_VALID_TOKEN,
				MockGroupMeBotConnection.TEST_GROUP_ID, new MockGroupMeBotConnection());
		GroupMeBot.botId = MockGroupMeBotConnection.TEST_VALID_BOTID;
		assertTrue(GroupMeBot.sendTextMessage("message"));
	}

	@Test
	public void testSendMessage_invalid() {
		GroupMeBot.init(MockGroupMeBotConnection.TEST_BOT_NAME, MockGroupMeBotConnection.TEST_VALID_TOKEN,
				MockGroupMeBotConnection.TEST_GROUP_ID, new MockGroupMeBotConnection());
		assertFalse(GroupMeBot.sendTextMessage("message"));
		GroupMeBot.botId = MockGroupMeBotConnection.TEST_INVALID_BOTID;
		assertFalse(GroupMeBot.sendTextMessage("message"));
	}
}
