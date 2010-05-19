package org.codefaces.core.events;

import java.util.EventListener;

public interface WorkspaceChangeEventListener extends EventListener {

	public void workSpaceChanged(WorkspaceChangeEvent evt);
	
}
