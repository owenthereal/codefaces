package org.codefaces.ui.resources;

public class WorkSpaceManager {

	private static WorkSpaceManager INSTANCE;

	private WorkSpaceManager() {
	}

	public static WorkSpaceManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new WorkSpaceManager();
		}
		return INSTANCE;
	}

	public WorkSpace getCurrentWorkspace() {
		return WorkSpace.getCurrent();
	}
}
