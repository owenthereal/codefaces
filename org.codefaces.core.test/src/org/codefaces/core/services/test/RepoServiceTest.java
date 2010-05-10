package org.codefaces.core.services.test;

import org.codefaces.core.services.RepoService;
import org.junit.Before;

public class RepoServiceTest {
	private RepoService repoService;

	@Before
	public void setUp() {
		repoService = new RepoService();
	}

//	@Test
//	public void test_getRoot_GetRepoRootParent() {
//		Repo repo = new Repo("jingweno", "ruby_grep", Collections.EMPTY_LIST);
//		RepoRoot repoRoot = repoService.getRoot(repo);
//
//		assertNull(repoRoot.getParent());
//	}
}
