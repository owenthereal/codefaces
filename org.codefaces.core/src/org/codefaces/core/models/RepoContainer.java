package org.codefaces.core.models;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RepoContainer extends RepoResource {
	private Set<RepoResource> children;

	protected RepoContainer(String id, String name, RepoResourceType type,
			RepoContainer parent) {
		super(id, name, type, parent);
	}

	public Set<RepoResource> getChildren() {
		return Collections.unmodifiableSet(getChildrenMuutable());
	}

	protected Set<RepoResource> getChildrenMuutable() {
		if (children == null) {
			children = (Set<RepoResource>) getAdapter(Set.class);
			
			// falls back to empty set
			if (children == null) {
				children = new HashSet<RepoResource>();
			}
		}

		return children;
	}

	void addChild(RepoResource repoResource) {
		getChildrenMuutable().add(repoResource);
	}
}
