package org.codefaces.core.svn.internal.clientadaptor;

import java.util.Date;

public class SVNDirectoryEntry {
	private String url;
	private String name;
	private long size;
	private Date lastChangedDate;
	private long lastChangedRevision;
	private SVNResourceKind resourcekind;
	
	public SVNDirectoryEntry(String url, String name, SVNResourceKind resourceKind, long size,
			Date lastChangedDate, long lastChangedRevision) {
		this.url = url;
		this.name = name;
		this.resourcekind = resourceKind;
		this.size = size;
		this.lastChangedDate = lastChangedDate;
		this.lastChangedRevision = lastChangedRevision;
	}
	
	public String getUrl(){
		return url;
	}
	
	public String getName() {
		return name;
	}
	
	public long getSize() {
		return size;
	}
	
	public Date getLastChangedDate() {
		return lastChangedDate;
	}
	
	public long getLastChangedRevision() {
		return lastChangedRevision;
	}
	
	public SVNResourceKind getResourceKind(){
		return resourcekind;
	}
}
