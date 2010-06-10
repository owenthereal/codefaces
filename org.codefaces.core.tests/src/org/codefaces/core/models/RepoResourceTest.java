package org.codefaces.core.models;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RepoResourceTest {
	@Test
	public void test_getFullPath() {
		RepoResource parent = new RepoResource(null, null, "parent", "parent",
				null);
		RepoResource child1 = new RepoResource(null, parent, "child1",
				"child1", null);
		RepoResource child2 = new RepoResource(null, parent, "child2",
				"child2", null);
		RepoResource grandChild1 = new RepoResource(null, child1,
				"grandChild1", "grandChild1", null);

		assertEquals("parent", parent.getFullPath().toString());
		assertEquals("parent/child1", child1.getFullPath().toString());
		assertEquals("parent/child2", child2.getFullPath().toString());
		assertEquals("parent/child1/grandChild1", grandChild1.getFullPath().toString());
	}
}
