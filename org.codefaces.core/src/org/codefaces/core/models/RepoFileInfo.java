package org.codefaces.core.models;

import org.codefaces.core.CodeFacesCoreActivator;
import org.codefaces.core.services.RepoService;
import org.codefaces.httpclient.RepoResponseException;

public class RepoFileInfo extends RepoResourceInfo {
	private String mimeType;

	private String mode;

	private int size;

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

	private void fill(String content, String mimeType, String mode, int size) {
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
		RepoService repoService = CodeFacesCoreActivator.getDefault()
				.getRepoService();
		try {
			RepoFileInfo info = repoService.fetchFileInfo(file);
			fill(info.content, info.mimeType, info.mode, info.size);
		} catch (RepoResponseException e) {
			e.printStackTrace();
		}
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

	public int getSize() {
		return size;
	}
}
