package edu.cs427.groupme;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

public class GroupMeStoredDataTest {

	@Test
	public void testCreateFile() {
		GroupMeStoredData.init();
		assertTrue(GroupMeStoredData.dataFileExists());
	}
	
	@Test
	public void testSetBotId() {
		String id = GroupMeStoredData.getGroupMeToken();
		
		File f = new File("groupMeData.json");
		f.delete();
		GroupMeStoredData.setGroupMeToken(id);
		
		assertTrue(GroupMeStoredData.dataFileExists());
	}

}
