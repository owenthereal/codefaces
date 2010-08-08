package org.codefaces.core.models;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

public class RepoProjectRoot extends RepoEntry {
	private static final String PROJECT_ROOT_NAME = "ProjectRoot";

	private Collection<RepoProject> projects = new HashSet<RepoProject>();

	public RepoProjectRoot() {
		super(null, null);
		this.id = UUID.randomUUID().toString();
		this.name = PROJECT_ROOT_NAME;
	}

	void addProject(RepoProject project) {
		projects.add(project);
	}

	void removeProject(RepoProject project) {
		projects.remove(project);
	}

	public Collection<RepoProject> getProjects() {
		return Collections.unmodifiableCollection(projects);
	}
}
