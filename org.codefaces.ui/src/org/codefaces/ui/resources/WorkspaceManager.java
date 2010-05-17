package org.codefaces.ui.resources;

public class WorkspaceManager {

	private static WorkspaceManager INSTANCE;

	private WorkspaceManager() {
	}

	public static WorkspaceManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new WorkspaceManager();
		}
		return INSTANCE;
	}

	public Workspace getCurrentWorkspace() {
		return Workspace.getCurrent();
	}
}
