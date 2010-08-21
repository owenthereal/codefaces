package org.codefaces.core.events;

import java.util.EventListener;

public interface WorkspaceChangedListener extends EventListener {

	public void workspaceChanged(WorkspaceChangedEvent evt);
	
}
