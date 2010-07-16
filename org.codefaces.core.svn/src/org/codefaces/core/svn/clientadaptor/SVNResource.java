package org.codefaces.core.svn.clientadaptor;

import java.util.Date;

public class SVNResource {

	private String url;
	private String mimetype;
	private long size;
	private String content;
	private String name;
	private Date lastChangedDate;
	private long lastChangedRevision;
	
	public SVNResource(String url, String name, String mimetype, long size, String content,
			Date lastChangedDate, long lastChangedRevision) {
		this.url = url;
		this.name = name;
		this.mimetype = mimetype;
		this.size = size;
		this.content = content;
		this.lastChangedDate = lastChangedDate;
		this.lastChangedRevision = lastChangedRevision;
	}
	
	public String getUrl(){
		return url;
	}
	
	public String getMimetype() {
		return mimetype;
	}
	
	public long getSize() {
		return size;
	}
	
	public String getContent() {
		return content;
	}
	
	public String getName() {
		return name;
	}
	
	public Date getLastChangedDate() {
		return lastChangedDate;
	}
	
	public long getLastChangedRevision() {
		return lastChangedRevision;
	}
	
	
}
