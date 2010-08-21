package org.codefaces.core.models;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.codefaces.core.events.WorkspaceChangedEvent;
import org.codefaces.core.events.WorkspaceChangedListener;
import org.eclipse.rwt.SessionSingletonBase;

public class RepoWorkspace {
	public static RepoWorkspace getCurrent() {
		return (RepoWorkspace) SessionSingletonBase
				.getInstance(RepoWorkspace.class);
	}

	private final Collection<WorkspaceChangedListener> changeListeners = new HashSet<WorkspaceChangedListener>();

	private Set<RepoProject> projects = new HashSet<RepoProject>();

	public void addWorkspaceChangeListener(WorkspaceChangedListener listener) {
		changeListeners.add(listener);
	}

	public void createProject(RepoFolder input) {
		RepoProject project = new RepoProject(input);
		addProject(project);
	}

	public void addProject(RepoProject project) {
		if (!projects.add(project)) {
			return;
		}

		notifyListeners(WorkspaceChangedEvent.PROJECT_ADDED, project);
	}

	public void removeProject(RepoProject project) {
		if (!projects.remove(project)) {
			return;
		}

		notifyListeners(WorkspaceChangedEvent.PROJECT_REMOVED, project);
	}

	public Collection<RepoProject> getProjects() {
		return Collections.unmodifiableCollection(projects);
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
