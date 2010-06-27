package org.codefaces.ui;

import org.codefaces.core.events.WorkspaceChangeEvent;
import org.codefaces.core.events.WorkspaceChangeListener;
import org.codefaces.core.models.Workspace;

public class CloseEditorsListener implements WorkspaceChangeListener {
	private final Workspace workspace;

	public CloseEditorsListener (Workspace workspace) {
		this.workspace = workspace;
		workspace.addWorkspaceChangeListener(this);
	}
	
	@Override
	public void workspaceChanged(WorkspaceChangeEvent evt) {
		// TODO Auto-generated method stub
		
	}
	
	

}
