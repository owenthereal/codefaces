package org.codefaces.ui.resources;

import static org.junit.Assert.*;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.ui.events.WorkSpaceChangeEvent;
import org.codefaces.ui.events.WorkSpaceChangeEventListener;
import org.codefaces.ui.resources.WorkSpace.Resources;
import org.junit.Before;
import org.junit.Test;

public class WorkSpaceTest {
	
	private Repo mockFooRepo;
	private RepoBranch mockFooBranch;
	private RepoBranch mockFooBranch2;
	
	private Repo mockBazRepo;
	private RepoBranch mockBazBranch;
	
	private WorkSpace ws;
	
	@Before
	public void setUp(){
		ws = new WorkSpace();
		
		Set<RepoBranch> branches =  new HashSet<RepoBranch>();
		branches.add(mockFooBranch);
		branches.add(mockFooBranch2);
		mockFooRepo = new Repo("http://abc.com", "foo", branches, null);
		mockFooBranch = new RepoBranch("fooBranchID", "fooBranch", mockFooRepo);
		mockFooBranch2 = new RepoBranch("fooBranch2ID", "fooBranch", mockFooRepo);
		
		branches =  new HashSet<RepoBranch>();
		branches.add(mockBazBranch);
		mockBazRepo = new Repo("http://abc.com", "baz", branches, null);
		mockBazBranch = new RepoBranch("bazBranchID", "bazBranch", mockBazRepo);
	}
	
	@Test
	public void test_update_repo_branch(){
		assertNull(ws.getWorkingRepo());
		assertNull(ws.getWorkingRepoBranch());
		
		ws.update(mockFooRepo, mockFooBranch);
		assertEquals(mockFooRepo, ws.getWorkingRepo());
		assertEquals(mockFooBranch, ws.getWorkingRepoBranch());
		
		for(int i=0; i<3; i++){
			WorkSpaceChangeEventListener listener = new WorkSpaceChangeEventListener() {
				
				@Override
				public void workSpaceChanged(WorkSpaceChangeEvent evt) {
					assertEquals(EnumSet.of(Resources.REPO, Resources.BRANCH),
							evt.getResourcesChanged());
					assertEquals(mockBazRepo, evt.getRepo());
					assertEquals(mockBazBranch, evt.getRepoBranch());
				}
			};
			
			ws.addWorkSpaceChangeEventListener(listener);
		}
		
		ws.update(mockBazRepo, mockBazBranch);
	}
	
	@Test
	public void test_update_branch_normal(){
		ws.update(mockFooRepo, mockFooBranch);
		
		for(int i=0; i<3; i++){
			WorkSpaceChangeEventListener listener = new WorkSpaceChangeEventListener() {
				
				@Override
				public void workSpaceChanged(WorkSpaceChangeEvent evt) {
					assertEquals(EnumSet.of(Resources.BRANCH), evt
							.getResourcesChanged());
					assertEquals(null, evt.getRepo());
					assertEquals(mockFooBranch2, evt.getRepoBranch());
				}
			};
			
			ws.addWorkSpaceChangeEventListener(listener);
		}
		
		ws.update(mockFooBranch2);
	}
	
	@Test
	public void test_update_branch_in_differnt_repo(){
		ws.update(mockFooRepo, mockFooBranch);
		
		for(int i=0; i<3; i++){
			WorkSpaceChangeEventListener listener = new WorkSpaceChangeEventListener() {
				
				@Override
				public void workSpaceChanged(WorkSpaceChangeEvent evt) {
					fail("The update is invalid. There should be no notification");
				}
			};
			
			ws.addWorkSpaceChangeEventListener(listener);
		}
		
		ws.update(mockBazBranch);
		assertEquals(mockFooRepo, ws.getWorkingRepo());
		assertEquals(mockFooBranch, ws.getWorkingRepoBranch());
		
	}

	public void test_add_and_remove_listner(){
		WorkSpaceChangeEventListener[] listeners = new WorkSpaceChangeEventListener[3];
		for (int i = 0; i < 3; i++) {
			listeners[i] = new WorkSpaceChangeEventListener() {
				@Override
				public void workSpaceChanged(WorkSpaceChangeEvent evt) {
					assertEquals(EnumSet.of(Resources.REPO, Resources.BRANCH),
							evt.getResourcesChanged());
					assertEquals(mockBazRepo, evt.getRepo());
					assertEquals(mockBazBranch, evt.getRepoBranch());
				}
			};
			ws.addWorkSpaceChangeEventListener(listeners[i]);
		}
		
		ws.update(mockBazRepo, mockBazBranch);
		
		//now set the listeners[1] to fail and remove it
		listeners[1] = new WorkSpaceChangeEventListener() {
			@Override
			public void workSpaceChanged(WorkSpaceChangeEvent evt) {
				fail("I have been removed");
			}
		};
		
		ws.removeWorkSpaceChangeEventListener(listeners[1]);
		ws.update(mockBazRepo, mockBazBranch);
	}
	
}
