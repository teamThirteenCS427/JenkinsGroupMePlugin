package edu.cs427.groupme;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

public class GroupMeStoredDataTest {
	@Before
	public void init() {
		try {
			GroupMeStoredData.writeToFile("testStoredData.json");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCreateFile() {
		try {
			GroupMeStoredData.writeToFile("testStoredData2.json");
		} catch (IOException e) {
			e.printStackTrace();
		}

		assertTrue(GroupMeStoredData.dataFileExists("testStoredData2.json"));
	}

	@Test
	public void testNullCheck_nonnullinput() {
		String ret = GroupMeStoredData.nullCheck("HELLO", "WORLD");
		assertEquals("HELLO", ret);
	}

	@Test
	public void testNullCheck_nullinput() {
		String ret = GroupMeStoredData.nullCheck(null, "WORLD");
		assertEquals("WORLD", ret);
	}

	@Test
	public void testFileExists() {
		boolean ret = GroupMeStoredData.dataFileExists("badfilepath");
		assertFalse(ret);

		ret = GroupMeStoredData.dataFileExists("testStoredData.json");
		assertTrue(ret);
	}

	@Test
	public void testReadFile() {
		String prevname = GroupMeStoredData.getGroupMeGroupName();
		GroupMeStoredData.setGroupMeGroupName("TestName", "testStoredData.json", false);
		try {
			GroupMeStoredData.readAllData("testStoredData.json");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		assertEquals("TestName", GroupMeStoredData.getGroupMeGroupName());
		GroupMeStoredData.setGroupMeGroupName(prevname, GroupMeStoredData.FILEPATH, false);
	}

	@Test
	public void testReadFile_badpath() {
		try {
			GroupMeStoredData.readAllData("badfilepath");
			fail("Should have thrown IOException");
		} catch (IOException e) {

		} catch (ParseException e) {

		}
	}

	@Test
	public void testReadFile_badfile() {
		try {
			GroupMeStoredData.readAllData("testinvalidfile.json");
			fail("Should have thrown ParseException");
		} catch (IOException e) {

		} catch (ParseException e) {

		}
	}
}
