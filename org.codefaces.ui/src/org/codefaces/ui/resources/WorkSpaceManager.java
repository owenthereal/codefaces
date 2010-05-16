package org.codefaces.ui.resources;


public class WorkSpaceManager {
	private WorkSpace workspace;

	private static WorkSpaceManager INSTANCE;

	private WorkSpaceManager() {
		workspace = new WorkSpace();
	}

	public static WorkSpaceManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new WorkSpaceManager();
		}
		return INSTANCE;
	}
	
	public WorkSpace getWorkSpace(){
		return workspace;
	}
}
