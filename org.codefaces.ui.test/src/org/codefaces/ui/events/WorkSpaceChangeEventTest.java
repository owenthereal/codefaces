package org.codefaces.ui.events;

import static org.junit.Assert.*;

import java.util.EnumSet;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.ui.resources.Workspace.Resources;
import org.junit.Before;
import org.junit.Test;

public class WorkSpaceChangeEventTest {

	private Repo mockRepo;
	private RepoBranch mockBranch;
	
	@Before
	public void setUp(){
		mockRepo = new Repo(null, "foo", null);
		mockBranch = new RepoBranch(mockRepo, null, "bar");
	}
	
	@Test
	public void test_constructor_branch_changed(){
		WorkSpaceChangeEvent evt = new WorkSpaceChangeEvent(new Object(), mockBranch);
		assertEquals(evt.getResourcesChanged(), EnumSet.of(Resources.BRANCH));
		assertNull(evt.getRepo());
		assertEquals(evt.getRepoBranch(), mockBranch);
	}
	
	@Test
	public void test_constructor_repo_changed(){
		WorkSpaceChangeEvent evt = new WorkSpaceChangeEvent(new Object(), mockRepo,
				mockBranch);
		assertEquals(evt.getResourcesChanged(), EnumSet.of(Resources.BRANCH, Resources.REPO));
		assertEquals(evt.getRepo(), mockRepo);
		assertEquals(evt.getRepoBranch(), mockBranch);
	}
}
