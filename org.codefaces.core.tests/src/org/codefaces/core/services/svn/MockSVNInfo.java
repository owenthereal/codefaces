package org.codefaces.core.services.svn;

import java.io.File;
import java.util.Date;

import org.tigris.subversion.svnclientadapter.ISVNInfo;
import org.tigris.subversion.svnclientadapter.SVNNodeKind;
import org.tigris.subversion.svnclientadapter.SVNScheduleKind;
import org.tigris.subversion.svnclientadapter.SVNUrl;
import org.tigris.subversion.svnclientadapter.SVNRevision.Number;

//An empty mock for ISVNInfo
public class MockSVNInfo implements ISVNInfo {

	private File file;
	private SVNUrl url;
	private String urlString;
	private String uuid;
	private SVNUrl repository;
	private SVNScheduleKind schedule;
	private SVNNodeKind nodeKind;
	private String lastCommitAuthor;
	private Number revision;
	private Number lastChangedRevision;
	private Date lastChangedDate;
	private Date lastDateTextUpdate;
	private Date lastDatePropsUpdate;
	private boolean isCopied;
	private Number copyRev;
	private SVNUrl copyUrl;
	private String lockOwner;
	private Date lockCreationDate;
	private String lockComment;
	private int depth;

	public void setFile(File file) {
		this.file = file;
	}

	public void setUrl(SVNUrl url) {
		this.url = url;
	}

	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setRepository(SVNUrl repository) {
		this.repository = repository;
	}

	public void setSchedule(SVNScheduleKind schedule) {
		this.schedule = schedule;
	}

	public void setNodeKind(SVNNodeKind nodeKind) {
		this.nodeKind = nodeKind;
	}

	public void setLastCommitAuthor(String lastCommitAuthor) {
		this.lastCommitAuthor = lastCommitAuthor;
	}

	public void setRevision(Number revision) {
		this.revision = revision;
	}

	public void setLastChangedRevision(Number lastChangedRevision) {
		this.lastChangedRevision = lastChangedRevision;
	}

	public void setLastChangedDate(Date lastChangedDate) {
		this.lastChangedDate = lastChangedDate;
	}

	public void setLastDateTextUpdate(Date lastDateTextUpdate) {
		this.lastDateTextUpdate = lastDateTextUpdate;
	}

	public void setLastDatePropsUpdate(Date lastDatePropsUpdate) {
		this.lastDatePropsUpdate = lastDatePropsUpdate;
	}

	public void setCopied(boolean isCopied) {
		this.isCopied = isCopied;
	}

	public void setCopyRev(Number copyRev) {
		this.copyRev = copyRev;
	}

	public void setCopyUrl(SVNUrl copyUrl) {
		this.copyUrl = copyUrl;
	}

	public void setLockOwner(String lockOwner) {
		this.lockOwner = lockOwner;
	}

	public void setLockCreationDate(Date lockCreationDate) {
		this.lockCreationDate = lockCreationDate;
	}

	public void setLockComment(String lockComment) {
		this.lockComment = lockComment;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	@Override
	public File getFile() {
		return file;
	}

	@Override
	public SVNUrl getUrl() {
		return url;
	}

	@Override
	public String getUrlString() {
		return urlString;
	}

	@Override
	public String getUuid() {
		return uuid;
	}

	@Override
	public SVNUrl getRepository() {
		return repository;
	}

	@Override
	public SVNScheduleKind getSchedule() {
		return schedule;
	}

	@Override
	public SVNNodeKind getNodeKind() {
		return nodeKind;
	}

	@Override
	public String getLastCommitAuthor() {
		return lastCommitAuthor;
	}

	@Override
	public Number getRevision() {
		return revision;
	}

	@Override
	public Number getLastChangedRevision() {
		return lastChangedRevision;
	}

	@Override
	public Date getLastChangedDate() {
		return lastChangedDate;
	}

	@Override
	public Date getLastDateTextUpdate() {
		return lastDateTextUpdate;
	}

	@Override
	public Date getLastDatePropsUpdate() {
		return lastDatePropsUpdate;
	}

	@Override
	public boolean isCopied() {
		return isCopied;
	}

	@Override
	public Number getCopyRev() {
		return copyRev;
	}

	@Override
	public SVNUrl getCopyUrl() {
		return copyUrl;
	}

	@Override
	public String getLockOwner() {
		return lockOwner;
	}

	@Override
	public Date getLockCreationDate() {
		return lockCreationDate;
	}

	@Override
	public String getLockComment() {
		return lockComment;
	}

	@Override
	public int getDepth() {
		return depth;
	}

}
