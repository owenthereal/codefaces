package org.codefaces.core.models.test;

import static org.junit.Assert.*;

import org.codefaces.core.models.Workspace;
import org.junit.Test;

public class WorkspaceManagerTest {
	
	@Test
	public void test_getInstance(){
		//Singleton test
		WorkspaceManager instance1 = WorkspaceManager.getInstance();
		WorkspaceManager instance2 = WorkspaceManager.getInstance();
		assertEquals(instance1, instance2);
	}
	
	@Test
	public void test_getWorkSpace(){
		//Singleton test
		Workspace ws1 = WorkspaceManager.getInstance().getCurrentWorkspace();
		Workspace ws2 = WorkspaceManager.getInstance().getCurrentWorkspace();
		assertEquals(ws1, ws2);
	}
	
}
