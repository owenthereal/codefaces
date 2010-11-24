package org.codefaces.core.svn.internal.clientadaptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.tmatesoft.svn.core.ISVNDirEntryHandler;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;

class DirEntryInfoCollector implements ISVNDirEntryHandler {

	private List<SVNDirectoryEntry> svnDirectoryEntries;
	
	public DirEntryInfoCollector(){
		svnDirectoryEntries = new ArrayList<SVNDirectoryEntry>();
	}
	
	@Override
	public void handleDirEntry(SVNDirEntry dirEntry) throws SVNException {
		svnDirectoryEntries.add(new SVNDirectoryEntry(dirEntry.getURL().toString(), 
				dirEntry.getName(), getResourceKind(dirEntry.getKind()), 
				dirEntry.getSize(), dirEntry.getDate(), dirEntry.getRevision()));
	}

	private SVNResourceKind getResourceKind(SVNNodeKind kind){
		if(kind == SVNNodeKind.DIR) return SVNResourceKind.DIRECTORY;
		if(kind == SVNNodeKind.FILE) return SVNResourceKind.FILE;
		if(kind == SVNNodeKind.NONE) return SVNResourceKind.NONE;
		return SVNResourceKind.UNKNOWN;
	}
	
	public List<SVNDirectoryEntry> getEntries(){
		return Collections.unmodifiableList(svnDirectoryEntries);
	}
}
