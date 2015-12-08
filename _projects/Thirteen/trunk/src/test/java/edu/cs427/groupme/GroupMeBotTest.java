package edu.cs427.groupme;

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
		JSONObject mockObject = null;
		try {
			mockObject = (JSONObject) new JSONParser().parse("{\"response\":{\"bot\":{\"bot_id\":\"" + "botid" + "\"}}}");
		} catch (ParseException e) {
			fail();
		}
		Mockito.when(mockedConn.register(Matchers.anyString(), Matchers.anyString(), Matchers.anyString())).thenReturn(mockObject);
		GroupMeBot.init("botname", "token", "groupid", mockedConn);
		
		assertTrue(GroupMeBot.register());
		assertFalse(GroupMeBot.isUnregistered());
		assertEquals("botid", GroupMeBot.botId);
	}

	@Test
	public void testRegister_nullResponse() {
		Mockito.when(mockedConn.register(Matchers.anyString(), Matchers.anyString(), Matchers.anyString())).thenReturn(null);
		GroupMeBot.init("botname", "token", "groupid", mockedConn);
		assertFalse(GroupMeBot.register());
		assertTrue(GroupMeBot.isUnregistered());
		assertEquals("", GroupMeBot.botId);
	}

	@Test
	public void testRegister_invalidJSONResponse() {
		JSONObject mockObject = null;
		try {
			mockObject = (JSONObject) new JSONParser().parse("{\"response2\":\"botid\"}");
		} catch (ParseException e) {
			fail();
		}
		Mockito.when(mockedConn.register(Matchers.anyString(), Matchers.anyString(), Matchers.anyString())).thenReturn(mockObject);
		GroupMeBot.init("botname", "token", "groupid", mockedConn);
		
		assertTrue(GroupMeBot.register());
		assertTrue(GroupMeBot.isUnregistered());
		assertEquals("", GroupMeBot.botId);
	}

	@Test
	public void testSendMessage_201Response() {
		Mockito.when(mockedConn.sendMessage(Matchers.anyString(), Matchers.anyString())).thenReturn(201);
		GroupMeBot.init("botname", "token", "groupid", mockedConn);
		GroupMeBot.botId = "botid";
		assertTrue(GroupMeBot.sendTextMessage("message"));
	}

	@Test
	public void testSendMessage_420Response() {
		Mockito.when(mockedConn.sendMessage(Matchers.anyString(), Matchers.anyString())).thenReturn(420);
		GroupMeBot.init("botname", "token", "groupid", mockedConn);
		GroupMeBot.botId = "botid";
		assertFalse(GroupMeBot.sendTextMessage("message"));
		Mockito.verify(mockedConn, Mockito.times(2)).sendMessage(Matchers.eq("botid"), Matchers.anyString());
	}
	
	@Test
	public void testSendMessage_500Response() {
		Mockito.when(mockedConn.sendMessage(Matchers.anyString(), Matchers.anyString())).thenReturn(500);
		GroupMeBot.init("botname", "token", "groupid", mockedConn);
		GroupMeBot.botId = "botid";
		assertFalse(GroupMeBot.sendTextMessage("message"));
		Mockito.verify(mockedConn, Mockito.times(1)).sendMessage(Matchers.eq("botid"), Matchers.anyString());
	}
}
