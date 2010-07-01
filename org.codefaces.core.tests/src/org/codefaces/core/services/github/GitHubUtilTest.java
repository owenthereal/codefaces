package org.codefaces.core.services.github;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GitHubUtilTest {
	private static final String TEST_URI_CONTEXT = "http://github.com/api/v2/json/tree/show/";

	@Test
	public void testMakeURI() {
		String uri = GitHubUtil.makeURI(TEST_URI_CONTEXT, "jingweno",
				"ruby_grep", "UUID");

		assertEquals(TEST_URI_CONTEXT + "/jingweno/ruby_grep/UUID", uri);
	}
}
