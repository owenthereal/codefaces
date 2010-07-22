package org.codefaces.core.events;

import java.util.EventObject;

import org.codefaces.core.models.RepoFolder;

public class WorkspaceChangeEvent extends EventObject {
	private static final long serialVersionUID = 5870469991944398567L;

	private final RepoFolder newBaseDirectory;

	/**
	 * Constructor for working base directory changed
	 * 
	 * @param source
	 *            the event source object
	 * @param newRepoBranch
	 *            the new repository branch
	 */
	public WorkspaceChangeEvent(Object source, RepoFolder newBaseDirectory) {
		super(source);
		this.newBaseDirectory = newBaseDirectory;
	}

	/**
	 * @return the new working base directory. null if no diectory changed
	 */
	public RepoFolder getBaseDirectory() {
		return newBaseDirectory;
	}
}
