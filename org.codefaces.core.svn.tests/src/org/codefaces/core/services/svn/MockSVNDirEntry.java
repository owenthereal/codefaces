package org.codefaces.core.services.svn;

import java.util.Date;

import org.tigris.subversion.svnclientadapter.ISVNDirEntry;
import org.tigris.subversion.svnclientadapter.SVNNodeKind;
import org.tigris.subversion.svnclientadapter.SVNRevision.Number;

//An empty mock for ISVNDirEntry
public class MockSVNDirEntry implements ISVNDirEntry {
	
	private String path;
	private Date lastChangedDate;
	private Number lastChangedRevision;
	private boolean hasProps;
	private String lastCommitAuthor;
	private long size;

	private SVNNodeKind nodeKind;

	public void setPath(String path) {
		this.path = path;
	}

	public void setLastChangedDate(Date lastChangeDate) {
		this.lastChangedDate = lastChangeDate;
	}

	public void setLastChangedRevision(Number lastChangedRevision) {
		this.lastChangedRevision = lastChangedRevision;
	}

	public void setHasProps(boolean hasProps) {
		this.hasProps = hasProps;
	}

	public void setLastCommitAuthor(String lastCommitAuthor) {
		this.lastCommitAuthor = lastCommitAuthor;
	}
	
	public void setNodeKind(SVNNodeKind nodeKind) {
		this.nodeKind = nodeKind;
	}

	public void setSize(long size) {
		this.size = size;
	}
	
	@Override
	public String getPath() {
		return path;
	}

	@Override
	public Date getLastChangedDate() {
		return lastChangedDate;
	}

	@Override
	public Number getLastChangedRevision() {
		return lastChangedRevision;
	}

	@Override
	public boolean getHasProps() {
		return hasProps;
	}

	@Override
	public String getLastCommitAuthor() {
		return lastCommitAuthor;
	}

	@Override
	public SVNNodeKind getNodeKind() {
		return nodeKind;
	}

	@Override
	public long getSize() {
		return size;
	}

}
