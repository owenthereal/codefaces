package org.codefaces.core.langs;


public class LangParser {
	private static final String EXTENSION_CHAR = ".";

	private LangParser() {
	}

	public static Lang parse(String fileName) {
		String fileExtension = getFileExtension(fileName);
		for (Lang lang : Lang.values()) {
			if (lang.containsExtension(fileExtension)) {
				return lang;
			}
		}

		return Lang.getDefault();
	}

	private static String getFileExtension(String fileName) {
		int extensionStartIndex = fileName.lastIndexOf(EXTENSION_CHAR);
		if (extensionStartIndex == -1) {
			return fileName;
		}

		return fileName.substring(extensionStartIndex + 1, fileName.length());
	}
}
