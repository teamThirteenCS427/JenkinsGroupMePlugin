package edu.cs427.groupme;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import hudson.plugins.im.IMMessage;
import hudson.plugins.im.bot.Bot;

public class GroupMeMessagePollingTest {
	private Bot mockedBot;
	private GroupMeAPIInterface mockedAPIInterface;

	@Before
	public void setUp() throws Exception {
		mockedAPIInterface = Mockito.mock(GroupMeAPIInterface.class);
		mockedBot = Mockito.mock(Bot.class);
	}

	@Test
	public void testPollParsesAndSendsToBot() throws ParseException {
		String mockJSONString = "{\"meta\": {\"code\": 200}, \"response\": {\"count\":123,\"messages\":[{\"id\":\"1234567890\",\"source_guid\":\"GUID\",\"created_at\":1302623328,\"user_id\":\"1234567890\",\"group_id\":\"1234567890\",\"name\":\"John\",\"avatar_url\":\"http://i.groupme.com/123456789\",\"text\":\"Hello world \",\"system\":true,\"favorited_by\":[\"101\",\"66\",\"1234567890\"],\"attachments\":[{\"type\":\"image\",\"url\":\"http://i.groupme.com/123456789\"},{\"type\":\"image\",\"url\":\"http://i.groupme.com/123456789\"},{\"type\":\"location\",\"lat\":\"40.738206\",\"lng\":\"-73.993285\",\"name\":\"GroupMe HQ\"},{\"type\":\"split\",\"token\":\"SPLIT_TOKEN\"},{\"type\":\"emoji\",\"placeholder\":\"\u2603\",\"charmap\":[[1,42],[2,34]]}]}]}}";
		JSONObject mockObject = (JSONObject) new JSONParser().parse(mockJSONString);
		JSONArray msgs = (JSONArray) ((JSONObject) (mockObject.get("response"))).get("messages");
		assertEquals(1, msgs.size());
		JSONObject obj = (JSONObject) msgs.get(0);
		String text = (String) obj.get("text");
		String from = (String) obj.get("name");
		String to = "FIX LATER";
		IMMessage message = new IMMessage(from, to, text, true);
		Mockito.when(mockedAPIInterface.GET(Matchers.anyString(), Matchers.any(String[].class))).thenReturn(mockObject);
		GroupMeMessagePolling testPolling = new GroupMeMessagePolling(mockedAPIInterface, mockedBot);
		testPolling.poll();
		Mockito.verify(mockedBot, Mockito.times(1)).onMessage(message);
	}

	@Test
	public void testPollNoNewMessagesDoNotSendToBot() throws ParseException {
		String mockJSONString = "{\"meta\": {\"code\": 200}, \"response\": {\"count\":123, \"messages\":[]}}";
		JSONObject mockObject = (JSONObject) new JSONParser().parse(mockJSONString);
		Mockito.when(mockedAPIInterface.GET(Matchers.anyString(), Matchers.any(String[].class))).thenReturn(mockObject);
		GroupMeMessagePolling testPolling = new GroupMeMessagePolling(mockedAPIInterface, mockedBot);
		testPolling.poll();
		Mockito.verify(mockedBot, Mockito.times(0)).onMessage(any(IMMessage.class));
	}

	@Test
	public void testPollIfNot200Response() throws ParseException {
		String mockJSONString = "{\"meta\": {\"code\": 404}, \"response\": {\"count\":123, \"messages\":[]}}";
		JSONObject mockObject = (JSONObject) new JSONParser().parse(mockJSONString);
		Mockito.when(mockedAPIInterface.GET(Matchers.anyString(), Matchers.any(String[].class))).thenReturn(mockObject);
		GroupMeMessagePolling testPolling = new GroupMeMessagePolling(mockedAPIInterface, mockedBot);
		testPolling.poll();
		Mockito.verify(mockedBot, Mockito.times(0)).onMessage(any(IMMessage.class));
	}

	@Test
	public void testPollIfNoResponseInJSON() throws ParseException {
		String mockJSONString = "{\"meta\": {\"code\": 200}}";
		JSONObject mockObject = (JSONObject) new JSONParser().parse(mockJSONString);
		Mockito.when(mockedAPIInterface.GET(Matchers.anyString(), Matchers.any(String[].class))).thenReturn(mockObject);
		GroupMeMessagePolling testPolling = new GroupMeMessagePolling(mockedAPIInterface, mockedBot);
		testPolling.poll();
		Mockito.verify(mockedBot, Mockito.times(0)).onMessage(any(IMMessage.class));
	}

	@Test
	public void testPollRemovesDuplicates() throws ParseException {
		String mockJSONString = "{\"meta\": {\"code\": 200}, \"response\": {\"count\":123,\"messages\":[{\"name\": \"Enrique\", \"text\": \"Hello World\"}, {\"name\": \"Enrique\", \"text\": \"Hello World\"}]}}";
		JSONObject mockObject = (JSONObject) new JSONParser().parse(mockJSONString);
		JSONArray msgs = (JSONArray) ((JSONObject) (mockObject.get("response"))).get("messages");
		assertEquals(2, msgs.size());
		JSONObject obj = (JSONObject) msgs.get(0);
		String text = (String) obj.get("text");
		String from = (String) obj.get("name");
		String to = "FIX LATER";
		IMMessage message = new IMMessage(from, to, text, true);
		Mockito.when(mockedAPIInterface.GET(Matchers.anyString(), Matchers.any(String[].class))).thenReturn(mockObject);
		GroupMeMessagePolling testPolling = new GroupMeMessagePolling(mockedAPIInterface, mockedBot);
		testPolling.poll();
		Mockito.verify(mockedBot, Mockito.times(1)).onMessage(Matchers.any(IMMessage.class));
	}

	@Test
	public void testPollDontRemoveNonDuplicates() throws ParseException {
		String mockJSONString = "{\"meta\": {\"code\": 200}, \"response\": {\"count\":123,\"messages\":[{\"name\": \"Enrique\", \"text\": \"Hello World\"}, {\"name\": \"Enrique\", \"text\": \"Hi\"}]}}";
		JSONObject mockObject = (JSONObject) new JSONParser().parse(mockJSONString);
		JSONArray msgs = (JSONArray) ((JSONObject) (mockObject.get("response"))).get("messages");
		assertEquals(2, msgs.size());
		JSONObject obj = (JSONObject) msgs.get(0);
		String text = (String) obj.get("text");
		String from = (String) obj.get("name");
		String to = "FIX LATER";
		IMMessage message = new IMMessage(from, to, text, true);
		JSONObject obj2 = (JSONObject) msgs.get(1);
		String text2 = (String) obj.get("text");
		String from2 = (String) obj.get("name");
		String to2 = "FIX LATER";
		IMMessage message2 = new IMMessage(from, to, text, true);
		Mockito.when(mockedAPIInterface.GET(Matchers.anyString(), Matchers.any(String[].class))).thenReturn(mockObject);
		GroupMeMessagePolling testPolling = new GroupMeMessagePolling(mockedAPIInterface, mockedBot);
		testPolling.poll();
		Mockito.verify(mockedBot, Mockito.times(1)).onMessage(message);
		Mockito.verify(mockedBot, Mockito.times(1)).onMessage(message2);

	}
}
