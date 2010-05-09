package org.codefaces.core.models;

public class RepoMetaData {
	private String name;
	private String mineType;
	private String id;
	private int size;
	private String mode;

	public RepoMetaData(String id, String name, String mineType, int size,
			String mode) {
		this.id = id;
		this.name = name;
		this.mineType = mineType;
		this.size = size;
		this.mode = mode;
	}

	/**
	 * This constructor is for the convenience of constructing repo meta data
	 * that only has id and name, for example, a directory.
	 * 
	 * @param id
	 * @param name
	 */
	public RepoMetaData(String id, String name) {
		this(id, name, null, -1, null);
	}

	public String getName() {
		return name;
	}

	public String getMineType() {
		return mineType;
	}

	public String getId() {
		return id;
	}

	public int getSize() {
		return size;
	}

	public String getMode() {
		return mode;
	}
}
