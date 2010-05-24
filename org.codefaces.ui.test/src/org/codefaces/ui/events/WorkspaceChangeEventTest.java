package org.codefaces.ui.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.codefaces.core.events.WorkspaceChangeEvent;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.junit.Before;
import org.junit.Test;

public class WorkspaceChangeEventTest {

	private Repo mockRepo;
	private RepoBranch mockBranch;

	@Before
	public void setUp() {
		mockRepo = new Repo(null, "foo", null);
		mockBranch = new RepoBranch(mockRepo, null, "bar");
	}

	@Test
	public void test_constructor_branch_changed() {
		WorkspaceChangeEvent evt = new WorkspaceChangeEvent(new Object(),
				mockBranch);
		assertNull(evt.getRepoBranch().getRepo());
		assertEquals(evt.getRepoBranch(), mockBranch);
	}

	@Test
	public void test_constructor_repo_changed() {
		WorkspaceChangeEvent evt = new WorkspaceChangeEvent(this,
				mockBranch);
		assertEquals(evt.getRepoBranch().getRepo(), mockRepo);
		assertEquals(evt.getRepoBranch(), mockBranch);
	}
}
