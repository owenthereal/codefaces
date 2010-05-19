package org.codefaces.core.models;

import java.util.Collection;
import java.util.Collections;

public class RepoResourceInfo {
	protected static final Collection<RepoResource> EMPTY_CHILDREN = Collections
			.emptyList();
	private final RepoResource context;

	protected RepoResourceInfo(RepoResource context) {
		this.context = context;
	}

	public RepoResource getContext() {
		return context;
	}

	public Collection<RepoResource> getChildren() {
		return EMPTY_CHILDREN;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RepoResourceInfo other = (RepoResourceInfo) obj;
		if (context == null) {
			if (other.context != null)
				return false;
		} else if (!context.equals(other.context))
			return false;
		return true;
	}
}
