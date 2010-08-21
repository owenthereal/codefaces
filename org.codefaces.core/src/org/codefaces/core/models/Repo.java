package org.codefaces.core.models;

import org.apache.commons.lang.StringUtils;
import org.codefaces.core.services.SCMService;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class Repo extends RepoResource {
	private RepoCredential credential;

	private String kind;

	private RepoFolderRoot root;

	public Repo(String kind, String url, String name, RepoCredential credential) {
		super(null, null, url, name, RepoResourceType.REPO);
		this.kind = kind;
		this.credential = credential;
		this.root = new RepoFolderRoot(this, "/", "/");
	}
	
	//This method is intended to use only in testing
	protected Repo(String kind, String url, String name, RepoCredential credential, RepoInfo repoInfo, RepoFolderInfo rootInfo) {
		super(null, null, url, name, RepoResourceType.REPO, repoInfo);
		this.kind = kind;
		this.credential = credential;
		this.root = new RepoFolderRoot(this, "/", "/", rootInfo);
	}

	public RepoFolderRoot getRoot() {
		return root;
	}

	public String getUrl() {
		return getId();
	}

	public RepoCredential getCredential() {
		return credential;
	}

	@Override
	protected RepoInfo getInfo() {
		return (RepoInfo) super.getInfo();
	}


	public static Repo create(String kind, String url, String username,
			String password) {
		return SCMService.getCurrent().connect(kind, url, username, password);
	}

	public String getKind() {
		return kind;
	}

	public RepoResource getRepoResourceByPath(IPath path) {
		if(path == Path.ROOT){return root;}
		
		String[] segments = path.segments();
		RepoResource currentResource = root;
		for (int i = 0; i < segments.length; i++) {
			for (RepoResource resource : currentResource.getChildren()) {
				if (StringUtils.equals(segments[i], resource.getName())){
					if(i == segments.length -1 ){
						return resource;
					}
					else if(resource instanceof RepoFolder){
						currentResource = resource;
						break;
					}
				}
			}
		}
		return null;
	}
}
