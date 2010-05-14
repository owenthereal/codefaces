package org.codefaces.core.models;

import java.util.Collections;
import java.util.Set;

public class RepoContainer extends RepoResource {
	private Set<RepoResource> children;

	protected RepoContainer(Repo repo, RepoContainer parent, String id, String name,
			RepoResourceType type) {
		super(repo, parent, id, name, type);
	}

	public Set<RepoResource> getChildren() {
		if (children == null) {
			children = (Set<RepoResource>) getAdapter(Set.class);
		}

		if (children == null) {
			return Collections.emptySet();
		}

		return Collections.unmodifiableSet(children);
	}

	public boolean hasChildren() {
		return getChildren().isEmpty();
	}
}
