package org.codefaces.core.events;

import java.util.EventListener;

public interface WorkspaceChangeListener extends EventListener {

	public void workspaceChanged(WorkspaceChangeEvent evt);
	
}
