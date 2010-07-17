package org.codefaces.core.svn.internal.clientadaptor;

public class SVNRepoInfo {

	private String url;
	private long revision;
	private String uuid;
	
	public SVNRepoInfo(String url, String uuid, long revision){
		this.url = url;
		this.uuid = uuid;
		this.revision = revision;
	}
	
	public String getUrl() {
		return url;
	}

	public long getRevision() {
		return revision;
	}

	public String getUuid() {
		return uuid;
	} 
}
