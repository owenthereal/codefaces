package org.codefaces.core.events;

import java.util.EventObject;

import org.codefaces.core.models.RepoFolder;

public class WorkspaceChangeEvent extends EventObject {
	private static final long serialVersionUID = 5870469991944398567L;

	private final RepoFolder newRepoBranch;

	/**
	 * Constructor for working branch changed
	 * 
	 * @param source
	 *            the event source object
	 * @param newRepoBranch
	 *            the new repository branch
	 */
	public WorkspaceChangeEvent(Object source, RepoFolder newRepoBranch) {
		super(source);
		this.newRepoBranch = newRepoBranch;
	}

	/**
	 * @return the new working branch. null if no branch changed
	 */
	public RepoFolder getRepoBranch() {
		return newRepoBranch;
	}
}
