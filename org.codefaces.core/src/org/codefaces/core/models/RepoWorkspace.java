package org.codefaces.core.models;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.codefaces.core.events.WorkspaceChangedEvent;
import org.codefaces.core.events.WorkspaceChangedListener;
import org.eclipse.rwt.SessionSingletonBase;

public class RepoWorkspace {
	public static RepoWorkspace getCurrent() {
		return (RepoWorkspace) SessionSingletonBase
				.getInstance(RepoWorkspace.class);
	}

	private final List<WorkspaceChangedListener> changeListeners = new CopyOnWriteArrayList<WorkspaceChangedListener>();

	private ConcurrentHashMap<RepoProject, RepoProject> projects = new ConcurrentHashMap<RepoProject, RepoProject>();

	public void addWorkspaceChangeListener(WorkspaceChangedListener listener) {
		changeListeners.add(listener);
	}

	public void createProject(RepoFolder input) {
		RepoProject project = new RepoProject(input);
		projects.putIfAbsent(project, project);
		notifyListeners(WorkspaceChangedEvent.PROJECT_ADDED, project);
	}

	public Collection<RepoProject> getProjects() {
		return Collections.unmodifiableCollection(projects.keySet());
	}

	private void notifyListeners(String type, RepoProject project) {
		WorkspaceChangedEvent event = new WorkspaceChangedEvent(this, type,
				project);
		for (WorkspaceChangedListener listener : changeListeners) {
			listener.workspaceChanged(event);
		}
	}

	public void removeWorkspaceChangeListener(WorkspaceChangedListener listener) {
		changeListeners.remove(listener);
	}
}
