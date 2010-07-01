package org.codefaces.core.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.codefaces.core.events.WorkspaceChangeEvent;
import org.codefaces.core.events.WorkspaceChangeListener;
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
		mockFooBranch = new RepoBranch(mockFooRepo, "fooBranchID", "fooBranch",
				false);
		mockFooBranch2 = new RepoBranch(mockFooRepo, "fooBranch2ID",
				"fooBranch", false);
		branchesForFooRepo.add(mockFooBranch);
		branchesForFooRepo.add(mockFooBranch2);

		final Set<RepoResource> branchesForBazRepo = new HashSet<RepoResource>();
		mockBazRepo = new Repo("http://abc.com", "baz", null) {
			@Override
			public Set<RepoResource> getChildren() {
				return branchesForBazRepo;
			}
		};
		mockBazBranch = new RepoBranch(mockBazRepo, "bazBranchID", "bazBranch",
				false);
		branchesForBazRepo.add(mockBazBranch);
	}

	@Test
	public void test_update_repo_branch() {
		assertNull(ws.getWorkingBranch());

		ws.update(mockFooBranch);
		assertEquals(mockFooRepo, ws.getWorkingBranch().getRepo());
		assertEquals(mockFooBranch, ws.getWorkingBranch());

		for (int i = 0; i < 3; i++) {
			WorkspaceChangeListener listener = new WorkspaceChangeListener() {

				@Override
				public void workspaceChanged(WorkspaceChangeEvent evt) {
					assertEquals(mockBazBranch, evt.getRepoBranch());
					assertEquals(mockBazRepo, evt.getRepoBranch().getRepo());
				}
			};

			ws.addWorkspaceChangeListener(listener);
		}

		ws.update(mockBazBranch);
	}

	@Test
	public void test_update_branch_normal() {
		ws.update(mockFooBranch);

		for (int i = 0; i < 3; i++) {
			WorkspaceChangeListener listener = new WorkspaceChangeListener() {

				@Override
				public void workspaceChanged(WorkspaceChangeEvent evt) {
					assertEquals(mockFooBranch2, evt.getRepoBranch());
				}
			};

			ws.addWorkspaceChangeListener(listener);
		}

		ws.update(mockFooBranch2);
	}

	public void test_add_and_remove_listner() {
		WorkspaceChangeListener[] listeners = new WorkspaceChangeListener[3];
		for (int i = 0; i < 3; i++) {
			listeners[i] = new WorkspaceChangeListener() {
				@Override
				public void workspaceChanged(WorkspaceChangeEvent evt) {
					assertEquals(mockBazBranch, evt.getRepoBranch());
					assertEquals(mockBazRepo, evt.getRepoBranch().getRepo());
				}
			};
			ws.addWorkspaceChangeListener(listeners[i]);
		}

		ws.update(mockBazBranch);

		// now set the listeners[1] to fail and remove it
		listeners[1] = new WorkspaceChangeListener() {
			@Override
			public void workspaceChanged(WorkspaceChangeEvent evt) {
				fail("I have been removed");
			}
		};

		ws.removeWorkspaceChangeListener(listeners[1]);
		ws.update(mockBazBranch);
	}

}
