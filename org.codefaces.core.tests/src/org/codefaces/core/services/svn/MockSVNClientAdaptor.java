package org.codefaces.core.services.svn;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.tigris.subversion.svnclientadapter.ISVNAnnotations;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.ISVNConflictResolver;
import org.tigris.subversion.svnclientadapter.ISVNDirEntry;
import org.tigris.subversion.svnclientadapter.ISVNDirEntryWithLock;
import org.tigris.subversion.svnclientadapter.ISVNInfo;
import org.tigris.subversion.svnclientadapter.ISVNLogMessage;
import org.tigris.subversion.svnclientadapter.ISVNLogMessageCallback;
import org.tigris.subversion.svnclientadapter.ISVNMergeInfo;
import org.tigris.subversion.svnclientadapter.ISVNNotifyListener;
import org.tigris.subversion.svnclientadapter.ISVNProgressListener;
import org.tigris.subversion.svnclientadapter.ISVNPromptUserPassword;
import org.tigris.subversion.svnclientadapter.ISVNProperty;
import org.tigris.subversion.svnclientadapter.ISVNStatus;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNDiffSummary;
import org.tigris.subversion.svnclientadapter.SVNKeywords;
import org.tigris.subversion.svnclientadapter.SVNNotificationHandler;
import org.tigris.subversion.svnclientadapter.SVNRevision;
import org.tigris.subversion.svnclientadapter.SVNRevisionRange;
import org.tigris.subversion.svnclientadapter.SVNUrl;
import org.tigris.subversion.svnclientadapter.SVNRevision.Number;

//An empty implementation of the interface ISVNClientAdapter
public class MockSVNClientAdaptor implements ISVNClientAdapter {

	@Override
	public boolean isThreadsafe() {
		return false;
	}

	@Override
	public void addNotifyListener(ISVNNotifyListener listener) {
	}

	@Override
	public void removeNotifyListener(ISVNNotifyListener listener) {
	}

	@Override
	public SVNNotificationHandler getNotificationHandler() {
		return null;
	}

	@Override
	public void setUsername(String username) {
	}

	@Override
	public void setPassword(String password) {
	}

	@Override
	public void addPasswordCallback(ISVNPromptUserPassword callback) {
	}

	@Override
	public void addConflictResolutionCallback(ISVNConflictResolver callback) {
	}

	@Override
	public void setProgressListener(ISVNProgressListener progressListener) {
	}

	@Override
	public void addFile(File file) throws SVNClientException {
	}

	@Override
	public void addDirectory(File dir, boolean recurse)
			throws SVNClientException {

	}

	@Override
	public void addDirectory(File dir, boolean recurse, boolean force)
			throws SVNClientException {

	}

	@Override
	public void checkout(SVNUrl moduleName, File destPath,
			SVNRevision revision, boolean recurse) throws SVNClientException {

	}

	@Override
	public void checkout(SVNUrl moduleName, File destPath,
			SVNRevision revision, int depth, boolean ignoreExternals,
			boolean force) throws SVNClientException {

	}

	@Override
	public long commit(File[] paths, String message, boolean recurse)
			throws SVNClientException {

		return 0;
	}

	@Override
	public long commit(File[] paths, String message, boolean recurse,
			boolean keepLocks) throws SVNClientException {

		return 0;
	}

	@Override
	public long[] commitAcrossWC(File[] paths, String message, boolean recurse,
			boolean keepLocks, boolean atomic) throws SVNClientException {

		return null;
	}

	@Override
	public ISVNDirEntry[] getList(SVNUrl url, SVNRevision revision,
			boolean recurse) throws SVNClientException {

		return null;
	}

	@Override
	public ISVNDirEntry[] getList(SVNUrl url, SVNRevision revision,
			SVNRevision pegRevision, boolean recurse) throws SVNClientException {

		return null;
	}

	@Override
	public ISVNDirEntryWithLock[] getListWithLocks(SVNUrl url,
			SVNRevision revision, SVNRevision pegRevision, boolean recurse)
			throws SVNClientException {

		return null;
	}

	@Override
	public ISVNDirEntry[] getList(File path, SVNRevision revision,
			boolean recurse) throws SVNClientException {

		return null;
	}

	@Override
	public ISVNDirEntry[] getList(File path, SVNRevision revision,
			SVNRevision pegRevision, boolean recurse) throws SVNClientException {

		return null;
	}

	@Override
	public ISVNDirEntry getDirEntry(SVNUrl url, SVNRevision revision)
			throws SVNClientException {

		return null;
	}

	@Override
	public ISVNDirEntry getDirEntry(File path, SVNRevision revision)
			throws SVNClientException {

		return null;
	}

	@Override
	public ISVNStatus getSingleStatus(File path) throws SVNClientException {

		return null;
	}

	@Override
	public ISVNStatus[] getStatus(File[] path) throws SVNClientException {

		return null;
	}

	@Override
	public ISVNStatus[] getStatus(File path, boolean descend, boolean getAll)
			throws SVNClientException {

		return null;
	}

	@Override
	public ISVNStatus[] getStatus(File path, boolean descend, boolean getAll,
			boolean contactServer) throws SVNClientException {

		return null;
	}

	@Override
	public ISVNStatus[] getStatus(File path, boolean descend, boolean getAll,
			boolean contactServer, boolean ignoreExternals)
			throws SVNClientException {

		return null;
	}

	@Override
	public void copy(File srcPath, File destPath) throws SVNClientException {

	}

	@Override
	public void copy(File srcPath, SVNUrl destUrl, String message)
			throws SVNClientException {

	}

	@Override
	public void copy(File[] srcPaths, SVNUrl destUrl, String message,
			boolean copyAsChild, boolean makeParents) throws SVNClientException {

	}

	@Override
	public void copy(SVNUrl srcUrl, File destPath, SVNRevision revision)
			throws SVNClientException {

	}

	@Override
	public void copy(SVNUrl srcUrl, File destPath, SVNRevision revision,
			boolean copyAsChild, boolean makeParents) throws SVNClientException {

	}

	@Override
	public void copy(SVNUrl srcUrl, File destPath, SVNRevision revision,
			SVNRevision pegRevision, boolean copyAsChild, boolean makeParents)
			throws SVNClientException {

	}

	@Override
	public void copy(SVNUrl srcUrl, SVNUrl destUrl, String message,
			SVNRevision revision) throws SVNClientException {

	}

	@Override
	public void copy(SVNUrl srcUrl, SVNUrl destUrl, String message,
			SVNRevision revision, boolean makeParents)
			throws SVNClientException {

	}

	@Override
	public void copy(SVNUrl[] srcUrls, SVNUrl destUrl, String message,
			SVNRevision revision, boolean copyAsChild, boolean makeParents)
			throws SVNClientException {

	}

	@Override
	public void remove(SVNUrl[] url, String message) throws SVNClientException {

	}

	@Override
	public void remove(File[] file, boolean force) throws SVNClientException {

	}

	@Override
	public void doExport(SVNUrl srcUrl, File destPath, SVNRevision revision,
			boolean force) throws SVNClientException {

	}

	@Override
	public void doExport(File srcPath, File destPath, boolean force)
			throws SVNClientException {

	}

	@Override
	public void doImport(File path, SVNUrl url, String message, boolean recurse)
			throws SVNClientException {

	}

	@Override
	public void mkdir(SVNUrl url, String message) throws SVNClientException {

	}

	@Override
	public void mkdir(SVNUrl url, boolean makeParents, String message)
			throws SVNClientException {

	}

	@Override
	public void mkdir(File file) throws SVNClientException {

	}

	@Override
	public void move(File srcPath, File destPath, boolean force)
			throws SVNClientException {

	}

	@Override
	public void move(SVNUrl srcUrl, SVNUrl destUrl, String message,
			SVNRevision revision) throws SVNClientException {

	}

	@Override
	public long update(File path, SVNRevision revision, boolean recurse)
			throws SVNClientException {

		return 0;
	}

	@Override
	public long update(File path, SVNRevision revision, int depth,
			boolean setDepth, boolean ignoreExternals, boolean force)
			throws SVNClientException {

		return 0;
	}

	@Override
	public long[] update(File[] path, SVNRevision revision, boolean recurse,
			boolean ignoreExternals) throws SVNClientException {

		return null;
	}

	@Override
	public long[] update(File[] path, SVNRevision revision, int depth,
			boolean setDepth, boolean ignoreExternals, boolean force)
			throws SVNClientException {

		return null;
	}

	@Override
	public void revert(File path, boolean recurse) throws SVNClientException {

	}

	@Override
	public ISVNLogMessage[] getLogMessages(SVNUrl url,
			SVNRevision revisionStart, SVNRevision revisionEnd)
			throws SVNClientException {

		return null;
	}

	@Override
	public ISVNLogMessage[] getLogMessages(SVNUrl url,
			SVNRevision revisionStart, SVNRevision revisionEnd,
			boolean fetchChangePath) throws SVNClientException {

		return null;
	}

	@Override
	public ISVNLogMessage[] getLogMessages(SVNUrl url, String[] paths,
			SVNRevision revStart, SVNRevision revEnd, boolean stopOnCopy,
			boolean fetchChangePath) throws SVNClientException {

		return null;
	}

	@Override
	public ISVNLogMessage[] getLogMessages(File path,
			SVNRevision revisionStart, SVNRevision revisionEnd)
			throws SVNClientException {

		return null;
	}

	@Override
	public ISVNLogMessage[] getLogMessages(File path,
			SVNRevision revisionStart, SVNRevision revisionEnd,
			boolean fetchChangePath) throws SVNClientException {

		return null;
	}

	@Override
	public ISVNLogMessage[] getLogMessages(File path,
			SVNRevision revisionStart, SVNRevision revisionEnd,
			boolean stopOnCopy, boolean fetchChangePath)
			throws SVNClientException {

		return null;
	}

	@Override
	public ISVNLogMessage[] getLogMessages(File path,
			SVNRevision revisionStart, SVNRevision revisionEnd,
			boolean stopOnCopy, boolean fetchChangePath, long limit)
			throws SVNClientException {

		return null;
	}

	@Override
	public ISVNLogMessage[] getLogMessages(File path, SVNRevision pegRevision,
			SVNRevision revisionStart, SVNRevision revisionEnd,
			boolean stopOnCopy, boolean fetchChangePath, long limit,
			boolean includeMergedRevisions) throws SVNClientException {

		return null;
	}

	@Override
	public ISVNLogMessage[] getLogMessages(SVNUrl url, SVNRevision pegRevision,
			SVNRevision revisionStart, SVNRevision revisionEnd,
			boolean stopOnCopy, boolean fetchChangePath, long limit)
			throws SVNClientException {

		return null;
	}

	@Override
	public ISVNLogMessage[] getLogMessages(SVNUrl url, SVNRevision pegRevision,
			SVNRevision revisionStart, SVNRevision revisionEnd,
			boolean stopOnCopy, boolean fetchChangePath, long limit,
			boolean includeMergedRevisions) throws SVNClientException {

		return null;
	}

	@Override
	public void getLogMessages(File path, SVNRevision pegRevision,
			SVNRevision revisionStart, SVNRevision revisionEnd,
			boolean stopOnCopy, boolean fetchChangePath, long limit,
			boolean includeMergedRevisions, String[] requestedProperties,
			ISVNLogMessageCallback callback) throws SVNClientException {

	}

	@Override
	public void getLogMessages(SVNUrl url, SVNRevision pegRevision,
			SVNRevision revisionStart, SVNRevision revisionEnd,
			boolean stopOnCopy, boolean fetchChangePath, long limit,
			boolean includeMergedRevisions, String[] requestedProperties,
			ISVNLogMessageCallback callback) throws SVNClientException {

	}

	@Override
	public InputStream getContent(SVNUrl url, SVNRevision revision,
			SVNRevision pegRevision) throws SVNClientException {

		return null;
	}

	@Override
	public InputStream getContent(SVNUrl url, SVNRevision revision)
			throws SVNClientException {

		return null;
	}

	@Override
	public InputStream getContent(File path, SVNRevision revision)
			throws SVNClientException {

		return null;
	}

	@Override
	public void propertySet(File path, String propertyName,
			String propertyValue, boolean recurse) throws SVNClientException {

	}

	@Override
	public void propertySet(File path, String propertyName, File propertyFile,
			boolean recurse) throws SVNClientException, IOException {

	}

	@Override
	public ISVNProperty propertyGet(File path, String propertyName)
			throws SVNClientException {

		return null;
	}

	@Override
	public ISVNProperty propertyGet(SVNUrl url, String propertyName)
			throws SVNClientException {

		return null;
	}

	@Override
	public ISVNProperty propertyGet(SVNUrl url, SVNRevision revision,
			SVNRevision peg, String propertyName) throws SVNClientException {

		return null;
	}

	@Override
	public void propertyDel(File path, String propertyName, boolean recurse)
			throws SVNClientException {

	}

	@Override
	public void setRevProperty(SVNUrl path, Number revisionNo, String propName,
			String propertyData, boolean force) throws SVNClientException {

	}

	@Override
	public String getRevProperty(SVNUrl path, Number revisionNo, String propName)
			throws SVNClientException {

		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getIgnoredPatterns(File path) throws SVNClientException {

		return null;
	}

	@Override
	public void addToIgnoredPatterns(File path, String pattern)
			throws SVNClientException {

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setIgnoredPatterns(File path, List patterns)
			throws SVNClientException {

	}

	@Override
	public void diff(File oldPath, SVNRevision oldPathRevision, File newPath,
			SVNRevision newPathRevision, File outFile, boolean recurse)
			throws SVNClientException {

	}

	@Override
	public void diff(File oldPath, SVNRevision oldPathRevision, File newPath,
			SVNRevision newPathRevision, File outFile, boolean recurse,
			boolean ignoreAncestry, boolean noDiffDeleted, boolean force)
			throws SVNClientException {

	}

	@Override
	public void diff(File path, File outFile, boolean recurse)
			throws SVNClientException {

	}

	@Override
	public void diff(File[] paths, File outFile, boolean recurse)
			throws SVNClientException {

	}

	@Override
	public void createPatch(File[] paths, File relativeToPath, File outFile,
			boolean recurse) throws SVNClientException {

	}

	@Override
	public void diff(SVNUrl oldUrl, SVNRevision oldUrlRevision, SVNUrl newUrl,
			SVNRevision newUrlRevision, File outFile, boolean recurse)
			throws SVNClientException {

	}

	@Override
	public void diff(SVNUrl oldUrl, SVNRevision oldUrlRevision, SVNUrl newUrl,
			SVNRevision newUrlRevision, File outFile, boolean recurse,
			boolean ignoreAncestry, boolean noDiffDeleted, boolean force)
			throws SVNClientException {

	}

	@Override
	public void diff(SVNUrl target, SVNRevision pegRevision,
			SVNRevision startRevision, SVNRevision endRevision, File outFile,
			int depth, boolean ignoreAncestry, boolean noDiffDeleted,
			boolean force) throws SVNClientException {

	}

	@Override
	public void diff(SVNUrl target, SVNRevision pegRevision,
			SVNRevision startRevision, SVNRevision endRevision, File outFile,
			boolean recurse) throws SVNClientException {

	}

	@Override
	public void diff(SVNUrl url, SVNRevision oldUrlRevision,
			SVNRevision newUrlRevision, File outFile, boolean recurse)
			throws SVNClientException {

	}

	@Override
	public void diff(File path, SVNUrl url, SVNRevision urlRevision,
			File outFile, boolean recurse) throws SVNClientException {

	}

	@Override
	public SVNKeywords getKeywords(File path) throws SVNClientException {

		return null;
	}

	@Override
	public void setKeywords(File path, SVNKeywords keywords, boolean recurse)
			throws SVNClientException {

	}

	@Override
	public SVNKeywords addKeywords(File path, SVNKeywords keywords)
			throws SVNClientException {

		return null;
	}

	@Override
	public SVNKeywords removeKeywords(File path, SVNKeywords keywords)
			throws SVNClientException {

		return null;
	}

	@Override
	public ISVNAnnotations annotate(SVNUrl url, SVNRevision revisionStart,
			SVNRevision revisionEnd) throws SVNClientException {

		return null;
	}

	@Override
	public ISVNAnnotations annotate(File file, SVNRevision revisionStart,
			SVNRevision revisionEnd) throws SVNClientException {

		return null;
	}

	@Override
	public ISVNAnnotations annotate(SVNUrl url, SVNRevision revisionStart,
			SVNRevision revisionEnd, SVNRevision pegRevision,
			boolean ignoreMimeType, boolean includeMergedRevisions)
			throws SVNClientException {

		return null;
	}

	@Override
	public ISVNAnnotations annotate(File file, SVNRevision revisionStart,
			SVNRevision revisionEnd, boolean ignoreMimeType,
			boolean includeMergedRevisions) throws SVNClientException {

		return null;
	}

	@Override
	public ISVNAnnotations annotate(File file, SVNRevision revisionStart,
			SVNRevision revisionEnd, SVNRevision pegRevision,
			boolean ignoreMimeType, boolean includeMergedRevisions)
			throws SVNClientException {

		return null;
	}

	@Override
	public ISVNProperty[] getProperties(File path) throws SVNClientException {

		return null;
	}

	@Override
	public ISVNProperty[] getProperties(SVNUrl url, SVNRevision revision,
			SVNRevision peg) throws SVNClientException {

		return null;
	}

	@Override
	public ISVNProperty[] getProperties(SVNUrl url) throws SVNClientException {

		return null;
	}

	@Override
	public ISVNProperty[] getRevProperties(SVNUrl url, Number revision)
			throws SVNClientException {

		return null;
	}

	@Override
	public void resolved(File path) throws SVNClientException {

	}

	@Override
	public void resolve(File path, int result) throws SVNClientException {

	}

	@Override
	public void createRepository(File path, String repositoryType)
			throws SVNClientException {

	}

	@Override
	public void cancelOperation() throws SVNClientException {

	}

	@Override
	public ISVNInfo getInfoFromWorkingCopy(File file) throws SVNClientException {

		return null;
	}

	@Override
	public ISVNInfo getInfo(File file) throws SVNClientException {

		return null;
	}

	@Override
	public ISVNInfo[] getInfo(File file, boolean descend)
			throws SVNClientException {

		return null;
	}

	@Override
	public ISVNInfo getInfo(SVNUrl url) throws SVNClientException {

		return null;
	}

	@Override
	public ISVNInfo getInfo(SVNUrl url, SVNRevision revision, SVNRevision peg)
			throws SVNClientException {

		return null;
	}

	@Override
	public void switchToUrl(File path, SVNUrl url, SVNRevision revision,
			boolean recurse) throws SVNClientException {

	}

	@Override
	public void switchToUrl(File path, SVNUrl url, SVNRevision revision,
			int depth, boolean setDepth, boolean ignoreExternals, boolean force)
			throws SVNClientException {

	}

	@Override
	public void switchToUrl(File path, SVNUrl url, SVNRevision revision,
			SVNRevision pegRevision, int depth, boolean setDepth,
			boolean ignoreExternals, boolean force) throws SVNClientException {

	}

	@Override
	public void setConfigDirectory(File dir) throws SVNClientException {

	}

	@Override
	public void cleanup(File dir) throws SVNClientException {

	}

	@Override
	public void merge(SVNUrl path1, SVNRevision revision1, SVNUrl path2,
			SVNRevision revision2, File localPath, boolean force,
			boolean recurse) throws SVNClientException {

	}

	@Override
	public void merge(SVNUrl path1, SVNRevision revision1, SVNUrl path2,
			SVNRevision revision2, File localPath, boolean force,
			boolean recurse, boolean dryRun) throws SVNClientException {

	}

	@Override
	public void merge(SVNUrl path1, SVNRevision revision1, SVNUrl path2,
			SVNRevision revision2, File localPath, boolean force,
			boolean recurse, boolean dryRun, boolean ignoreAncestry)
			throws SVNClientException {

	}

	@Override
	public void merge(SVNUrl path1, SVNRevision revision1, SVNUrl path2,
			SVNRevision revision2, File localPath, boolean force, int depth,
			boolean dryRun, boolean ignoreAncestry, boolean recordOnly)
			throws SVNClientException {

	}

	@Override
	public void mergeReintegrate(SVNUrl path, SVNRevision pegRevision,
			File localPath, boolean force, boolean dryRun)
			throws SVNClientException {

	}

	@Override
	public void lock(SVNUrl[] paths, String comment, boolean force)
			throws SVNClientException {

	}

	@Override
	public void unlock(SVNUrl[] paths, boolean force) throws SVNClientException {

	}

	@Override
	public void lock(File[] paths, String comment, boolean force)
			throws SVNClientException {

	}

	@Override
	public void unlock(File[] paths, boolean force) throws SVNClientException {

	}

	@Override
	public boolean statusReturnsRemoteInfo() {

		return false;
	}

	@Override
	public boolean canCommitAcrossWC() {

		return false;
	}

	@Override
	public String getAdminDirectoryName() {

		return null;
	}

	@Override
	public boolean isAdminDirectory(String name) {

		return false;
	}

	@Override
	public void relocate(String from, String to, String path, boolean recurse)
			throws SVNClientException {

	}

	@Override
	public void merge(SVNUrl url, SVNRevision pegRevision,
			SVNRevisionRange[] revisions, File localPath, boolean force,
			int depth, boolean ignoreAncestry, boolean dryRun,
			boolean recordOnly) throws SVNClientException {

	}

	@Override
	public ISVNMergeInfo getMergeInfo(File path, SVNRevision revision)
			throws SVNClientException {

		return null;
	}

	@Override
	public ISVNMergeInfo getMergeInfo(SVNUrl url, SVNRevision revision)
			throws SVNClientException {

		return null;
	}

	@Override
	public ISVNLogMessage[] getMergeinfoLog(int kind, File path,
			SVNRevision pegRevision, SVNUrl mergeSourceUrl,
			SVNRevision srcPegRevision, boolean discoverChangedPaths)
			throws SVNClientException {

		return null;
	}

	@Override
	public ISVNLogMessage[] getMergeinfoLog(int kind, SVNUrl url,
			SVNRevision pegRevision, SVNUrl mergeSourceUrl,
			SVNRevision srcPegRevision, boolean discoverChangedPaths)
			throws SVNClientException {

		return null;
	}

	@Override
	public SVNDiffSummary[] diffSummarize(SVNUrl target1,
			SVNRevision revision1, SVNUrl target2, SVNRevision revision2,
			int depth, boolean ignoreAncestry) throws SVNClientException {

		return null;
	}

	@Override
	public SVNDiffSummary[] diffSummarize(SVNUrl target,
			SVNRevision pegRevision, SVNRevision startRevision,
			SVNRevision endRevision, int depth, boolean ignoreAncestry)
			throws SVNClientException {

		return null;
	}

	@Override
	public SVNDiffSummary[] diffSummarize(File path, SVNUrl toUrl,
			SVNRevision toRevision, boolean recurse) throws SVNClientException {

		return null;
	}

	@Override
	public String[] suggestMergeSources(File path) throws SVNClientException {

		return null;
	}

	@Override
	public String[] suggestMergeSources(SVNUrl url, SVNRevision peg)
			throws SVNClientException {

		return null;
	}

	@Override
	public void dispose() {

	}

}
