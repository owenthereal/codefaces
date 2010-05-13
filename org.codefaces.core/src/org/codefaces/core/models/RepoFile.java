package org.codefaces.core.models;

public class RepoFile extends RepoFileLite {
	private String mimeType;

	private String mode;

	private int size;

	private String content;

	public RepoFile(String id, String name, String content, String mimeType,
			String mode, int size, RepoResource parent) {
		super(id, name, parent);
		this.content = content;
		this.mimeType = mimeType;
		this.mode = mode;
		this.size = size;
	}

	public String getContent() {
		return content;
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
