package org.codefaces.core.models;

import org.eclipse.core.runtime.PlatformObject;

public class RepoResource extends PlatformObject {
	protected final String id;
	protected final String name;
	protected final RepoResourceType type;
	private final RepoResource parent;

	public RepoResource(String id, String name, RepoResourceType type,
			RepoResource parent) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.parent = parent;
	}

	public RepoResource getParent() {
		return parent;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public RepoResourceType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
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
		RepoResource other = (RepoResource) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		return true;
	}

}
