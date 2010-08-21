package org.codefaces.core.models;

import org.codefaces.core.services.SCMService;

public class RepoFileInfo extends RepoResourceInfo {
	private String mimeType;

	private String mode;

	private long size;

	private String content;

	public RepoFileInfo(RepoFile context) {
		super(context);
		fetchInfo(context);
	}

	public RepoFileInfo(RepoFile context, String content, String mimeType,
			String mode, int size) {
		super(context);
		fill(content, mimeType, mode, size);
	}

	/**
	 * This constructor is only for data transportation, no context information
	 * is set.
	 */
	public RepoFileInfo(String content, String mimeType, String mode, long size) {
		super(null);
		fill(content, mimeType, mode, size);
	}

	private void fill(String content, String mimeType, String mode, long size) {
		this.content = content;
		this.mimeType = mimeType;
		this.mode = mode;
		this.size = size;
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	private void fetchInfo(RepoFile file) {
		SCMService scmService = SCMService.getCurrent();
		RepoFileInfo info = scmService.fetchFileInfo(file);
		fill(info.content, info.mimeType, info.mode, info.size);
	}

	@Override
	public RepoFile getContext() {
		return (RepoFile) super.getContext();
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

	public long getSize() {
		return size;
	}
}
