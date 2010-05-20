package org.codefaces.core.models.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.codefaces.core.events.WorkspaceChangeEvent;
import org.codefaces.core.events.WorkspaceChangeEventListener;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.models.Workspace;
import org.junit.Before;
import org.junit.Test;

public class WorkspaceTest {

	private Repo mockFooRepo;
	private RepoBranch mockFooBranch;
	private RepoBranch mockFooBranch2;

	private Repo mockBazRepo;
	private RepoBranch mockBazBranch;

	private Workspace ws;

	@Before
	public void setUp() {
		ws = new Workspace();

		final Set<RepoResource> branchesForFooRepo = new HashSet<RepoResource>();
		mockFooRepo = new Repo("http://abc.com", "foo", null) {
			@Override
			public Set<RepoResource> getChildren() {
				return branchesForFooRepo;
			}
		};
		mockFooBranch = new RepoBranch(mockFooRepo, "fooBranchID", "fooBranch");
		mockFooBranch2 = new RepoBranch(mockFooRepo, "fooBranch2ID",
				"fooBranch");
		branchesForFooRepo.add(mockFooBranch);
		branchesForFooRepo.add(mockFooBranch2);

		final Set<RepoResource> branchesForBazRepo = new HashSet<RepoResource>();
		mockBazRepo = new Repo("http://abc.com", "baz", null) {
			@Override
			public Set<RepoResource> getChildren() {
				return branchesForBazRepo;
			}
		};
		mockBazBranch = new RepoBranch(mockBazRepo, "bazBranchID", "bazBranch");
		branchesForBazRepo.add(mockBazBranch);
	}

	@Test
	public void test_update_repo_branch() {
		assertNull(ws.getWorkingRepoBranch());

		ws.update(mockFooBranch);
		assertEquals(mockFooRepo, ws.getWorkingRepoBranch().getRepo());
		assertEquals(mockFooBranch, ws.getWorkingRepoBranch());

		for (int i = 0; i < 3; i++) {
			WorkspaceChangeEventListener listener = new WorkspaceChangeEventListener() {

				@Override
				public void workspaceChanged(WorkspaceChangeEvent evt) {
					assertEquals(mockBazBranch, evt.getRepoBranch());
					assertEquals(mockBazRepo, evt.getRepoBranch().getRepo());
				}
			};

			ws.addWorkSpaceChangeEventListener(listener);
		}

		ws.update(mockBazBranch);
	}

	@Test
	public void test_update_branch_normal() {
		ws.update(mockFooBranch);

		for (int i = 0; i < 3; i++) {
			WorkspaceChangeEventListener listener = new WorkspaceChangeEventListener() {

				@Override
				public void workspaceChanged(WorkspaceChangeEvent evt) {
					assertEquals(mockFooBranch2, evt.getRepoBranch());
					assertEquals(null, evt.getRepoBranch().getRepo());
				}
			};

			ws.addWorkSpaceChangeEventListener(listener);
		}

		ws.update(mockFooBranch2);
	}

	@Test
	public void test_update_branch_in_differnt_repo() {
		ws.update(mockFooBranch);

		for (int i = 0; i < 3; i++) {
			WorkspaceChangeEventListener listener = new WorkspaceChangeEventListener() {

				@Override
				public void workspaceChanged(WorkspaceChangeEvent evt) {
					fail("The update is invalid. There should be no notification");
				}
			};

			ws.addWorkSpaceChangeEventListener(listener);
		}

		ws.update(mockBazBranch);
		assertEquals(mockFooBranch, ws.getWorkingRepoBranch());
		assertEquals(mockFooRepo, ws.getWorkingRepoBranch().getRepo());

	}

	public void test_add_and_remove_listner() {
		WorkspaceChangeEventListener[] listeners = new WorkspaceChangeEventListener[3];
		for (int i = 0; i < 3; i++) {
			listeners[i] = new WorkspaceChangeEventListener() {
				@Override
				public void workspaceChanged(WorkspaceChangeEvent evt) {
					assertEquals(mockBazBranch, evt.getRepoBranch());
					assertEquals(mockBazRepo, evt.getRepoBranch().getRepo());
				}
			};
			ws.addWorkSpaceChangeEventListener(listeners[i]);
		}

		ws.update(mockBazBranch);

		// now set the listeners[1] to fail and remove it
		listeners[1] = new WorkspaceChangeEventListener() {
			@Override
			public void workspaceChanged(WorkspaceChangeEvent evt) {
				fail("I have been removed");
			}
		};

		ws.removeWorkSpaceChangeEventListener(listeners[1]);
		ws.update(mockBazBranch);
	}

}
