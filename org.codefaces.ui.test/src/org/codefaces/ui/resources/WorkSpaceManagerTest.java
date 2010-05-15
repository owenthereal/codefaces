package org.codefaces.ui.resources;

import static org.junit.Assert.*;

import org.junit.Test;

public class WorkSpaceManagerTest {
	
	@Test
	public void test_getInstance(){
		//Singleton test
		WorkSpaceManager instance1 = WorkSpaceManager.getInstance();
		WorkSpaceManager instance2 = WorkSpaceManager.getInstance();
		assertEquals(instance1, instance2);
	}
	
	@Test
	public void test_getWorkSpace(){
		//Singleton test
		WorkSpace ws1 = WorkSpaceManager.getInstance().getWorkSpace();
		WorkSpace ws2 = WorkSpaceManager.getInstance().getWorkSpace();
		assertEquals(ws1, ws2);
	}
	
}
