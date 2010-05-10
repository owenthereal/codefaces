package org.codefaces.core.models;

public class RepoFile extends RepoResource {
	private String mimeType;

	private String mode;

	private int size;

	public RepoFile(String id, String name, String mimeType, String mode,
			int size, RepoResource parent) {
		super(id, name, RepoResourceType.FILE, parent);
		this.mimeType = mimeType;
		this.mode = mode;
		this.size = size;
	}

	public String getMimeType() {
		return mimeType;
	}

	public String getMode() {
		return mode;
	}

	public int getSize() {
		return size;
	}
}
