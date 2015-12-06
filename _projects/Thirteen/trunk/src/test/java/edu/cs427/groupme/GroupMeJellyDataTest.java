package edu.cs427.groupme;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.cs427.groupme.GroupMeJellyData.StoredData;

public class GroupMeJellyDataTest {
	
	@Test
	public void test_StoredDataConstructor(){
		StoredData data = new StoredData("1", "2", "3", "4", "5");
		String bot_id = data.getGroupMeGroupId();
		String bot_name = data.getGroupMeBotName();
		String token = data.getGroupMeToken();
		String group_name = data.getGroupMeGroupName();
		String cmd_prefix = data.getBotCommandPrefix();
		assertEquals("1", bot_id);
		assertEquals("2", token);
		assertEquals("3", bot_name);
		assertEquals("4", group_name);
		assertEquals("5", cmd_prefix);
	}
	
	@Test
	public void test_StoredDatatoString(){
		StoredData data = new StoredData("1", "2", "3", "4", "5");
		String expected = "[1, 2, 3, 4, 5]";
		String actual = data.toString();
		assertEquals(expected, actual);
	}
	
	@Test
	public void test_StoredDatatoString_nullcheck(){
		StoredData data = new StoredData(null, null, null, null, null);
		String expected = "[null, null, null, null, null]";
		String actual = data.toString();
		assertEquals(expected, actual);
	}
	
	/*
	@Test
	public void test_StoredDataDescriptor(){
		StoredData data = new StoredData("1", "2", "3", "4", "5");
		StoredDataDescriptor d = (StoredDataDescriptor) data.getDescriptor();
		assertNotNull(d);
		String expected_classname = "StoredData";
		String actual_classname = d.getDisplayName();
		assertEquals(expected_classname, actual_classname);
	}*/
}