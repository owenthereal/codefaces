package org.codefaces.core.github.internal.operations;

import static org.junit.Assert.assertEquals;

import org.codefaces.core.github.internal.operations.GitHubOperationUtil;
import org.junit.Test;

public class GitHubOperationUtilTest {
	private static final String TEST_URI_CONTEXT = "http://github.com/api/v2/json/tree/show/";

	@Test
	public void testMakeURI() {
		String uri = GitHubOperationUtil.makeURI(TEST_URI_CONTEXT, "jingweno",
				"ruby_grep", "UUID");

		assertEquals(TEST_URI_CONTEXT + "/jingweno/ruby_grep/UUID", uri);
	}
}
