package org.codefaces.core.models;

import java.net.MalformedURLException;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.codefaces.core.services.SCMService;

public class Repo extends RepoResource {
	private RepoCredential credential;

	public Repo(String url, String name, RepoCredential credential) {
		super(null, null, url, name, RepoResourceType.REPO);
		this.credential = credential;
	}

	public String getUrl() {
		return getId();
	}

	public RepoCredential getCredential() {
		return credential;
	}

	public Collection<RepoBranch> getBranches() {
		return getInfo().getBranches();
	}

	public RepoBranch getBranchByName(String branchName) {
		for (RepoBranch branch : getBranches()) {
			if (StringUtils.equals(branch.getName(), branchName)) {
				return branch;
			}
		}

		return null;
	}

	public RepoBranch getMaster() {
		for (RepoBranch branch : getBranches()) {
			if (branch.isMaster()) {
				return branch;
			}
		}

		return null;
	}

	@Override
	protected RepoInfo getInfo() {
		return (RepoInfo) super.getInfo();
	}

	public static Repo create(String url) throws MalformedURLException {
		return SCMService.getCurrent().createRepo(url);
	}
}
