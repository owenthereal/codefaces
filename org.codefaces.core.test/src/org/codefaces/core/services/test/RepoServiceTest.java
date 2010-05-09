package org.codefaces.core.services.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoItem;
import org.codefaces.core.models.RepoRoot;
import org.codefaces.core.models.RepoType;
import org.codefaces.core.services.RepoService;
import org.junit.Before;
import org.junit.Test;

public class RepoServiceTest {
	private RepoService repoService;

	private static String TEST_HTTP_GITHUB_URL = "http://github.com/jingweno/ruby_grep";

	@Before
	public void setUp() {
		repoService = new RepoService();
	}

	@Test
	public void test_getRepo_GetUserNameAndRepoNameFromHttpUrl() {
		Repo repo = repoService.getRepo(TEST_HTTP_GITHUB_URL);

		assertEquals("jingweno", repo.getUserName());
		assertEquals("ruby_grep", repo.getRepoName());
	}

	@Test
	public void test_getRoot_GetRepoRootParent() {
		Repo repo = new Repo("jingweno", "ruby_grep");
		RepoRoot repoRoot = repoService.getRoot(repo);

		assertNull(repoRoot.getParent());
	}

	@Test
	public void test_createGithubRepoShowUrl() {
		String githubShowUrl = repoService.createGithubRepoShowUrl("jingweno",
				"ruby_grep");

		assertEquals(
				"http://github.com/api/v2/json/repos/show/jingweno/ruby_grep/branches",
				githubShowUrl);
	}

	@Test
	public void test_getRoot_GetRepoRootChildren() {
		final List<RepoItem> mockRepoItems = new ArrayList<RepoItem>();
		RepoRoot mockRoot = new RepoRoot(null, mockRepoItems, RepoType.FOLDER,
				null);
		mockRepoItems.add(new RepoItem(mockRoot, null, RepoType.FILE, null));
		mockRepoItems.add(new RepoItem(mockRoot, null, RepoType.FILE, null));

		repoService = new RepoService() {
			@Override
			protected List<RepoItem> retrieveGithubRootFileList(
					String githubRepoShowUrl) {
				return mockRepoItems;
			}
		};

		Repo repo = new Repo("jingweno", "ruby_grep");
		RepoRoot repoRoot = repoService.getRoot(repo);

		assertArrayEquals(mockRepoItems.toArray(), repoRoot.getChildren()
				.toArray());
	}
}
