package org.codefaces.core.models;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RepoResourceTest {
	@Test
	public void test_getFullPath() {
		Repo repo = new Repo("test_kind", "repo", "repo", null);
		RepoFolderRoot root = repo.getRoot();
		RepoFolder folder = new RepoFolder(root, root,
				"folder", "folder");
		RepoFile file = new RepoFile(root, folder, "file", "file");

		assertEquals("repo", repo.getPath().toString());
		assertEquals("/", root.getPath().toString());
		assertEquals("/folder", folder.getPath().toString());
		assertEquals("/folder/file", file.getPath().toString());
	}
}
