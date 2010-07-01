package org.codefaces.core.models;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RepoResourceTest {
	@Test
	public void test_getFullPath() {
		Repo repo = new Repo("repo", "repo", null);
		RepoBranch branch = new RepoBranch(repo, "branch", "branch", false);
		RepoFolderRoot root = branch.getRoot();
		RepoFolder folder = new RepoFolder(root, root,
				"folder", "folder");
		RepoFile file = new RepoFile(root, folder, "file", "file");

		assertEquals("repo", repo.getFullPath().toString());
		assertEquals("repo/branch", branch.getFullPath().toString());
		assertEquals("/", root.getFullPath().toString());
		assertEquals("/folder", folder.getFullPath().toString());
		assertEquals("/folder/file", file.getFullPath().toString());
	}
}
