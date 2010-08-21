package org.codefaces.core.github.internal.operations.dtos;

public class GitHubFileDataDTO {
	private String name;
	private int size;
	private String data;
	private String sha;
	private String mode;
	private String mime_type;

	public String getName() {
		return name;
	}

	public int getSize() {
		return size;
	}

	public String getData() {
		return data;
	}

	public String getSha() {
		return sha;
	}

	public String getMode() {
		return mode;
	}

	public String getMime_type() {
		return mime_type;
	}
}
