package org.codefaces.core.langs;

import java.util.HashSet;
import java.util.Set;

public enum Lang {
	LANG_BASH("bash", "sh"), 
	LANG_CPP("cpp", "cpp"), 
	LANG_HTML("html", "html", "htm"), 
	LANG_JAVA("java", "java"), 
	LANG_JAVASCRIPT("javascript", "js"), 
	LANG_PHP("php", "php"), 
	LANG_PYTHON("python", "py"), 
	LANG_RUBY("ruby", "rb", "RakeFile"), 
	LANG_SQL("sql", "sql"),
	LANG_XML("xml", "xml"),
	LANG_NULL("no-highlight");

	private final String name;

	private final Set<String> fileExtensions;

	private Lang(String name, String... fileExtensions) {
		this.name = name;
		this.fileExtensions = new HashSet<String>();
		for (String extension : fileExtensions) {
			this.fileExtensions.add(extension.toLowerCase());
		}
	}

	public String getName() {
		return name;
	}

	public boolean containsExtension(String extension) {
		return fileExtensions.contains(extension.toLowerCase());
	}

	public static Lang getDefault() {
		return LANG_NULL;
	}
}
