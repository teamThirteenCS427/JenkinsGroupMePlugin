package edu.cs427.groupme;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import java.io.File;
import java.io.IOException;

public class GroupMeStoredDataTest 
{
	@Before
	public void init()
	{
		try{
			GroupMeStoredData.writeToFile("testStoredData.json");
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	

	@Test
	public void testCreateFile() 
	{
		try{
			GroupMeStoredData.writeToFile("testStoredData2.json");
		}catch (IOException e){
			e.printStackTrace();
		}
		
		assertTrue(GroupMeStoredData.dataFileExists("testStoredData2.json"));
	}
	
	@Test
	public void testNullCheck_nonnullinput()
	{
		String ret = GroupMeStoredData.nullCheck("HELLO", "WORLD");
		assertEquals("HELLO", ret);
	}

	@Test
	public void testNullCheck_nullinput()
	{
		String ret = GroupMeStoredData.nullCheck(null, "WORLD");
		assertEquals("WORLD", ret);
	}
	
	@Test
	public void testFileExists()
	{
		boolean ret = GroupMeStoredData.dataFileExists("badfilepath");
		assertFalse(ret);
		
		ret = GroupMeStoredData.dataFileExists("testStoredData.json");
		assertTrue(ret);
	}
	
	@Test
	public void testReadFile()
	{
		
	}
}
