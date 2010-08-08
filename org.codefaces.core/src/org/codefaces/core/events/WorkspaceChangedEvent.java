package org.codefaces.core.events;

import java.util.EventObject;

import org.codefaces.core.models.RepoProject;
import org.codefaces.core.models.RepoWorkspace;

public class WorkspaceChangedEvent extends EventObject {
	private static final long serialVersionUID = 5870469991944398567L;

	public static final String PROJECT_ADDED = "org.codefaces.core.events.WorkspaceChangedEvent.PROJECT_ADDED";

	public static final String PROJECT_REMOVED = "org.codefaces.core.events.WorkspaceChangedEvent.PROJECT_REMOVED";

	private final RepoProject project;

	private final String type;

	public WorkspaceChangedEvent(RepoWorkspace source, String type,
			RepoProject project) {
		super(source);
		this.type = type;
		this.project = project;
	}

	public RepoWorkspace getWorkspace() {
		return (RepoWorkspace) getSource();
	}

	public RepoProject getProject() {
		return project;
	}

	public String getType() {
		return type;
	}
}
