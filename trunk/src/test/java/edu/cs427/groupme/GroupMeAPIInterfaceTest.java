package edu.cs427.groupme;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;



public class GroupMeAPIInterfaceTest {

//	@Before
//	public void setUp() throws Exception {
//		
//		
//		
//	}

	@Test
	public void testWithMockURLThatStreamIsCorrectlyReturnedAsJSONObject() throws Exception {
		final HttpsURLConnection mockUrlCon = mock(HttpsURLConnection.class);
		ByteArrayInputStream is = new ByteArrayInputStream("{\"test\": \"Hello World\"}".getBytes("UTF-8"));
		doReturn(is).when(mockUrlCon).getInputStream();
		(doNothing().when(mockUrlCon)).setRequestMethod(anyString());
		when(mockUrlCon.getResponseCode()).thenReturn(200);



		URLStreamHandler stubUrlHandler = new URLStreamHandler() {
		    @Override
		     protected URLConnection openConnection(URL u) throws IOException {
		        return mockUrlCon;
		     }            
		};
		
		final URL url = new URL("http://foo.bar", "foo.bar", 80, "", stubUrlHandler);
		JSONObject json = (JSONObject) new JSONParser().parse("{\"test\": \"Hello World\"}");
		GroupMeAPIInterface testApi = new GroupMeAPIInterface("foo", "bar");
		JSONObject jsonReturned = testApi.GET(url);
		assertEquals(json, jsonReturned);
	}
	
	@Test
	public void testReturnNullIfMalformedURL() throws MalformedURLException, ParseException {
		GroupMeAPIInterface testApi = new GroupMeAPIInterface("foo", "bar");
		JSONObject jsonReturned = testApi.GET("asdfsa.d.s..ds..","//////////");
		assertEquals(null, jsonReturned);
	}
	
	@Test
	public void testConstructor() throws MalformedURLException, ParseException {
		GroupMeAPIInterface testApi = new GroupMeAPIInterface("foo", "bar");
		String setToken = testApi.getGROUPME_TOKEN();
		String setID = testApi.getGROUPME_ID();
		assertEquals("foo", setToken);
		assertEquals("bar", setID);
	}

}
