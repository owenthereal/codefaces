package org.codefaces.core.models;


import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.IPath;

public class RepoModelTestingUtils {

	
	private static class MockRepoFolderInfo extends RepoFolderInfo{
		public MockRepoFolderInfo() {
			super(null);
		}

		private Collection<RepoResource> children = new ArrayList<RepoResource>();
		
		public void addChild(RepoResource child){
			children.add(child);
		}
		
		@Override
		public Collection<RepoResource> getChildren() {
			return children;
		}

		@Override
		public boolean hasChildren() {
			return !getChildren().isEmpty();
		}
	}
	
	private static class MockRepoFileInfo extends RepoFileInfo{

		public MockRepoFileInfo() {
			super(null, null, null, null, 0);
		}

		private Collection<RepoResource> children = new ArrayList<RepoResource>();
		
		@Override
		public Collection<RepoResource> getChildren() {
			return children;
		}

		@Override
		public boolean hasChildren() {
			return !getChildren().isEmpty();
		}
	}
	
	private static class MockRepoInfo extends RepoInfo{
		public MockRepoInfo() {
			super(null);
		}

		private Collection<RepoResource> children = new ArrayList<RepoResource>();
		
		public void addChild(RepoResource child){
			children.add(child);
		}
		
		@Override
		public Collection<RepoResource> getChildren() {
			return children;
		}

		@Override
		public boolean hasChildren() {
			return !getChildren().isEmpty();
		}
	}
	
	
	
	public static RepoResource createMockRepoResourceFromPath(String mockRepoKind,
			String mockUrl, String mockUser, String mockPassword, IPath mockPath, boolean isFile) {
		
		MockRepoInfo mockRepoInfo = new MockRepoInfo();
		MockRepoFolderInfo mockRootInfo = new MockRepoFolderInfo();
		Repo repo = new Repo(mockRepoKind, mockUrl, "repoName",
				new RepoCredential(null, mockUser, mockPassword), mockRepoInfo, mockRootInfo);
		mockRepoInfo.addChild(repo.getRoot());
		
		String[] segments = mockPath.segments();
		RepoResource currResource = repo.getRoot();
		MockRepoFolderInfo currInfo = mockRootInfo;
		for(int i=0; i < segments.length; i++){
			MockRepoFolderInfo folderInfo = new MockRepoFolderInfo();
		
			RepoResource resource;
			if(isFile && (i == segments.length - 1)){
				MockRepoFileInfo fileInfo = new MockRepoFileInfo();
				resource = new RepoFile(repo.getRoot(), currResource,
						segments[i], segments[i], fileInfo);
			}
			else{
				resource = new RepoFolder(repo.getRoot(), currResource,
						segments[i], segments[i], RepoResourceType.FOLDER, folderInfo);
			}
			currInfo.addChild(resource);
			currResource = resource;
			currInfo = folderInfo;
		}
		
		return currResource;
	}
}
