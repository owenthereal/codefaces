package org.codefaces.core.models;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class RepoResource extends RepoElement {
	private RepoResourceType type;
	private RepoResource parent;
	private RepoFolderRoot root;

	protected RepoResource(RepoFolderRoot root, RepoResource parent, String id,
			String name, RepoResourceType type) {
		super(id, name);
		this.root = root;
		this.type = type;
		this.parent = parent;
	}

	public RepoFolderRoot getRoot() {
		return root;
	}

	public RepoResource getParent() {
		return parent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		RepoResource other = (RepoResource) obj;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	public RepoResourceType getType() {
		return type;
	}

	public Collection<RepoResource> getChildren() {
		return getInfo().getChildren();
	}

	public boolean hasChildren() {
		return getInfo().hasChildren();
	}

	protected RepoResourceInfo getInfo() {
		return RepoResourceManager.getInstance().getInfo(this);
	}
	
	/**
	 * @return a list of parents from Root to current parent
	 */
	public List<RepoResource> getParents(){
		List<RepoResource> parents = new LinkedList<RepoResource>();
		RepoResource p = getParent();
		while(p != null){
			parents.add(0, p);
			p = p.getParent();
		}
		return Collections.unmodifiableList(parents);
	}

}
