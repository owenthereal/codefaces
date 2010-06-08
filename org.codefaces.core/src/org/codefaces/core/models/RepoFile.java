package org.codefaces.core.models;

public class RepoFile extends RepoResource {
	private RepoFileInfo info;

	public RepoFile(RepoFolderRoot root, RepoResource parent, String id,
			String name) {
		super(root, parent, id, name, RepoResourceType.FILE);
	}

	@Override
	protected RepoFileInfo getInfo() {
		if (info == null) {
			info = (RepoFileInfo) super.getInfo();
		}

		return info;
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	public String getContent() {
		return getInfo().getContent();
	}

	public String getMimeType() {
		return getInfo().getMimeType();
	}

	public String getMode() {
		return getInfo().getMode();
	}

	public int getSize() {
		return getInfo().getSize();
	}
}
