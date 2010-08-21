package org.codefaces.core.github.internal.operations.dtos;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class GitHubRepoDataDTO {
	private String description;
	private int watchers;
	private boolean has_wiki;
	private Date created_at;
	private String source;
	private int open_issues;
	private String homepage;
	private int forks;
	private boolean has_issues;
	private String parent;
	private boolean fork;
	@SerializedName("private") private boolean isPrivate;
	private String name;
	private String url;
	private String owner;
	private boolean has_downloads;
	private Date pushed_at;
	
	public String getDescription() {
		return description;
	}
	public int getWatchers() {
		return watchers;
	}
	public boolean isHas_wiki() {
		return has_wiki;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public String getSource() {
		return source;
	}
	public int getOpen_issues() {
		return open_issues;
	}
	public String getHomepage() {
		return homepage;
	}
	public int getForks() {
		return forks;
	}
	public boolean isHas_issues() {
		return has_issues;
	}
	public String getParent() {
		return parent;
	}
	public boolean isFork() {
		return fork;
	}
	public boolean isPrivate() {
		return isPrivate;
	}
	public String getName() {
		return name;
	}
	public String getUrl() {
		return url;
	}
	public String getOwner() {
		return owner;
	}
	public boolean isHas_downloads() {
		return has_downloads;
	}
	public Date getPushed_at() {
		return pushed_at;
	}
	
	
}
