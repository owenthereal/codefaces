package org.codefaces.core.models;

import java.util.Collections;
import java.util.Set;

public class RepoContainer extends RepoResource {
	protected Set<RepoResource> children;

	public RepoContainer(String id, String name, RepoResourceType type,
			RepoResource parent) {
		super(id, name, type, parent);
	}

	public Set<RepoResource> getChildren() {
		if (children == null) {
			children = (Set<RepoResource>) getAdapter(Set.class);
		}

		return Collections.unmodifiableSet(children);
	}
}
